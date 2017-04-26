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

import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private AlbumManager albumManager;
    private int mode;

    private int currentAlbum;
    private int currentSong;

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
        this.currentAlbum = -1;
        this.currentSong = 0;
        this.nextSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.player != null) {
            this.player.stop();
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

    public void onPrevClicked(View view) {
        this.prevSong();
    }

    public void onNextClicked(View view) {
        this.nextSong();
    }

    public void nextSong() {

        boolean doneLoop = false;
        while(true) {
            if (this.currentAlbum < 0) {
                this.currentAlbum = 0;
                this.currentSong = 0;
            } else {
                this.currentSong++; // next song
            }
            Album album = this.albumManager.getAlbum(this.currentAlbum);
            if (album == null) {
                if (doneLoop) {
                    return;
                }
                doneLoop = true; // to avoid failure if there are no songs
                this.currentAlbum = -1;
                continue;
            }
            Song song = album.get_song(this.currentSong);
            if (song == null) {
                this.currentAlbum++;
                this.currentSong = -1;
                continue;
            }
            if (this.playSong(song)) {
                return;
            }
        }
    }

    public void prevSong() {

        boolean doneLoop = false;
        while(true) {
            if (this.currentAlbum < 0) {
                this.currentAlbum = this.albumManager.getNAlbumes() - 1;
                this.currentSong = this.albumManager.getAlbum(this.currentAlbum).getNSongs() - 1;
            } else {
                this.currentSong--; // previous song
            }
            Album album = this.albumManager.getAlbum(this.currentAlbum);
            if (album == null) {
                if (doneLoop) {
                    return;
                }
                doneLoop = true; // to avoid failure if there are no songs
                this.currentAlbum = -1;
                continue;
            }
            Song song = album.get_song(this.currentSong);
            if (song == null) {
                this.currentAlbum--;
                this.currentSong = -1;
                continue;
            }
            if (this.playSong(song)) {
                return;
            }
        }
    }

    public boolean playSong(Song song) {

        EditText text = (EditText) findViewById(R.id.currentSong);
        text.setText("%s\n\n%s".format(song.album,song.name));

        String path = song.path;
        this.albumManager.listAlbumes();
        if (this.player != null) {
            this.player.stop();
        }
        this.player = new MediaPlayer();
        this.player.setScreenOnWhilePlaying(true);
        this.player.setOnCompletionListener(this);

        Log.v("Playing: ",path);
        Uri uri = Uri.parse("file://"+path);

        this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            player.setDataSource(getApplicationContext(),uri);
            player.prepare();
        } catch(IOException e) {
            Log.v("Sonido2: ",e.getMessage());
            return false;
        }
        player.start();
        player.setLooping(false); // Set looping
        player.setVolume(100,100);
        this.setButtonStatus();
        return true;
    }

    public void onCompletion(MediaPlayer mp) {
        Log.v("Musica","Completado");
        this.player.stop();
        this.player = null;
        this.nextSong();
    }
}
