package com.example.testerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class P_Adapter extends RecyclerView.Adapter<P_Adapter.ViewHolder> {
    //private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<P_RideItem> mRideList;
    //copy of list for searchbar
    private Button requestBtn;


    //constructor
    // (mRideList,RecyclerViewInterface recyclerViewInterface)
    public P_Adapter(ArrayList<P_RideItem> mRideList) {
        this.context = context;
        this.mRideList = mRideList;
        //this.recyclerViewInterface = recyclerViewInterface;
    }

    //viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // public ImageView image;
        TextView name,pickup, destination, date, time, seat, D_ID, n1,p1,dest1,tm1,dt1,st1, D_ID2;
        public CardView cd;
        public RelativeLayout lyt;
        public Button lyt2;

        //(View itemView, RecyclerViewInterface recyclerViewInterface)
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
//            cd = itemView.findViewById(R.id.cardView);
            lyt = itemView.findViewById(R.id.rideItemlayout);
            lyt2 = itemView.findViewById(R.id.chatUserList);

            n1 = itemView.findViewById(R.id.name1);
            p1 = itemView.findViewById(R.id.pickup1);
            dest1 = itemView.findViewById(R.id.destination1);
            tm1 = itemView.findViewById(R.id.time1);
            dt1 = itemView.findViewById(R.id.date1);
            st1 = itemView.findViewById(R.id.seatNo1);
            D_ID2 = itemView.findViewById(R.id.D_iD2);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (recyclerViewInterface!= null){
//                        int pos = getAdapterPosition();
//
//                        if (pos != RecyclerView.NO_POSITION){
//                            recyclerViewInterface.onItemClick(pos);
//
//                        }
//                    }
//                }
//            });
        }
    }


    //methods for recyclerview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //to inflate layout (giving a look to the rows)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.p_ride_item,parent, false);

        //cardview click method -> brings to new actvity
//        RelativeLayout lyt = (RelativeLayout)v.findViewById(R.id.rideItemlayout);
//        lyt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(parent.getContext(), "Clickable card", Toast.LENGTH_LONG).show();
//                //context.startActivity(new Intent(context,P_RequestRide.class));
////                Intent intent = new Intent(getApplicationContext(),P_RequestRide.class);
////                startActivity(i);
//
//
//              //  i.putExtra("date", date.getText().toString());
//              //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//              //  context.getApplicationContext().startActivity(i);
//
//
////                String n = name.getText().toString();
////                String pk = pickup.getText().toString();
////                String dest = destination.getText().toString();
////                String dt = date.getText().toString();
////                String tm = time.getText().toString();
////                String st = seat.getText().toString();
//                Intent i = new Intent(parent.getContext(), P_RequestRide.class);
//                parent.getContext().startActivity(i);
//
//                //Toast.makeText(parent.getContext(), "hi ",Toast.LENGTH_SHORT).show();
//                //Toast.makeText(parent.getContext(), "name: "+n+pk+dest+dt+tm+st,Toast.LENGTH_SHORT).show();
//
//                //https://stackoverflow.com/questions/4197135/how-to-start-activity-in-adapter
//                //https://stackoverflow.com/questions/12142255/call-activity-method-from-adapter
//                //practical coding
//                //https://www.youtube.com/watch?v=7GPUpvcU1FE
//
//
//
//            }
//        });
//        requestBtn = v.findViewById(R.id.rqBtn);
        return new ViewHolder(v);
        // (v, recyclerViewInterface)

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind txtview with data received
        //assign values to views created based on position
        P_RideItem currentItem = mRideList.get(position);

//        holder.image.setImageResource(currentItem.getImage());
        holder.name.setText(currentItem.getName());
        holder.pickup.setText(currentItem.getPickup());
        holder.destination.setText(currentItem.getDestination());
        holder.date.setText(currentItem.getDate());
        holder.time.setText(currentItem.getTime());
        holder.seat.setText(currentItem.getSeat());
        holder.D_ID.setText(currentItem.getId());

        holder.lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                recyclerViewInterface.onItemClick(currentItem);
//                int pos=holder.getAdapterPosition();
                String n1 = currentItem.getName();
                String p1 = currentItem.getPickup();
                String dest1 = currentItem.getDestination();
                String dt1 = currentItem.getDate();
                String tm1 = currentItem.getTime();
                String st1 = currentItem.getSeat();
                String d_id2 = currentItem.getId();

                Intent intent = new Intent(view.getContext(), P_RequestRide.class);
                Bundle extras = new Bundle();
                extras.putString("n1",n1);
                extras.putString("p1",p1);
                extras.putString("dest1",dest1);
                extras.putString("dt1",dt1);
                extras.putString("tm1",tm1);
                extras.putString("st1",st1);
                extras.putString("d_id2",d_id2);
                intent.putExtras(extras);
//                intent.putExtra("pos",pos);
                view.getContext().startActivity(intent);

            }
        });

//                holder.lyt2.setOnClickListener(new View.OnClickListener() {
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
