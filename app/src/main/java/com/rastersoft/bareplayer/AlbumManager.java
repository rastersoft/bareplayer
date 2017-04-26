package com.rastersoft.bareplayer;

import android.util.Log;

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

    private int currentAlbum;

    public AlbumManager() {
        this.albumes = new ArrayList<Album>();
        this.currentAlbum = -1;
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
            this.currentAlbum =  this.albumes.size() - 1;
        }
        Album album = this.albumes.get(this.currentAlbum);
        album.resetSong(false);
        return album;
    }

    public void refreshSongSublist(String base_folder) {

        Album album = new Album(base_folder);
        if (0 != album.getNSongs()) {
            this.albumes.add(album);
        }

        File directory = new File(base_folder);
        File[] files = directory.listFiles();
        for(File file:files) {
            if (file.isDirectory()) {
                this.refreshSongSublist(file.getAbsolutePath());
            }
        }
    }


    public void sortAlbumes(int mode) {

        ArrayList<Album> newAlbumes = new ArrayList<Album>();
        int nAlbumes = this.albumes.size();
        while (nAlbumes > 0) {
            int position = (int) (Math.random() * ((double) nAlbumes));
            Album album = this.albumes.get(position);
            album.sortSongs(mode);
            newAlbumes.add(album);
            this.albumes.remove(position);
            nAlbumes--;
        }
        this.albumes = newAlbumes;
        return;
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
        for(Album album:this.albumes) {
            Log.v("Album",album.path);
            int n = album.getNSongs();
            for(int a=0;a<n;a++) {
                Log.v("Song",album.get_song(a).name);
            }
        }
    }
}
