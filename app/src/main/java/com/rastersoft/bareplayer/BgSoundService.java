package com.rastersoft.bareplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.net.URI;


/**
 * Created by raster on 24/04/17.
 */

public class BgSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer player;
    public IBinder onBind(Intent arg0) {
        Log.v("Sonido: ","binded");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String path = intent.getAction();
        Log.v("Sonido: ",path);
        Uri uri = Uri.parse("file://"+path);
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(getApplicationContext(), uri);
            player.prepare();
        } catch (java.io.IOException e) {
            return 1;
        }
        player.start();
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        player.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        Log.v("Sonido: ","onStart");
        // TO DO

    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        Log.v("Sonido: ","binded2");
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        Log.v("Sonido: ","ondestroy");
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}