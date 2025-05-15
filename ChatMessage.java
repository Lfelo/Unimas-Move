package com.example.unimasmove;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private String message;
    private String sender;
    private boolean isReceived;
    private String time;

    public ChatMessage(String message, String sender, boolean isReceived) {
        this.message = message;
        this.sender = sender;
        this.isReceived = isReceived;
        this.time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public String getTime() {
        return time;
    }
}