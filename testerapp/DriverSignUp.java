package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverSignUp extends AppCompatActivity {
    private TextView login;
    private EditText fEmail,fPswd,fName,fPhone;
    private Button signupBtn1;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_sign_up);

        fName = findViewById(R.id.name);
        fEmail = findViewById(R.id.email);
        fPhone = findViewById(R.id.phone);
        fPswd = findViewById(R.id.pswd);
        signupBtn1 = findViewById(R.id.signUpBtn1);
        login = (TextView)findViewById(R.id.loginTxt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();

        signupBtn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = fEmail.getText().toString().trim();
                String pswd =  fPswd.getText().toString().trim();
                String name =  fName.getText().toString().trim();
                String ph =  fPhone.getText().toString().trim();

                //if field is null
                if(TextUtils.isEmpty(email)){
                    fEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(pswd)){
                    fPswd.setError("Password is Required");
                    return;
                }

                if(pswd.length() < 6){
                    fPswd.setError("Password must be more than or equal to 6 characters");
                    return;
                }

                //make progressbar visible
                progressBar.setVisibility(View.VISIBLE);

                //register user in firebase
                fAuth.createUserWithEmailAndPassword(email,pswd).addOnCompleteListener(DriverSignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check if task/registration successful or not
                        if(task.isSuccessful()){
                            //put user id in database for our reference
                            String user_id =fAuth.getCurrentUser().getUid();
                            String key = "Personal Details";
                            String key1 = "New Ride";
                            //String ridekey = "Ride Details";
                            db = FirebaseDatabase.getInstance().getReference();

                            UserData userData = new UserData(name,email,ph);
                            db.child("Users").child("Driver").child(user_id).child(key).setValue(userData);

                            //ride
                            //create new id for each ride created
                            //adding empty slots to fb for ride details
                            //user id will only be listed in database IF rideitem is not null BUT wont affect authentication aka login etc
                            D_RideItem rideitem = new D_RideItem(null,null,null,null,null,null,null);
                            String ride_id = db.child("Users").child("Driver").child(user_id).child(key1).push().getKey();
                            db.child("Users").child("Driver").child(user_id).child(ride_id).setValue(rideitem);
                            //db.setValue(true);

                            Toast.makeText(DriverSignUp.this, "Registration Successful! Driver created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), D_Homescreen.class));

                        }
                        else{
                            Toast.makeText(DriverSignUp.this, "Registration failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(DriverSignUp.this,DriverLogin.class));
            }
        });

    }

    public void createRide(){

    }
}