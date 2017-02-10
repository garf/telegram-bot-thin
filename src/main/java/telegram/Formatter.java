package telegram;

public class Formatter {
    public static String format(String text) {
        return formatBold(text);
    }

    private static String formatBold(String text) {
        text = text.replace("<b>", "<b>");

        return text.replace("</b>", "</b>");
    }
}
