package com.rastersoft.bareplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Notification;

import java.io.IOException;
import java.lang.Runnable;

public class Main2Activity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,Runnable {

    private MediaPlayer player;
    private AlbumManager albumManager;
    private int mode;
    private Album currentAlbum;
    private int duration;
    private Handler mHandler;
    private Song currentSong;
    private NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Window w = this.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent workIntent = getIntent();
        this.mode = workIntent.getIntExtra("launch_mode", 0); //if it's a string you stored.
        this.player = null;
        this.albumManager = new AlbumManager(this.mode);

        String fullPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        this.albumManager.refreshSongSublist(fullPath);
        this.albumManager.sortAlbumes();
        this.currentAlbum = null;
        this.duration = 0;
        this.mHandler = new Handler();
        this.run();
        this.currentSong = null;
        this.notification = null;
        this.nextSong();
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

    @Override
    public void onBackPressed() {

        this.moveTaskToBack(true);
    }

    @Override
    public void onPause() {
        /*this.notification = new NotificationCompat.Builder(this.getApplicationContext());
        this.notification.setSmallIcon(R.drawable.ic_bareplayer_bw);
        this.notification.setContentTitle("BarePlayer");
        this.notification.setContentText(this.currentSong.name);*/
        /*Intent resultIntent = new Intent(this, Main2Activity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Main2Activity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        this.notification.setContentIntent(resultPendingIntent);*/
        /*this.notification.setAutoCancel(true):
        this.notification.addAction(this);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        mNotificationManager.notify(this.getTaskId(),this.notification.build());*/
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setSongData();
    }

    private void setButtonStatus() {

        if (this.player != null) {
            Button playpause = (Button) findViewById(R.id.buttonPlay);
            if (this.player.isPlaying()) {
                playpause.setText(R.string.pause_song);
                playpause.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_pause, 0, 0);
            } else {
                playpause.setText(R.string.play_song);
                playpause.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_play, 0, 0);
            }
        }
    }

    public void onStopClicked(View view) {
        this.player.stop();
        this.player.release();
        this.player = null;
        super.onBackPressed();
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

    public void onNextAlbumClicked(View view) {
        if (this.mode == AlbumManager.MODE_RANDOM_ALBUM) {
            this.currentAlbum = this.albumManager.nextAlbum();
            this.currentAlbum.resetSong(true);
            this.nextSong();
        }
    }

    public void onPreviousAlbumClicked(View view) {
        if (this.mode == AlbumManager.MODE_RANDOM_ALBUM) {
            this.currentAlbum = this.albumManager.prevAlbum();
            this.currentAlbum.resetSong(true);
            this.nextSong();
        }
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

    private void setSongData() {
        if (this.currentSong != null) {
            TextView text = (TextView) findViewById(R.id.albumName);
            text.setText(this.currentSong.album);
            text = (TextView) findViewById(R.id.songTitle);
            text.setText(this.currentSong.name);
        }
    }

    public boolean playSong(Song song) {

        this.currentSong = song;
        this.setSongData();

        String path = song.path;
        this.albumManager.listAlbumes();
        if (this.player != null) {
            this.player.stop();
            this.player.release();
            this.player = null;
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
        this.duration = player.getDuration();
        this.setButtonStatus();
        return true;
    }

    public void onCompletion(MediaPlayer mp) {
        this.player.stop();
        this.player.release();
        this.player = null;
        this.nextSong();
    }

    private String mSeconds(int ms) {
        ms = ms/1000;
        String date = String.format("%1$02d:%2$02d:%3$02d",(int)(ms/3600),(int)((ms/60)%60),(int)(ms%60));
        return date;
    }

    public void run() {
        if (this.player != null) {
            int pos = this.player.getCurrentPosition();
            String currentPos = String.format("%1$s / %2$s", this.mSeconds(pos), this.mSeconds(this.duration));
            EditText textPos = (EditText) findViewById(R.id.Duration);
            textPos.setText(currentPos);
        }
        this.mHandler.postDelayed(this, 1000);
    }
}
