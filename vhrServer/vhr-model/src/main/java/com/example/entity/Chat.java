package com.example.entity;

import java.util.Date;

public class Chat {
    private String from;
    private String to;
    private String message;
    private Date date;

    public Chat() {
    }

    public Chat(String from, String to, String message, Date date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
