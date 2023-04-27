package com.example.testerapp;

public class Data {
    private String Title;
    private String Msg;

    public Data(String title, String msg) {
        this.Title = title;
        this.Msg = msg;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
