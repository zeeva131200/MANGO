package com.example.testerapp;

public class NotificationSender {
    public Data data; //collection of var (title&msg)
    public String to; //token

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender(){

    }
}
