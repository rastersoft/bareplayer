package com.rastersoft.bareplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.io.File;

import static android.view.Surface.ROTATION_0;
import static android.view.Surface.ROTATION_180;
import static android.view.Surface.ROTATION_270;
import static android.view.Surface.ROTATION_90;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private SharedPreferences prefs;
    private String musicPath;
    private int currentOrientation;

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
        Configuration newConfig = this.getResources().getConfiguration();
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case ROTATION_0:
                this.currentOrientation = 0;
                break;
            case ROTATION_90:
                this.currentOrientation = 1;
                break;
            case ROTATION_180:
                this.currentOrientation = 2;
                break;
            case ROTATION_270:
                this.currentOrientation = 3;
                break;
            default:
                this.currentOrientation = 0;
                break;
        }
        Log.e("Orientacion",String.format("%1$d; %2$d",this.currentOrientation,newConfig.screenLayout));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public void randomSongClicked(View view) {

        Intent myIntent = new Intent(this, MainPlayerActivity.class);
        myIntent.putExtra("launch_mode", 1); //Optional parameters
        myIntent.putExtra("music_path",this.musicPath);
        myIntent.putExtra("orientation",this.currentOrientation);
        this.startActivity(myIntent);
    }

    public void randomAlbumClicked(View view) {
        Intent myIntent = new Intent(this, MainPlayerActivity.class);
        myIntent.putExtra("launch_mode", 2);
        myIntent.putExtra("music_path",this.musicPath);
        myIntent.putExtra("orientation",this.currentOrientation);
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
