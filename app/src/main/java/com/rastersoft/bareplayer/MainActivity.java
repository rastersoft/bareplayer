package com.rastersoft.bareplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.verifyStoragePermissions(this);
        String fullPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        File tmp = new File(fullPath);
        try {
            tmp.mkdir(); // Just to ensure that it has been made
        } catch (Exception e) {
        }
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

    public void onHelpClicked(View view) {
        Intent myIntent = new Intent(this, Main3Activity.class);
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
