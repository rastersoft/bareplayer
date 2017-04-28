package com.rastersoft.bareplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity {

    private String current_path;
    private int current_song;

    private MediaPlayer player;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.verifyStoragePermissions(this);
        this.current_path = "";
        this.current_song = 0;

    }


    public void randomSongClicked(View view) {

        Intent myIntent = new Intent(this, Main2Activity.class);
        myIntent.putExtra("launch_mode", 1); //Optional parameters
        this.startActivity(myIntent);
    }

    public void randomAlbumClicked(View view) {
        Intent myIntent = new Intent(this, Main2Activity.class);
        myIntent.putExtra("launch_mode", 2);
        this.startActivity(myIntent);
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have read permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
}
