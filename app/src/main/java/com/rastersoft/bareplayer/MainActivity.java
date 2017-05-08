package com.rastersoft.bareplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private SharedPreferences prefs;
    private String musicPath;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menucfg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()) {
            case R.id.help:
                myIntent = new Intent(this, HelpActivity.class);
                myIntent.putExtra("music_path",this.musicPath);
                this.startActivity(myIntent);
                return true;
            case R.id.setmusicpath:
                myIntent = new Intent(this, FileChooserActivity.class);
                myIntent.putExtra("music_path",this.musicPath);
                this.startActivityForResult(myIntent,90);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.verifyStoragePermissions(this);
        this.prefs = this.getPreferences(Context.MODE_PRIVATE);
        this.musicPath = prefs.getString("musicpath",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
    }


    public void randomSongClicked(View view) {

        Intent myIntent = new Intent(this, MainPlayerActivity.class);
        myIntent.putExtra("launch_mode", 1); //Optional parameters
        myIntent.putExtra("music_path",this.musicPath);
        this.startActivity(myIntent);
    }

    public void randomAlbumClicked(View view) {
        Intent myIntent = new Intent(this, MainPlayerActivity.class);
        myIntent.putExtra("launch_mode", 2);
        myIntent.putExtra("music_path",this.musicPath);
        this.startActivity(myIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 90:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    this.musicPath = res.getString("newPath");
                    SharedPreferences.Editor editor = this.prefs.edit();
                    editor.putString("musicpath",this.musicPath);
                    editor.commit();
                }
                break;
        }
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
