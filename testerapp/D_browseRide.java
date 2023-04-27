  package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
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

  public class D_browseRide extends AppCompatActivity {

      private FirebaseAuth fAuth;
      private  ArrayList<D_RideItem> ride,listCopy;
      private Object nam;
      private D_RideItem d_rideItem;
      //r_viw from xml browseride;
      private RecyclerView r_view;
      //adapter bridges the below data n loads to recycler view
     // private RecyclerView.Adapter adapter;
      private D_Adapter adapter,adapter2;
      //creates presaved list view from library
      private RecyclerView.LayoutManager l_manager;

      private AlertDialog.Builder dialogBuilder;
      private  AlertDialog dialog;

      private FloatingActionButton floatBtn;
      private EditText pick,des,time,date,seat,name;
      private TextView D_Id;
      private Button addBtn,removeBtn;
      DatabaseReference db;
      String n,nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_browse_ride);

        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Driver");
        fAuth = FirebaseAuth.getInstance();
        floatBtn = findViewById(R.id.floatButton);
        //name = findViewById(R.id.name);
        r_view = findViewById(R.id.recyclerView);

        buildRecyclerView();

        //TODO- remove ride button

        //float plus btn to insert item
        floatBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createDialogBox();
                retName();
            }
        });
    }


        //   DIALOG BOX POPUP
      public void createDialogBox(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View ridePopUpView = getLayoutInflater().inflate(R.layout.popup_window,null);
        pick = (EditText) ridePopUpView.findViewById(R.id.pickup);
        des = (EditText) ridePopUpView.findViewById(R.id.destination);
        time = (EditText) ridePopUpView.findViewById(R.id.Time);
        date = (EditText) ridePopUpView.findViewById(R.id.Date);
        seat = (EditText) ridePopUpView.findViewById(R.id.seatNo);
        addBtn = (Button) ridePopUpView.findViewById(R.id.addbtn);
        D_Id = (TextView) ridePopUpView.findViewById(R.id.D_iD);

        dialogBuilder.setView(ridePopUpView);
        dialog = dialogBuilder.create();
        dialog.show();


        //  INSERT NEW ITEM WHEN 'ADD' BTN CLICKED
          addBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  String pk = pick.getText().toString();
                  String dest = des.getText().toString();
                  String dt = date.getText().toString();
                  String tm = time.getText().toString();
                  String st = seat.getText().toString();

                  String nm = nama;
                  //String id = D_Id;
                  Toast.makeText(D_browseRide.this, "addbtnNAMA: "+nama, Toast.LENGTH_SHORT).show();

                 // String id = db.push().getKey();

                  String user_id =fAuth.getCurrentUser().getUid();
                  D_RideItem rideitem = new D_RideItem(nm,pk,dest,dt,tm,st,user_id);

                  //String ride_id = "Ride Details";
                  String ride_id = db.child(user_id).push().getKey();
                  db.child(user_id).child("New Ride").child(ride_id).setValue(rideitem).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                      refreshPage();
                                     // D_Id.setText(user_id);
                                      Toast.makeText(D_browseRide.this, "New ride added to list", Toast.LENGTH_SHORT).show();
                                      dialog.dismiss();
                                  }
                                  else{
                                      Toast.makeText(D_browseRide.this, "Insert failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                      dialog.dismiss();
                                  }
                              }
                          });
                }
          });
      }

      //    LAYOUT

      //recyler view + layout
      public void buildRecyclerView(){
        //initialize adapter, r view and layout
        ride = new ArrayList<>();
        r_view = findViewById(R.id.recyclerView);
        //xml layout wont change in size
        r_view.setHasFixedSize(true);
        l_manager = new LinearLayoutManager(this);
        //send the ride arraylist to adapter which brings to viewholder
        adapter = new D_Adapter(ride,0);

        r_view.setLayoutManager(l_manager);
        r_view.setAdapter(adapter);

        getRideList();
    }

    //realtime update
    public void refreshPage(){
        ride = new ArrayList<>();
        adapter = new D_Adapter(ride,0);
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
        adapter2 = new D_Adapter(listCopy,0);

 //       Toast.makeText(D_browseRide.this, "BEFORE: "+ride, Toast.LENGTH_SHORT).show();

 //       ride = getRideList();
 //       Toast.makeText(D_browseRide.this, "AFTER: "+ride, Toast.LENGTH_SHORT).show();

        if(str == null|| str.length()==0){
            listCopy.addAll(ride);
        }
        else{
            String pattern = str.toString().toLowerCase().trim();
            listCopy.clear();

            for(D_RideItem item:ride) {
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
                        //Toast.makeText(D_browseRide.this, "a : "+key, Toast.LENGTH_SHORT).show();
                        if(key.equals("New Ride")) {
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
                Toast.makeText(D_browseRide.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    //  ADD NEW RIDE -> RETURN NAME

    public void retName(){
        String user_id =fAuth.getCurrentUser().getUid();

        //asynchronous so x return
        //on data change method executes at the end, after all other method are executed
        db.child(user_id).addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(D_browseRide.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

      public void assignName(String nm){
          nama = nm;
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