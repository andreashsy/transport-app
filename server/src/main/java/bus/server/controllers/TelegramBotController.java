package bus.server.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.User;

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
    private String helpReply = """
        List of commands:
        /arrival <5 digit bus stop code> ==> for bus arrival data
        /search <description or road name> ==> for bus stop details

        Commands requiring a registered Telegram username:
        /getfavourites ==> to list all favourite bus stops
        /favourite <5 digit bus stop code> ==> to add a bus stop to favourites
        /removefavourite <5 digit bus stop code> ==> to remove a bus stop from favourites
        """;
    private String catchAllMessage = "Command not recognised. Type /help for list of commands ";

    @Autowired
    UserService userService;

    @Override
    public String getToken() {
        return token;
    }

    @MessageRequest("/start")
    public String welcomeMessage() {
        return helpReply;
    }

    @MessageRequest("/help")
    public String helpMessage() {
        return helpReply;
    }

    @MessageRequest("**")
    public String catchAllMessage() {
        return catchAllMessage;
    }

    @MessageRequest("/**")
    public String catchAllMessageSlash() {
        return catchAllMessage;
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
        String telegramUsername = user.username();
        if (user.username().length() <= 1) {
            return "You do not have a telegram username";
        }
        if (!userService.doesTelegramUserExist(telegramUsername)) {
            return "Telegram username not registered";
        }
        Optional<String> opt = userService.getUsernameFromTelegramUsername(telegramUsername);
        if (opt.isPresent()) {
            List<BusStop> busStops = userService.getFavouriteBusStops(opt.get()).get();
            for (BusStop bs:busStops) {
                System.out.println(">>>> " + bs.getBusStopCode());
                result += "Bus Stop Code: " + bs.getBusStopCode() + 
                    ", Road Name: " + bs.getRoadName() + 
                    ", Description: " + bs.getDescription() + ". \r\n";
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

    @MessageRequest("/removefavourite {busStopCode:[0-9]+}")
    public String removeFavouriteBusStop(
        @BotPathVariable("busStopCode") String busStopCode,
        User user) {
            if (user.username().length() <= 1) {
                return "You do not have a telegram username";
            }
            if (!userService.doesTelegramUserExist(user.username())) {
                return "Telegram username not registered";
            }
            String username = userService.getUsernameFromTelegramUsername(user.username()).get();
            userService.deleteFavouriteBusStop(username, busStopCode);
            return "Bus Stop Deleted!";
    }

}
