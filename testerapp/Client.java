package com.example.testerapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//register client
public class Client {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
