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

import bus.server.models.Notification;
import bus.server.utilities.BusHelper;

import static bus.server.Constants.*;

import java.io.IOException;

@BotController
public class TelegramBotController implements TelegramMvcController{
    private String token = TOKEN_TELEGRAM_BOT;
    private String defaultReply = """
        Not recognised.
        List of commands:
        /arrival <5 digit bus stop code> for bus stop data""";

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

}
