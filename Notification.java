package com.example.unimasmove;

public class Notification {
    private String title;
    private String message;
    private String time;
    private boolean isUnread;

    public Notification(String title, String message, String time, boolean isUnread) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.isUnread = isUnread;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public boolean isUnread() {
        return isUnread;
    }
}