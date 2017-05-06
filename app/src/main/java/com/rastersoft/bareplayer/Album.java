package com.rastersoft.bareplayer;

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
    private boolean neverRandom;
    private int currentSong;

    public Album(String path,ArrayList<Album> plainSongs) {

        Album tmpPlainSongs = null;

        this.songs = new ArrayList<Song>();
        this.path = path;
        this.alwaysRandom = false;
        this.neverRandom = false;

        if (plainSongs != null) {
            File directory = new File(path);
            File[] files = directory.listFiles();
            for (File file : files) {
                String filename = file.getName();
                if ((filename.equals("random")) || (filename.equals("random.txt"))) {
                    this.alwaysRandom = true;
                    continue;
                }
                if ((filename.equals("norandom")) || (filename.equals("norandom.txt"))) {
                    this.neverRandom = true;
                    tmpPlainSongs = new Album(path, null);
                    plainSongs.add(tmpPlainSongs);
                    continue;
                }
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                String filename = file.getName();
                Song song = new Song(filename, path);
                this.songs.add(song);
                if (this.neverRandom) {
                    tmpPlainSongs.add(song);
                } else {
                    tmpPlainSongs = new Album(path,null);
                    tmpPlainSongs.add(song);
                    plainSongs.add(tmpPlainSongs);
                }
            }
        }
        this.currentSong = -1;
    }

    public void add(Song song) {
        this.songs.add(song);
    }

    public void resetSong(boolean toFirst) {
        if (toFirst) {
            this.currentSong = -1;
        } else {
            this.currentSong = this.songs.size();
        }
    }

    public Song nextSong() {
        this.currentSong++;
        if (this.currentSong >= this.songs.size()) {
            return null;
        }
        return this.songs.get(this.currentSong);
    }

    public Song prevSong() {
        this.currentSong--;
        if (this.currentSong < 0) {
            return null;
        }
        return this.songs.get(this.currentSong);
    }

    public void sortSongs(int mode) {

        boolean doRandom;
        if (mode == AlbumManager.MODE_RANDOM_ALBUM) {
            doRandom = this.alwaysRandom;
        } else {
            doRandom = this.neverRandom ? false : true;
        }
        if (doRandom) {
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
                public int compare(Song song1, Song song2) {
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
