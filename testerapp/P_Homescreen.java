package com.example.testerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class P_Homescreen extends AppCompatActivity {

    //5 tabs
    private int selectedTab =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_homescreen);

//        ImageButton viewRide = findViewById(R.id.viewRideBtn);
//
//        viewRide.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                startActivity(new Intent(P_Homescreen.this,P_BrowseRide.class));
//            }
//        });

        final LinearLayout homeLyt = findViewById(R.id.homeLayout);
        final LinearLayout actLyt = findViewById(R.id.activityLayout);
        final LinearLayout otherLyt = findViewById(R.id.othersLayout);
        final LinearLayout inboxLyt = findViewById(R.id.inboxLayout);
        final LinearLayout profLyt = findViewById(R.id.profileLayout);

        final ImageView homeImg = findViewById(R.id.homeImg);
        final ImageView actImg = findViewById(R.id.activityImg);
        final ImageView otherImg = findViewById(R.id.othersImg);
        final ImageView inboxImg = findViewById(R.id.inboxImg);
        final ImageView profImg = findViewById(R.id.profileImg);

        final TextView homeTxt = findViewById(R.id.homeTxt);
        final TextView actTxt = findViewById(R.id.activityTxt);
        final TextView otherTxt = findViewById(R.id.othersTxt);
        final TextView inboxTxt = findViewById(R.id.inboxTxt);
        final TextView profTxt = findViewById(R.id.profileTxt);

        //set activity fragment
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, P_HomeFragment.class,null)
                .commit();

        homeLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if home is selected
                if(selectedTab!=1){

                    //set activity fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, P_HomeFragment.class,null)
                            .commit();

                    //unselect other tabs
                    actTxt.setVisibility(View.GONE);
                    otherTxt.setVisibility(View.GONE);
                    inboxTxt.setVisibility(View.GONE);
                    profTxt.setVisibility(View.GONE);

                    actImg.setImageResource(R.drawable.ic_activity);
                    otherImg.setImageResource(R.drawable.ic_others);
                    inboxImg.setImageResource(R.drawable.ic_inbox);
                    profImg.setImageResource(R.drawable.ic_profile);

                    actLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    otherLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    inboxLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select home tab
                    homeTxt.setVisibility(View.VISIBLE);
                    homeImg.setImageResource(R.drawable.home_selected);
                    homeLyt.setBackgroundResource(R.drawable.round_back_home_100);

                    //create animation
                    ScaleAnimation sA =  new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    sA.setDuration(200);
                    sA.setFillAfter(true);
                    homeLyt.startAnimation(sA);

                    //set 1st tab as selected
                    selectedTab=1;
                }

            }
        });

        actLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if activity is selected
                if(selectedTab!=2){

                    //set activity fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, D_ActivityFragment.class,null)
                            .commit();

                    //unselect other tabs
                    homeTxt.setVisibility(View.GONE);
                    otherTxt.setVisibility(View.GONE);
                    inboxTxt.setVisibility(View.GONE);
                    profTxt.setVisibility(View.GONE);

                    homeImg.setImageResource(R.drawable.ic_home);
                    otherImg.setImageResource(R.drawable.ic_others);
                    inboxImg.setImageResource(R.drawable.ic_inbox);
                    profImg.setImageResource(R.drawable.ic_profile);

                    homeLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    otherLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    inboxLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select activity tab
                    actTxt.setVisibility(View.VISIBLE);
                    actImg.setImageResource(R.drawable.activity_selected);
                    actLyt.setBackgroundResource(R.drawable.round_back_activity_100);

                    //create animation
                    ScaleAnimation sA =  new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    sA.setDuration(200);
                    sA.setFillAfter(true);
                    actLyt.startAnimation(sA);

                    //set tab as selected
                    selectedTab=2;
                }
            }
        });

        otherLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if others is selected
                if(selectedTab!=3){

                    //set activity fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, OthersFragment.class,null)
                            .commit();

                    //unselect other tabs
                    homeTxt.setVisibility(View.GONE);
                    actTxt.setVisibility(View.GONE);
                    inboxTxt.setVisibility(View.GONE);
                    profTxt.setVisibility(View.GONE);

                    homeImg.setImageResource(R.drawable.ic_home);
                    actImg.setImageResource(R.drawable.ic_activity);
                    inboxImg.setImageResource(R.drawable.ic_inbox);
                    profImg.setImageResource(R.drawable.ic_profile);

                    homeLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    actLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    inboxLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select others tab
                    otherTxt.setVisibility(View.VISIBLE);
                    otherImg.setImageResource(R.drawable.others_selected);
                    otherLyt.setBackgroundResource(R.drawable.round_back_others_100);

                    //create animation
                    ScaleAnimation sA =  new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    sA.setDuration(200);
                    sA.setFillAfter(true);
                    otherLyt.startAnimation(sA);

                    //set tab as selected
                    selectedTab=3;
                }
            }
        });

        inboxLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if inbox is selected
                if(selectedTab!=4){

                    //set activity fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, InboxFragment.class,null)
                            .commit();

                    //unselect other tabs
                    homeTxt.setVisibility(View.GONE);
                    actTxt.setVisibility(View.GONE);
                    otherTxt.setVisibility(View.GONE);
                    profTxt.setVisibility(View.GONE);

                    homeImg.setImageResource(R.drawable.ic_home);
                    actImg.setImageResource(R.drawable.ic_activity);
                    otherImg.setImageResource(R.drawable.ic_others);
                    profImg.setImageResource(R.drawable.ic_profile);

                    homeLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    actLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    otherLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select inbox tab
                    inboxTxt.setVisibility(View.VISIBLE);
                    inboxImg.setImageResource(R.drawable.inbox_selected);
                    inboxLyt.setBackgroundResource(R.drawable.round_back_inbox_100);

                    //create animation
                    ScaleAnimation sA =  new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    sA.setDuration(200);
                    sA.setFillAfter(true);
                    inboxLyt.startAnimation(sA);

                    //set tab as selected
                    selectedTab=4;
                }
            }
        });

        profLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if profile is selected
                if(selectedTab!=5){

                    //set activity fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class,null)
                            .commit();

                    //unselect other tabs
                    homeTxt.setVisibility(View.GONE);
                    actTxt.setVisibility(View.GONE);
                    otherTxt.setVisibility(View.GONE);
                    inboxTxt.setVisibility(View.GONE);

                    homeImg.setImageResource(R.drawable.ic_home);
                    actImg.setImageResource(R.drawable.ic_activity);
                    otherImg.setImageResource(R.drawable.ic_others);
                    inboxImg.setImageResource(R.drawable.ic_inbox);

                    homeLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    actLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    otherLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    inboxLyt.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //select profile tab
                    profTxt.setVisibility(View.VISIBLE);
                    profImg.setImageResource(R.drawable.profile_selected);
                    profLyt.setBackgroundResource(R.drawable.round_back_profile_100);

                    //create animation
                    ScaleAnimation sA =  new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    sA.setDuration(200);
                    sA.setFillAfter(true);
                    profLyt.startAnimation(sA);

                    //set tab as selected
                    selectedTab=5;
                }
            }
        });
    }


    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}

