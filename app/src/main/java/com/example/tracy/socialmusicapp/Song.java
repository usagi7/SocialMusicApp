package com.example.tracy.socialmusicapp;

/**
 * Created by Vina on 2/5/15.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

public class Song {

    private String title;
    private String artist;
    private String path;
    private String displayName;
    private String songDuration;
    private String albumName;
    private String genre;
    private Bitmap albumArt;

    /* Constructor */
    public Song(String title, String artist, String path, String displayName, String songDuration){
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.displayName = displayName;
        this.songDuration = songDuration;
    }

    public Song(String title, String path){
        this.title = title;
        this.path = path;

        MediaMetadataRetriever mR = new MediaMetadataRetriever();
        mR.setDataSource(path);
        byte[] art;
        art = mR.getEmbeddedPicture();

        try {
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            this.albumArt = songImage;
            this.albumName = mR.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            this.artist = mR.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            this.genre = mR.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            this.songDuration = mR.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception e) {
            this.albumName = "Unknown Album";
            this.artist = "Unknown Artist";
            this.genre = "Unknown Genre";
        }
    }

    public String get_path(){
        return path;
    }

    public String get_title(){ return title; }

    public String get_artist(){
        return artist;
    }

    public String get_displayName()
    {
        return displayName;
    }

    public String get_genre() { return genre; }

    public String get_albumName() { return albumName; }

    public String get_SongDuration() { return songDuration; }

    public Bitmap getAlbumArt() { return albumArt; }

}
