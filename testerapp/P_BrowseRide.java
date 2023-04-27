package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class P_BrowseRide extends AppCompatActivity{

    private  ArrayList<P_RideItem> ride,listCopy;
    private P_RideItem p_rideItem;
    private P_RequestRide r_ride;
    //r_viw from xml browseride;
    private RecyclerView r_view;
    //adapter bridges the below data n loads to recycler view
    // private RecyclerView.Adapter adapter;
    private P_Adapter adapter,adapter2;
    //creates presaved list view from library
    private RecyclerView.LayoutManager l_manager;
    public Button requestBtn;
    public RelativeLayout lyt;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_browse_ride);

        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Driver");
        r_view = findViewById(R.id.recyclerView);

        buildRecyclerView();

    }



    //    LAYOUT

    //recycler view + layout
    public void buildRecyclerView(){
        //initialize adapter, r view and layout
        ride = new ArrayList<>();
        r_view = findViewById(R.id.recyclerView);
        //xml layout wont change in size
        r_view.setHasFixedSize(true);
        l_manager = new LinearLayoutManager(this);
        //send the ride arraylist to adapter which brings to viewholder
        adapter = new P_Adapter(ride);

        r_view.setLayoutManager(l_manager);
        r_view.setAdapter(adapter);

        getRideList();

    }


    //    SEARCH BAR

    // for search bar+searchView using menu
    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_searchbar);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return true;
            }
        });
        return true;
    }

    private void search(String str){ //use CharSequence

        listCopy = new ArrayList<>();
        r_view = findViewById(R.id.recyclerView);
        adapter2 = new P_Adapter(listCopy);

        //       Toast.makeText(P_browseRide.this, "BEFORE: "+ride, Toast.LENGTH_SHORT).show();

        //       ride = getRideList();
        //       Toast.makeText(P_browseRide.this, "AFTER: "+ride, Toast.LENGTH_SHORT).show();

        if(str == null|| str.length()==0){
            listCopy.addAll(ride);
        }
        else{
            String pattern = str.toString().toLowerCase().trim();
            listCopy.clear();

            for(P_RideItem item:ride) {
                if(item.getPickup().toLowerCase().contains(pattern)){
                    listCopy.add(item);
                }
                if(item.getDestination().toLowerCase().contains(pattern)){
                    listCopy.add(item);
                }
                if(item.getDate().toLowerCase().contains(pattern)){
                    listCopy.add(item);
                }
            }
        }

        r_view.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

    }

    public void getRideList(){  //if want to return ridelist, use arraylist and return ride

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    for(DataSnapshot rideshot: dataSnapshot.getChildren()) {
                        String key = rideshot.getKey();
                        //  Toast.makeText(P_browseRide.this, "a : "+key, Toast.LENGTH_SHORT).show();
                        if(key.equals("New Ride")) {
                            for(DataSnapshot rideshot1: rideshot.getChildren()) {
                                //get data from snapshot of database
                                p_rideItem = rideshot1.getValue(P_RideItem.class);
                                //add d ride class to ride list
                                ride.add(p_rideItem);
                            }
                        }
                    }
                };

                //lytPressed();
//                itemCount = adapter.getItemCount();
//                Toast.makeText(P_BrowseRide.this, "itemCNT: "+itemCount, Toast.LENGTH_SHORT).show();
//                assignCount(itemCount);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(P_BrowseRide.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void refreshPage(){
        ride = new ArrayList<>();
        adapter = new P_Adapter(ride);
        r_view.setAdapter(adapter);

        getRideList();
    }

    //remove/delete

    //schedule



//
//      //TODO - popup does not go away until fields are filled,add/cancel btn pressed
//    public void insertItem (String pickup,String dest,String dt,String tm,String st){
//        //TODO-read from edittext then put in ride item
//
//        ride.add(new D_RideItem(R.drawable.ic_face1,"NEW",pickup,dest,dt, tm,st));
//        adapter.notifyDataSetChanged();
//    }
//
//    public void createRideList(){
//        //access the rideitem java class & call the rideitem method to input values
//        ride = new ArrayList<>();
//        ride.add(new D_RideItem(R.drawable.ic_face1,"ALEX","KK8","NU SENTRAL","02/10/2022", "2PM","4"));
//        ride.add(new D_RideItem(R.drawable.ic_face1,"ALEX","KK8","NU SENTRAL","02/10/2022", "2PM","4"));
//        ride.add(new D_RideItem(R.drawable.car_icon,"SAM","KK9","NU SENTRAL","10/10/2022", "5PM","2"));
//        ride.add(new D_RideItem(R.drawable.ic_activity,"JAM","KK10","NU SENTRAL","11/10/2022", "2AM","3"));
//        ride.add(new D_RideItem(R.drawable.ic_face1,"ALEX","KK8","NU SENTRAL","02/10/2022", "2PM","4"));
//        ride.add(new D_RideItem(R.drawable.car_icon,"SAM","KK9","NU SENTRAL","10/10/2022", "5PM","2"));
//        ride.add(new D_RideItem(R.drawable.ic_activity,"JAM","KK10","NU SENTRAL","11/10/2022", "2AM","3"));
//        ride.add(new D_RideItem(R.drawable.ic_face1,"ALEX","KK8","NU SENTRAL","02/10/2022", "2PM","4"));
//        ride.add(new D_RideItem(R.drawable.car_icon,"SAM","KK9","NU SENTRAL","10/10/2022", "5PM","2"));
//        ride.add(new D_RideItem(R.drawable.ic_activity,"JAM","KK10","NU SENTRAL","11/10/2022", "2AM","3"));
//        ride.add(new D_RideItem(R.drawable.ic_face1,"ALEX","KK8","NU SENTRAL","02/10/2022", "2PM","4"));
//        ride.add(new D_RideItem(R.drawable.car_icon,"SAM","KK9","NU SENTRAL","10/10/2022", "5PM","2"));
//        ride.add(new D_RideItem(R.drawable.ic_activity,"JAM","KK10","NU SENTRAL","11/10/2022", "2AM","3"));
//
//    }
//    public void buildRecyclerView(){
//        //initialize adapter, r view and layout
//        r_view = findViewById(R.id.recyclerView);
//        //xml layout wont change in size
//        r_view.setHasFixedSize(true);
//        l_manager = new LinearLayoutManager(this);
//        //send the ride arraylist to adapter which brings to viewholder
//        adapter = new D_Adapter(ride);
//
//        r_view.setLayoutManager(l_manager);
//        r_view.setAdapter(adapter);
//    }
}


//popwindow method
//    public void popup(View view){
//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View viewPopUp = layoutInflater.inflate(R.layout.popup_window,null);
//        PopupWindow pw = new PopupWindow(viewPopUp, 900,900,true);
//        pw.showAtLocation(view, Gravity.BOTTOM,0,0);
//
//        //popup not dismissed if touch is out of bounds
//        pw.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getX() < 0 || motionEvent.getX() > 900) return true;
//                if (motionEvent.getY() < 0 || motionEvent.getY() > 900) return true;
//
//                return false;
//            }
//        });