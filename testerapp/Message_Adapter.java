package com.example.testerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.ViewHolder> {
private Context mcontext;
private List<Chat_Item> mChat;
private String imageUrl;

public static final int MSG_TYPE_LEFT =0;
public static final int MSG_TYPE_RIGHT =1;

FirebaseUser fuser;


    //constructor
    public Message_Adapter(Context mcontext,List <Chat_Item> mChat) {
        this.mcontext = mcontext;
        this.mChat = mChat;
    }

    //viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_msg = itemView.findViewById(R.id.show_msg);
        }
    }


    //methods for recyclerview
    @NonNull
    @Override
    public Message_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new Message_Adapter.ViewHolder(v);
        }
         else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new Message_Adapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Message_Adapter.ViewHolder holder, int position) {

        Chat_Item chat_item = mChat.get(position);

        holder.show_msg.setText(chat_item.getMsg());

//        if  (imageUrl.equals("default")){
//            holder.pr
//        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}

