package com.rastersoft.bareplayer;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by raster on 6/05/17.
 */

public class MediaBarePlayer extends IntentService implements MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private AlbumManager albumManager;
    private int mode;

    private Album currentAlbum;

    public MediaBarePlayer(String name) {
        super(name);
    }

    public MediaBarePlayer() {
        super("MediaBarePlayer");
    }

    @Override
    public void onHandleIntent(Intent workIntent) {
        String dataString = workIntent.getDataString();

        switch (workIntent.getStringExtra("command")) {
            case "init":
                this.mode = workIntent.getIntExtra("launch_mode", 0); //if it's a string you stored.

                this.player = null;
                this.albumManager = new AlbumManager(this.mode);

                String fullPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
                this.albumManager.refreshSongSublist(fullPath);
                this.albumManager.sortAlbumes();
                this.currentAlbum = null;
                this.nextSong();
                break;
            case "nextSong":
                this.nextSong();
                break;
            case "prevSong":
                this.prevSong();
                break;
            case "nextAlbum":
                this.nextAlbum();
                break;
            case "prevAlbum":
                this.previousAlbum();
                break;

        }
    }

    @Override
    public void onDestroy() {
        if (this.player != null) {
            this.player.stop();
            this.player.release();
            this.player = null;
        }
        super.onDestroy();
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

    public void nextAlbum() {
        this.currentAlbum = this.albumManager.nextAlbum();
        this.currentAlbum.resetSong(true);
        this.nextSong();
    }

    public void previousAlbum() {
        this.currentAlbum = this.albumManager.prevAlbum();
        this.currentAlbum.resetSong(true);
        this.nextSong();
    }


    public boolean playSong(Song song) {

        /*TextView text = (TextView) findViewById(R.id.albumName);
        text.setText(song.album);
        text = (TextView) findViewById(R.id.songTitle);
        text.setText(song.name);
        this.setButtonStatus();*/

        String path = song.path;
        this.albumManager.listAlbumes();
        if (this.player != null) {
            this.player.stop();
            this.player.release();
        }

        this.player = new MediaPlayer();
        this.player.setOnCompletionListener(this);
        this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Uri uri = Uri.parse("file://"+path);

        try {
            player.setDataSource(getApplicationContext(),uri);
            player.prepare();
        } catch(IOException e) {
            return false;
        }
        player.setLooping(false); // Set looping
        player.setVolume(100,100);
        player.start();
        return true;
    }

    public void onCompletion(MediaPlayer mp) {
        this.player.stop();
        this.player.release();
        this.player = null;
        this.nextSong();
    }

}
