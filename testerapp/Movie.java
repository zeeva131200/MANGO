package com.example.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.security.keystore.SecureKeyImportUnavailableException;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Movie extends AppCompatActivity {
    DatabaseReference db;
    private PlayerView playerView;
    SimpleExoPlayer exoPlayer;
    String url;
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    tools:context=".Movie"
//    android:background="@color/black">
//
//
//    <WebView
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:id="@+id/vidPlayer"/>
//
//
//</androidx.constraintlayout.widget.ConstraintLayout>

//    <com.google.android.exoplayer2.ui.PlayerView
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:id = "@+id/exoplayer"
//    android:background="@android:color/black"
//    app:resize_mode="fill"
//    android:foregroundGravity="center"/>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie);

        db = FirebaseDatabase.getInstance().getReference();
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.exoplayer);
        playerView.setPlayer(exoPlayer);;

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rideshot : snapshot.getChildren()) {
                        String key = rideshot.getKey();
                        //Toast.makeText(DriverLogin.this, "a : " + key, Toast.LENGTH_SHORT).show();
                        if (key.equals("Movie")) {
                            url = rideshot.getValue().toString();
                            //  Toast.makeText(Movie.this, "uri: "+ url, Toast.LENGTH_SHORT).show();

                            MediaItem mediaItem = MediaItem.fromUri(url);
                            exoPlayer.addMediaItem(mediaItem);
                            exoPlayer.prepare();
                            exoPlayer.setPlayWhenReady(true);

                        } else {
                            //Toast.makeText(DriverLogin.this, "please choose an image to continue", Toast.LENGTH_SHORT).show();
                        }

                    }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Movie.this, "Fail to get data.", Toast.LENGTH_SHORT).show();

            }
        });


//        WebView webView = findViewById(R.id.vidPlayer);
//        webView.loadUrl("https://www.youtube.com/watch?v=WDkg3h8PCVU");


//        exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(ctx);
//        Uri video = Uri.parse(url);



    }
}