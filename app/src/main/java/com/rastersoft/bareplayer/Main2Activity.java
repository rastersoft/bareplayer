package com.rastersoft.bareplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private AlbumManager albumManager;
    private int mode;

    private Album currentAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        this.mode = intent.getIntExtra("launch_mode",0); //if it's a string you stored.
        this.player = null;
        this.albumManager = new AlbumManager();
        this.albumManager.refreshSongSublist("/sdcard/Music");
        this.albumManager.sortAlbumes(this.mode);
        this.currentAlbum = null;
        this.nextSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.player != null) {
            this.player.stop();
            this.player.release();
            this.player = null;
        }
    }

    private void setButtonStatus() {
        if (this.player != null) {
            Button playpause = (Button) findViewById(R.id.buttonPlay);
            if (this.player.isPlaying()) {
                playpause.setText("Pause");
                playpause.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_pause, 0, 0);
            } else {
                playpause.setText("Play");
                playpause.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play, 0, 0);
            }
        }
    }

    public void onPlayClicked(View view) {

        if (this.player == null) {
            return;
        }
        if (this.player.isPlaying()) {
            this.player.pause();
        } else {
            this.player.start();
        }
        this.setButtonStatus();
    }

    public void onPrevSongClicked(View view) {
        this.prevSong();
    }

    public void onNextSongClicked(View view) {
        this.nextSong();
    }

    public void nextSong() {

        while(true) {
            if (this.currentAlbum == null) {
                this.currentAlbum = this.albumManager.nextAlbum();
            }
            Song song = this.currentAlbum.nextSong();
            if (song == null) {
                this.currentAlbum = this.albumManager.nextAlbum();
                continue;
            }
            if (this.playSong(song)) {
                return;
            }
        }
    }

    public void prevSong() {
        while(true) {
            if (this.currentAlbum == null) {
                this.currentAlbum = this.albumManager.prevAlbum();
            }
            Song song = this.currentAlbum.prevSong();
            if (song == null) {
                this.currentAlbum = this.albumManager.prevAlbum();
                continue;
            }
            if (this.playSong(song)) {
                return;
            }
        }
    }

    public boolean playSong(Song song) {

        TextView text = (TextView) findViewById(R.id.albumName);
        text.setText(song.album);
        text = (TextView) findViewById(R.id.songTitle);
        text.setText(song.name);

        String path = song.path;
        this.albumManager.listAlbumes();
        if (this.player != null) {
            this.player.stop();
            this.player.release();
        }

        this.player = new MediaPlayer();
        this.player.setScreenOnWhilePlaying(true);
        this.player.setOnCompletionListener(this);
        this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Log.v("Playing",path);
        Uri uri = Uri.parse("file://"+path);

        try {
            player.setDataSource(getApplicationContext(),uri);
            player.prepare();
        } catch(IOException e) {
            Log.v("Sonido2: ",e.getMessage());
            return false;
        }
        player.setLooping(false); // Set looping
        player.setVolume(100,100);
        player.start();
        this.setButtonStatus();
        return true;
    }

    public void onCompletion(MediaPlayer mp) {
        Log.v("Musica","Completado");
        this.player.stop();
        this.player.release();
        this.player = null;
        this.nextSong();
    }
}
