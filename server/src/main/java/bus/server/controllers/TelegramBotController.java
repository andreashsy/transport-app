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

import static bus.server.Constants.*;

import java.io.IOException;

@BotController
public class TelegramBotController implements TelegramMvcController{
    private String token = TOKEN_TELEGRAM_BOT;

    @Override
    public String getToken() {
        return token;
    }

    @BotRequest(value = "/hello", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest hello(User user, Chat chat) {
        return new SendMessage(chat.id(), "Hello, " + user.firstName() + "!!!");
    }

    @MessageRequest("/hello {name:[\\S]+}")
    public String helloWithName(@BotPathVariable("name") String userName) {
        // Return a string if you need to reply with a simple message
        return "Hello, " + userName;
    }

    @MessageRequest("/helloCallback")
    public String helloWithCustomCallback(TelegramRequest request, User user) {
        request.setCallback(new Callback() {
            @Override
            public void onResponse(BaseRequest request, BaseResponse response) {
                // TODO
                
            }

            @Override
            public void onFailure(BaseRequest request, IOException e) {
                // TODO
            }
        });
        return "Hello, " + user.firstName() + "!";
    }

}
