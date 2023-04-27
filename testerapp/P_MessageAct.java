package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class P_MessageAct extends AppCompatActivity {

    TextView username;
    FirebaseUser fuser;
    DatabaseReference db;
    Intent intent;
    ImageButton btn_send;
    EditText text_send;

    Message_Adapter msgAdapter;
    List<Chat_Item> mChat;
    RecyclerView rv;
    LinearLayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_message_act);

        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Driver");
        rv = findViewById(R.id.chat_recyc_view);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(getApplicationContext());
        lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);

        Bundle extras = getIntent().getExtras();
        String n1 = extras.getString("n1");
        username.setText(n1);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // c
                finish();
            }
        });


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //   for (DataSnapshot rideshot : dataSnapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    readMsg(fuser.getUid(),key);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(P_MessageAct.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            //   for (DataSnapshot rideshot : dataSnapshot.getChildren()) {
                            String key = dataSnapshot.getKey();
                            Toast.makeText(P_MessageAct.this, "a : "+key, Toast.LENGTH_SHORT).show();
                            String msg = text_send.getText().toString();
                            if(!msg.equals("")){
                                sendMsg(fuser.getUid(), key, msg);
                            }
                            else {
                                Toast.makeText(P_MessageAct.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                            }
                            text_send.setText("");
                            //   }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(P_MessageAct.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }


    private void sendMsg(String sender, String receiver, String msg){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg",msg);

        ref.child("Chats").push().setValue(hashMap);
    }

    private void readMsg(String myId, String userId){
        mChat = new ArrayList<>();

        db = FirebaseDatabase.getInstance().getReference("Chats");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat_Item chat_item = snapshot.getValue(Chat_Item.class);
                    if(chat_item.getReceiver().equals(userId) && chat_item.getSender().equals(myId)){
                        mChat.add(chat_item);
                    }
                    msgAdapter = new Message_Adapter(P_MessageAct.this, mChat);
                    rv.setAdapter(msgAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
