package com.example.testerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        TextView mangoTxt = findViewById(R.id.Mango);
        mangoTxt.setText("MANGO");

        // SHOULD BE WITHIN A METHOD LIKE ONCREATE()
        // ADD EACH NEW ACTIVITY TO MANIFEST FILE
        Button loginBtn1 = (Button)findViewById(R.id.driverBtn);
        loginBtn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,DriverLogin.class));
            }
        });

        Button signUpBtn = (Button)findViewById(R.id.passengerBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,PassengerLogin.class));
            }
        });

        //TODO:drop down menu
        //private val binding get()= binding!!
        // val user = getResources().getStringArray(R.array.userType)



        //Spinner spinner_choose=(Spinner) findViewById(R.id.spinner_choose);
        //ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this , R.array.choose, android.R.layout.simple_spinner_item);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner_choose.setAdapter(adapter);
        //spinner_choose.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

    }

    //   public void GoToLoginPage(View view) {
    //       Intent intent = new Intent(MainActivity.this, LoginPage.class);
    //       startActivity(intent);
    //   }
    //    public void goToAnotherActivity(View view) {
//        Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
//        startActivity(intent);
//    }



}
