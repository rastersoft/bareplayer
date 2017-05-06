package com.rastersoft.bareplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class Main2Activity extends AppCompatActivity {

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Window w = this.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent workIntent = getIntent();
        this.mode = workIntent.getIntExtra("launch_mode", 0); //if it's a string you stored.
        this.sendCommand("init");
    }

    @Override
    public void onBackPressed() {

        this.moveTaskToBack(true);
    }

    private void setButtonStatus() {

        /*if (this.player != null) {
            Button playpause = (Button) findViewById(R.id.buttonPlay);
            if (this.player.isPlaying()) {
                playpause.setText(R.string.pause_song);
                playpause.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_pause, 0, 0);
            } else {
                playpause.setText(R.string.play_song);
                playpause.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play, 0, 0);
            }
        }*/
    }

    public void onStopClicked(View view) {

        super.onBackPressed();
    }

    public void onPlayClicked(View view) {

        /*if (this.player == null) {
            return;
        }
        if (this.player.isPlaying()) {
            this.player.pause();
        } else {
            this.player.start();
        }
        this.setButtonStatus();*/
    }

    private void sendCommand(String command) {
        Intent callService = new Intent(this,MediaBarePlayer.class);
        callService.putExtra("command",command);
        if (command == "init") {
            callService.putExtra("launch_mode",this.mode);
        }
        this.startService(callService);
    }

    public void onPrevSongClicked(View view) {
        this.sendCommand("prevSong");
    }

    public void onNextSongClicked(View view) {

        this.sendCommand("nextSong");
    }

    public void onNextAlbumClicked(View view) {
        this.sendCommand("nextAlbum");
    }

    public void onPreviousAlbumClicked(View view) {
        this.sendCommand("prevAlbum");
    }

}
