package com.rastersoft.bareplayer;

import java.io.File;

/**
 * Created by raster on 24/04/17.
 */

class Song {

    public String name;
    public String album;
    public String path;

    public Song(String name, String path) {
        this.name = name;
        String[] parts = path.split("/");
        this.album = parts[parts.length - 1];
        this.path = new File(path,name).getAbsolutePath();
    }
}