// XML LAYOUT CODE FOR d_homescreen.xml
//<Button
//        android:id="@+id/logoutBtn"
//                android:layout_width="wrap_content"
//                android:layout_height="wrap_content"
//                android:layout_marginStart="28dp"
//                android:layout_marginTop="568dp"
//                android:background="@color/aqua"
//                android:foreground="?android:attr/selectableItemBackground"
//                android:onClick="logout"
//                android:text="@string/log_out"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent" />
//
//<ImageButton
//        android:id="@+id/viewRideBtn"
//                android:layout_width="296dp"
//                android:layout_height="221dp"
//                android:layout_marginStart="16dp"
//                android:layout_marginTop="84dp"
//                android:clickable="true"
//                android:contentDescription="@string/desc"
//                android:focusable="true"
//                android:foreground="?android:attr/selectableItemBackground"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:srcCompat="@drawable/car_icon" />
//
//<TextView
//        android:id="@+id/viewRide"
//                android:layout_width="wrap_content"
//                android:layout_height="wrap_content"
//                android:layout_marginStart="28dp"
//                android:layout_marginTop="100dp"
//                android:textStyle="bold"
//                android:textColor="@color/black"
//                app:backgroundTint="@null"
//                android:textSize="10pt"
//                android:text="@string/offer_ride"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent" />