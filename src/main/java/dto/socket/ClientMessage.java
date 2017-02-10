package dto.socket;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private String senderId;
    private String service = "telegram";
    private String chatId;
    private String text;
    private String additional;

    public String getSenderId() {
        return senderId;
    }

    public ClientMessage setSenderId(String senderId) {
        this.senderId = senderId;

        return this;
    }

    public String getService() {
        return service;
    }

    public ClientMessage setService(String service) {
        this.service = service;

        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public ClientMessage setChatId(String chatId) {
        this.chatId = chatId;

        return this;
    }

    public String getText() {
        return text;
    }

    public ClientMessage setText(String text) {
        this.text = text;

        return this;
    }

    public String getAdditional() {
        return additional;
    }

    public ClientMessage setAdditional(String additional) {
        this.additional = additional;

        return this;
    }
}
