package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    DatabaseReference db;
    private RecyclerView r_view;
    private  ArrayList<D_RideItem> ride,listCopy;
    private Object nam;
    private D_RideItem d_rideItem;
    private D_Adapter adapter,adapter2;
    //creates presaved list view from library
    private RecyclerView.LayoutManager l_manager;
    private Button msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_fragment);

        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Driver");
        r_view = findViewById(R.id.recyclerView2);

        Toast.makeText(Chat.this, "1" , Toast.LENGTH_SHORT).show();
        buildRecyclerView();

    }

    //    LAYOUT

    //recyler view + layout
    public void buildRecyclerView(){
        Toast.makeText(Chat.this, "2" , Toast.LENGTH_SHORT).show();
        //initialize adapter, r view and layout
        ride = new ArrayList<>();
        r_view = findViewById(R.id.recyclerView2);
        //xml layout wont change in size
        r_view.setHasFixedSize(true);
        l_manager = new LinearLayoutManager(this);
        //send the ride arraylist to adapter which brings to viewholder
        adapter = new D_Adapter(ride,2);

        r_view.setLayoutManager(l_manager);
        r_view.setAdapter(adapter);

        getRideList();

        View v = findViewById(R.id.view_chats);
        v.setVisibility(View.GONE);
        Toast.makeText(Chat.this, "here " , Toast.LENGTH_SHORT).show();
    }

    public void getRideList(){  //if want to return ridelist, use arraylist and return ride

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    for(DataSnapshot rideshot: dataSnapshot.getChildren()) {
                        String key = rideshot.getKey();
                        //Toast.makeText(D_browseRide.this, "a : "+key, Toast.LENGTH_SHORT).show();
                        if(key.equals("Ride request")) {
                            for(DataSnapshot rideshot1: rideshot.getChildren()) {
                                //get data from snapshot of database
                                d_rideItem = rideshot1.getValue(D_RideItem.class);
                                //Toast.makeText(D_browseRide.this, "key : " + d_rideItem.toString(), Toast.LENGTH_SHORT).show();
                                //add d ride class to ride list
                                ride.add(d_rideItem);
                            }
                        }

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });
    }


}