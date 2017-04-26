package com.rastersoft.bareplayer;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by raster on 24/04/17.
 */

class Album {

    public String path;
    private ArrayList<Song> songs;
    private boolean alwaysRandom;

    public Album(String path) {

        this.songs = new ArrayList<Song>();
        this.path = path;
        this.alwaysRandom = false;
        int counter = 0;

        File directory = new File(path);
        File[] files = directory.listFiles();
        for(File file:files) {
            if (file.isDirectory()) {
                continue;
            }
            String filename = file.getName();
            if ((filename == "random") || (filename == "random.txt")) {
                this.alwaysRandom = true;
                continue;
            }
            Song song = new Song(filename,path);
            this.songs.add(song);
            counter++;
        }
    }

    public void sortSongs(int mode) {

        if ((mode == AlbumManager.MODE_RANDOM_SONG) || (this.alwaysRandom)) {
            ArrayList<Song> newSongs = new ArrayList<Song>();
            int nSongs = this.songs.size();
            while (nSongs > 0) {
                int position = (int) (Math.random() * ((double) nSongs));
                Song song = this.songs.get(position);
                newSongs.add(song);
                this.songs.remove(position);
                nSongs--;
            }
            this.songs = newSongs;
        } else {
            Collections.sort(this.songs, new Comparator<Song>() {
                @Override
                public int compare(Song song2, Song song1) {
                    return song1.name.compareTo(song2.name);
                }
            });
        }
    }

    public Song get_song(int index) {
        if (index < 0) {
            return null;
        }
        if (index < this.songs.size()) {
            return this.songs.get(index);
        } else {
            return null;
        }
    }

    public int getNSongs() {
        return this.songs.size();
    }
}
