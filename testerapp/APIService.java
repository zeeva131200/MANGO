package com.example.testerapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA6YtMX4E:APA91bFpaCt8gKzfhzPDojrE8D2_DmbGSz9JU141tVs744nP0gPycuTvtiJrmHHdiuBjHaehb1B638mbzeBXITYwvWcgIxECZ5YEt2vo_mH1nPIJUrn89lj_VCnp8p85IryjRQke1aeR" // Your server key from Fb
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
