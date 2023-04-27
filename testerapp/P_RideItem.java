package com.example.testerapp;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class P_RideItem  {
    private int image;
    private String name,pickup,destination,date,time,seat,id;
    private Button requestBtn;

    //NEEDED FOR FB
    P_RideItem(){

    }

    //VARIABLES PASSED MUST BE SIMILAR TO THT IN FB (Lowercase,spelling etc)
    // int Image, String Name,
    public P_RideItem( String name,String pickup, String destination, String date, String time, String seat, String id){
//        this.image = Image;
        this.name = name;
        this.pickup = pickup;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.seat = seat;
        this.id = id;
    }

    //    public int getImage() {
//        return image;
//    }
//
    public String getName() {
        return name;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSeat() {
        return seat;
    }

    public String getId() {
        return id;
    }

}
