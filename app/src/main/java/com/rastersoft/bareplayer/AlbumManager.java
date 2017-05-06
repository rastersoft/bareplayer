package com.rastersoft.bareplayer;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by raster on 25/04/17.
 */

class AlbumManager {

    public static final int MODE_RANDOM_NONE  = 0;
    public static final int MODE_RANDOM_SONG  = 1;
    public static final int MODE_RANDOM_ALBUM = 2;

    private ArrayList<Album> albumes;

    private ArrayList<Album> randomAlbum;
    private ArrayList<Album> randomSongs;
    private int currentMode;
    private int currentAlbum;

    public AlbumManager(int mode) {
        this.randomAlbum = new ArrayList<Album>();
        this.randomSongs = new ArrayList<Album>();
        this.currentAlbum = -1;
        this.currentMode = mode;
        if (this.currentMode == MODE_RANDOM_ALBUM) {
            this.albumes = this.randomAlbum;
        } else {
            this.albumes = this.randomSongs;
        }
    }

    public Album nextAlbum() {
        this.currentAlbum++;
        if (this.currentAlbum >= this.albumes.size()) {
            this.currentAlbum = 0;
        }
        Album album = this.albumes.get(this.currentAlbum);
        album.resetSong(true);
        return album;
      }

    public Album prevAlbum() {
        this.currentAlbum--;
        if (this.currentAlbum < 0) {
            this.currentAlbum = this.albumes.size() - 1;
        }
        Album album = this.albumes.get(this.currentAlbum);
        album.resetSong(false);
        return album;
    }

    public void refreshSongSublist(String base_folder) {

        Album album = new Album(base_folder,this.randomSongs);
        if (0 != album.getNSongs()) {
            this.randomAlbum.add(album);
        }

        File directory = new File(base_folder);
        File[] files = directory.listFiles();
        for(File file:files) {
            if (file.isDirectory()) {
                String filename = file.getName();
                if ((filename.equals("random")) || (filename.equals("random.txt")) || (filename.equals("norandom")) || (filename.equals("norandom.txt"))) {
                    continue;
                }
                this.refreshSongSublist(file.getAbsolutePath());
            }
        }
    }


    public void sortAlbumes() {


        // Random sort for album play
        ArrayList<Album> newAlbumes = new ArrayList<Album>();
        int nAlbumes = this.albumes.size();
        while (nAlbumes > 0) {
            int position = (int) (Math.random() * ((double) nAlbumes));
            Album album = this.albumes.get(position);
            album.sortSongs(this.currentMode);
            newAlbumes.add(album);
            this.albumes.remove(position);
            nAlbumes--;
        }
        this.albumes = newAlbumes;
    }

    public Album getAlbum(int album) {
        if (album < this.albumes.size()) {
            return this.albumes.get(album);
        } else {
            return null;
        }
    }

    public int getNAlbumes() {

        return this.albumes.size();
    }

    public void listAlbumes() {
/*        for(Album album:this.albumes) {
            Log.v("Album",album.path);
            int n = album.getNSongs();
            for(int a=0;a<n;a++) {
                Log.v("Song",album.get_song(a).name);
            }
        }*/
    }
}
