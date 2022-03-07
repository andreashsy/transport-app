package bus.server.controllers;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;

import bus.server.models.BusStop;
import bus.server.models.Notification;
import bus.server.services.UserService;
import bus.server.utilities.BusHelper;

import static bus.server.Constants.*;

import java.util.List;
import java.util.Optional;

@BotController
public class TelegramBotController implements TelegramMvcController{
    private String token = TOKEN_TELEGRAM_BOT;
    private String defaultReply = """
        Command not recognised.
        List of commands:
        /arrival <5 digit bus stop code> for bus stop data
        /search <description or road name> for bus stop details
        """;

    @Autowired
    UserService userService;

    @Override
    public String getToken() {
        return token;
    }

    @MessageRequest("**")
    public String catchAllMessage() {
        return defaultReply;
    }

    @MessageRequest("/**")
    public String catchAllMessageSlash() {
        return defaultReply;
    }

    @MessageRequest("/arrival {busStopCode:[0-9]+}")
    public String getBusArrival(@BotPathVariable("busStopCode") String busStopCode) {
        BusHelper busHelper = new BusHelper();
        String jsonResp = busHelper.getBusStopById(busStopCode);
        
        return "Arrival data for bus stop " + busStopCode + ": " + Notification.parseJsonNotification(jsonResp);
    }

    @MessageRequest("/search {query:[a-z]+}")
    public String searchBusStops(@BotPathVariable("query") String rawQuery) {
        String query = rawQuery.trim().toLowerCase();
        Optional<List<BusStop>> opt = userService.searchBusStops(query);
        if (opt.isPresent()) {
            String results = "";
            for (BusStop bs:opt.get()) {
                results += "Bus Stop Code: " + bs.getBusStopCode() + " | Road nane: " + bs.getRoadName() + " | Description:  " + bs.getDescription() + "\r\n";
            }
            return results;
        }
        return "No data found for " + rawQuery;
    }

    @MessageRequest("/getfavourites")
    public String listFavouriteBusStop(User user) {
            String result = "";
            if (user.username().length() <= 1) {
                return "You do not have a telegram username";
            }
            if (!userService.doesTelegramUserExist(user.username())) {
                return "Telegram username not registered";
            }
            Optional<String> opt = userService.getUsernameFromTelegramUsername(user.username());
            if (opt.isPresent()) {
                List<BusStop> busStops = userService.getFavouriteBusStops(user.username()).get();
                for (BusStop bs:busStops) {
                    result += "Bus Stop Code: " + bs.getBusStopCode() + 
                        ", Road Name: " + bs.getRoadName() + 
                        ", Description: " + bs.getDescription() + ".";
                }
                return result;
            }
            return "Your username is not registered";
        }

    @MessageRequest("/favourite {busStopCode:[0-9]+}")
    public String favouriteBusStop(
        @BotPathVariable("busStopCode") String busStopCode,
        User user) {
            if (user.username().length() <= 1) {
                return "You do not have a telegram username";
            }
            if (!userService.doesTelegramUserExist(user.username())) {
                return "Telegram username not registered";
            }
            String username = userService.getUsernameFromTelegramUsername(user.username()).get();
            userService.addFavourite(username, busStopCode);
            return "Bus Stop saved!";
        }

}
