import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import libs.helpers.Config;
import libs.telegram.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class App
{
    public static void main( String[] args ) {
        AnsiConsole.systemInstall();

        Config config = new Config("./config.properties");

        String key = "telegram.token";
        String telegramToken = config.get(key);

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

        bot.setUpdatesListener(updates -> {
            String newMessageText = updates.get(0).message().text();
            String newMessageSender = updates.get(0).message().from().username();
            Long chatId = updates.get(0).message().chat().id();

            logger.info(updates.toString());
            System.out.println(ansi().fg(GREEN).a("@" + newMessageSender + " sent: ").reset() + newMessageText);

            Send send = new Send(bot);

            send.message("<b>Your Telegram ID is:</b> @" + newMessageSender, chatId);

            Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton[] {
                    new KeyboardButton("What is my overall score?"),
                    new KeyboardButton("I am").requestContact(true),
                    new KeyboardButton("I am here").requestLocation(true)
                }
            )
            .oneTimeKeyboard(true)
            .resizeKeyboard(true);

            send.message("<b>Your message text was:</b> " + newMessageText, keyboard, chatId);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
