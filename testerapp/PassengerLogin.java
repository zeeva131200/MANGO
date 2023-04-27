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

public class PassengerLogin extends AppCompatActivity {
    private TextView signup;
    private EditText fEmail,fPswd;
    private Button loginBtn;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_login);

        fEmail = (EditText) findViewById(R.id.email);
        fPswd = (EditText) findViewById(R.id.pswd);
        loginBtn = (Button) findViewById(R.id.loginBtn2);
        signup = (TextView)findViewById(R.id.signUpTxt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //get current state of logging status
        fAuth = FirebaseAuth.getInstance();

        //if user nvr logout, send them str8 to homescreen
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(PassengerLogin.this, P_Homescreen.class));
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = fEmail.getText().toString().trim();
                String pswd =  fPswd.getText().toString().trim();

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

                //authenticate user from firebase
                fAuth.signInWithEmailAndPassword(email,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PassengerLogin.this, "Log in Successful! WELCOME!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), P_Homescreen.class));
                        }
                        else{
                            Toast.makeText(PassengerLogin.this, "Log in failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(PassengerLogin.this,PassengerSignUp.class));
            }
        });
    }
}