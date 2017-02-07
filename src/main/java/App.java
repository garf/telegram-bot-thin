import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import libs.helpers.Config;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
            logger.info(updates.toString());
            System.out.println(ansi().fg(GREEN).a("@" + newMessageSender + " sent: ").reset() + newMessageText);

            try {
                InputStream tokensInputStream = new FileInputStream("opennlp/models/en-token.bin");
                InputStream nameInputStream = new FileInputStream("opennlp/models/en-ner-person.bin");

                TokenizerModel tokenizerModel = new TokenizerModel(tokensInputStream);
                TokenNameFinderModel nameFinderModel = new TokenNameFinderModel(nameInputStream);

                nameInputStream.close();
                tokensInputStream.close();

                Tokenizer tokenizer = new TokenizerME(tokenizerModel);
                NameFinderME nameFinder = new NameFinderME(nameFinderModel);

                String tokens[] = tokenizer.tokenize(newMessageText);
                Span nameSpans[] = nameFinder.find(tokens);
                for(Span s: nameSpans)
                    System.out.println(s.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
