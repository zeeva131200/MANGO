package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class P_RequestRide extends AppCompatActivity{
    private TextView pickup,dest,time,date,seat,name,d_iD2;
    private Button request;
    private APIService apiService;
    private FirebaseAuth fAuth;
    DatabaseReference db,db1;
    public RelativeLayout lyt;
    private Object nam;

    public int pos;
    String n,nama;
    private ArrayList<P_RideItem> ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_request_ride);
        Bundle extras = getIntent().getExtras();

        String n1 = extras.getString("n1");
        String p1 = extras.getString("p1");
        String d1 = extras.getString("dest1");
        String dt1 = extras.getString("dt1");
        String tm1 = extras.getString("tm1");
        String st1 = extras.getString("st1");
        String d_id2 = extras.getString("d_id2");


//        if(extras!=null){
//            pos=extras.getInt("pos");
//            Toast.makeText(P_RequestRide.this, "pos:"+pos,Toast.LENGTH_SHORT).show();
//        }


        name = (TextView) findViewById(R.id.name1);
        pickup = (TextView) findViewById(R.id.pickup1);
        dest = (TextView) findViewById(R.id.destination1);
        time = (TextView) findViewById(R.id.time1);
        date = (TextView) findViewById(R.id.date1);
        seat = (TextView) findViewById(R.id.seatNo1);
        d_iD2 = (TextView) findViewById(R.id.D_iD2);


        name.setText(n1);
        pickup.setText(p1);
        dest.setText(d1);
        time.setText(tm1);
        date.setText(dt1);
        seat.setText(st1);
        d_iD2.setText(d_id2);


        //Send Notification once request ride
        request = findViewById(R.id.Rq2Btn);
        //DriverId = d_id2.getT


        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Driver");
        db1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Passenger");
        fAuth = FirebaseAuth.getInstance();
        retName();

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ADD REQUEST TO DRIVER DB
                String user_id = d_id2;
                String key = "Ride request";

                Toast.makeText(P_RequestRide.this, "name"+nama, Toast.LENGTH_SHORT).show();
                String nm = nama;
                D_RideItem rideitem = new D_RideItem(nm,p1,d1,dt1,tm1,st1,d_id2);

                String ride_id = db.child(user_id).push().getKey();
                db.child(user_id).child(key).child(ride_id).setValue(rideitem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //refreshPage();
                            // D_Id.setText(user_id);
                            Toast.makeText(P_RequestRide.this, "Ride requested added to list", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(P_RequestRide.this, "Ride request failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //ADD REQUEST TO PASSENGER DB
                lyt = findViewById(R.id.rideItemlayout);
                String user_id1 = fAuth.getCurrentUser().getUid();;
                String key1 = "Ride request";

                String nm1 = d_id2;
                P_RideItem rideitem1 = new P_RideItem(nm1,p1,d1,dt1,tm1,st1,d_id2);

                String ride_id1 = db1.child(user_id1).push().getKey();
                db1.child(user_id1).child(key1).child(ride_id1).setValue(rideitem1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //refreshPage();
                            // D_Id.setText(user_id);
                            Toast.makeText(P_RequestRide.this, "Ride requested added to list 2", Toast.LENGTH_SHORT).show();
                            // TODO - change clicked cardview colour and setclickable=false
                            //lyt.setBackgroundResource(R.color.white);
                            //lyt.setClickable(false);
                            request.setClickable(false);
                        }
                        else{
                            Toast.makeText(P_RequestRide.this, "Ride request failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        //NOTIFICATION
        //creating a url to client class
//        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
//        request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(P_browseRide.this, "BEFORE: "+ride, Toast.LENGTH_SHORT).show();
//                Toast.makeText(P_RequestRide.this, "id"+d_iD2.getText().toString(), Toast.LENGTH_LONG).show();
//                FirebaseDatabase.getInstance().getReference().child("Tokens").child(d_iD2.getText().toString().trim()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                            String usertoken = snapshot.getValue(String.class);
//                            sendNotif(usertoken,pickup.getText().toString().trim(), dest.getText().toString().trim());
//                            Toast.makeText(P_RequestRide.this, "snapshot: "+usertoken, Toast.LENGTH_SHORT).show();
//                        }
//
//
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                //unique id for each id, help fb send notif
//                updateToken();
//            }
//        });


    }

    public void retName(){
        String user_id =fAuth.getCurrentUser().getUid();

        //asynchronous so x return
        //on data change method executes at the end, after all other method are executed
        db1.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot rideshot: snapshot.getChildren()) {
                    String key = rideshot.getKey();

                    if(key.equals("Personal Details")){
                        for(DataSnapshot nameshot: rideshot.getChildren()) {
                            String na = nameshot.getKey().toString();
                            if(na.equals("name")) {
                                nam = nameshot.getValue();
                            }
                        }
                        n = nam.toString();
                    }
                }
                assignName(n);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(P_RequestRide.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void assignName(String nm){
        nama = nm;
    }

    public void updateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);
        String user = firebaseUser.getUid();
        Toast.makeText(P_RequestRide.this, "uid curr: "+user, Toast.LENGTH_SHORT).show();
        Toast.makeText(P_RequestRide.this, "token: "+token, Toast.LENGTH_SHORT).show();
    }
    public void sendNotif(String usertoken, String title,String msg){
        Data data = new Data(title,msg);
        NotificationSender sender = new NotificationSender(data,usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code() == 200){
                    if(response.body().success!= 1){
                        Toast.makeText(P_RequestRide.this, "Failed", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
             public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}