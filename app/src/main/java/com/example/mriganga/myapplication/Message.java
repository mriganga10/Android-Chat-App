package com.example.mriganga.myapplication;

/**
 * Created by Mriganga on 1/13/2018.
 */

public class Message {
    private String from;
    private String time;

    public Message(String from, String time, String text) {
        this.from = from;
        this.time = time;
        this.text = text;
    }

    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTime(String to) {
        this.time = to;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;

    }

    public String getText() {
        return text;
    }
}
