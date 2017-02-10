import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import libs.helpers.Config;
import dto.socket.MotherMessage;
import telegram.Formatter;
import telegram.Send;
import mother.Connect;
import dto.socket.ClientMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class App
{
    public static void main( String[] args ) {
        AnsiConsole.systemInstall();

        Config config = new Config("./config.properties");

        String telegramToken = config.get("telegram.token");

        Boolean isTokenSet = telegramToken != null && !telegramToken.equals("SetYourToken");

        if (!isTokenSet) {
            System.out.println("Token: " + ansi().bg(RED).fg(BLACK).a("NOT SET").reset());
            System.out.println("Please set your telegram token");
            return;
        }

        System.out.println("Token: " + ansi().fg(GREEN).bold().a(telegramToken).reset());

        TelegramBot bot = TelegramBotAdapter.build(telegramToken);

        final Logger logger = LoggerFactory.getLogger(App.class);

        System.out.println(ansi().bg(GREEN).fg(BLACK).a("Bot started").reset());

        Connect mother = new Connect(config.get("mother.host"), Integer.parseInt(config.get("mother.port")));

        bot.setUpdatesListener(updates -> {
            String newMessageText = updates.get(0).message().text();
            String newMessageSender = updates.get(0).message().from().username();
            Long chatId = updates.get(0).message().chat().id();

            System.out.println(ansi().fg(GREEN).a("@" + newMessageSender + " sent: ").reset() + newMessageText);

            Send send = new Send(bot);

            if (newMessageSender == null) {
                send.message("Your username is not set. Please, do it in settings of Telegram Messenger.", chatId);
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }

            ClientMessage clientMessage = new ClientMessage();

            clientMessage.setText(newMessageText)
                    .setSenderId(newMessageSender)
                    .setChatId(chatId.toString());

            MotherMessage motherResponse = mother.send(clientMessage);

            send.message(Formatter.format(motherResponse.getText()), chatId);

            send.message("", App.makeKeyboard(motherResponse.getKeyboard()), chatId);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static ReplyKeyboardMarkup makeKeyboard(String[] keys) {
        List<KeyboardButton> buttons = new ArrayList<>();

        for (String key: keys) {
            buttons.add(new KeyboardButton(key));
        }

        KeyboardButton[] buttonsArray = buttons.toArray(new KeyboardButton[buttons.size()]);

        return new ReplyKeyboardMarkup(buttonsArray)
        .oneTimeKeyboard(true)
        .resizeKeyboard(true);
    }
}
