package com.rastersoft.bareplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by raster on 24/04/17.
 */

class Album implements Comparator<Song> {

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
            Collections.sort(this.songs, this);
        }
    }

    private boolean isNumber(char c) {
        if ((c < '0') || (c > '9')) {
            return false;
        }
        return true;
    }

    public int compare(Song song1, Song song2) {
        String name1 = song1.name;
        String name2 = song2.name;

        int nameSize = name1.length();
        if (nameSize > name2.length()) {
            nameSize = name2.length();
        }

        int i1 = 0;
        int i2 = 0;
        while(true) {
            if ((i1 == name1.length()) || (i2 == name2.length())) {
                break;
            }
            char n1 = name1.charAt(i1);
            char n2 = name2.charAt(i2);
            if (!this.isNumber(n1) || !this.isNumber(n2)) {
                if (n1 < n2) {
                    return -1;
                }
                if (n1 > n2) {
                    return 1;
                }
                i1++;
                i2++;
                continue;
            }
            // both are numbers
            int num1 = 0;
            int num2 = 0;
            while(i1 < name1.length()) {
                int c;
                c = (int)name1.charAt(i1);
                if (c<0) {
                    c += 256;
                }
                if ((c < '0') || (c > '9')) {
                    break;
                }
                num1 *= 10;
                num1 += c;
                i1++;
            }
            while(i2 < name2.length()) {
                int c;
                c = (int)name2.charAt(i2);
                if (c<0) {
                    c += 256;
                }
                if ((c < '0') || (c > '9')) {
                    break;
                }
                num2 *= 10;
                num2 += c;
                i2++;
            }
            if (num1 < num2) {
                return -1;
            }
            if (num1 > num2) {
                return 1;
            }
        }
        if (i1 < i2) {
            return -1;
        }
        if (i1 > i2) {
            return 1;
        }
        return 0;
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
