package com.example.testerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class D_Adapter extends RecyclerView.Adapter<D_Adapter.ViewHolder> {
    Context context;
    ArrayList<D_RideItem> mRideList;
    int type;
    //copy of list for searchbar



    //constructor
    public D_Adapter(ArrayList<D_RideItem> mRideList, int layoutType) {
    //    this.context = context;
        this.mRideList = mRideList;
        this.type = layoutType;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    //viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // public ImageView image;
        TextView name,pickup, destination, date, time, seat,D_ID, n1,p1,dest1,tm1,dt1,st1, P_ID;
       // public RelativeLayout lyt;
        public Button lyt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            image = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            pickup = itemView.findViewById(R.id.pickup);
            destination = itemView.findViewById(R.id.destination);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            seat = itemView.findViewById(R.id.seatNo);
            D_ID = itemView.findViewById(R.id.D_iD);
            lyt = itemView.findViewById(R.id.chatUserList);

//            n1 = itemView.findViewById(R.id.name1);
//            p1 = itemView.findViewById(R.id.pickup1);
//            dest1 = itemView.findViewById(R.id.destination1);
//            tm1 = itemView.findViewById(R.id.time1);
//            dt1 = itemView.findViewById(R.id.date1);
//            st1 = itemView.findViewById(R.id.seatNo1);
//            P_ID = itemView.findViewById(R.id.P_iD);
        }
    }


    //methods for recyclerview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==1){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_accept_ride,parent, false);
        }
        else if(viewType==2){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat,parent, false);
        }
        else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_ride_item,parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        D_RideItem currentItem = mRideList.get(position);

//        holder.image.setImageResource(currentItem.getImage());
        holder.name.setText(currentItem.getName());
        holder.pickup.setText(currentItem.getPickup());
        holder.destination.setText(currentItem.getDestination());
        holder.date.setText(currentItem.getDate());
        holder.time.setText(currentItem.getTime());
        holder.seat.setText(currentItem.getSeat());
        holder.D_ID.setText(currentItem.getId());

//        holder.lyt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String n1 = currentItem.getName();
//                Intent intent = new Intent(view.getContext(), D_MessageAct.class);
//
//                Bundle extras = new Bundle();
//                extras.putString("n1",n1);
//                intent.putExtras(extras);
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mRideList.size();
    }

}


//
//    public D_Adapter(ArrayList<D_RideItem> ridelist){
//        mRideList = ridelist;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_ride_item,parent, false);
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        D_RideItem currentItem = mRideList.get(position);
//
//        holder.image.setImageResource(currentItem.getImage());
//        holder.name.setText(currentItem.getName());
//        holder.pickup.setText(currentItem.getPickup());
//        holder.destination.setText(currentItem.getDestination());
//        holder.date.setText(currentItem.getDate());
//        holder.time.setText(currentItem.getTime());
//        holder.seat.setText(currentItem.getSeat());
//    }
//
//    //define no of items in the list
//    @Override
//    public int getItemCount() {
//        return mRideList.size();
//    }
