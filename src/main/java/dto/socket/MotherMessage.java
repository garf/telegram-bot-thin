package dto.socket;

import java.io.Serializable;

public class MotherMessage implements Serializable {
    private String text;
    private String[] keyboard;

    public String getText() {
        return text;
    }

    public MotherMessage setText(String text) {
        this.text = text;

        return this;
    }

    public String[] getKeyboard() {
        return keyboard;
    }

    public MotherMessage setKeyboard(String[] keyboard) {
        this.keyboard = keyboard;

        return this;
    }
}
