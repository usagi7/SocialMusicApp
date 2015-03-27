package com.example.tracy.socialmusicapp;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tracy on 3/9/2015.
 */
public class MusicManager {

    ArrayList<Song> songsList = new ArrayList<Song>();

    // Constructor
    public MusicManager() {

    }

    public MusicManager(Context context){
        buildSongsList(context);

    }

    private void buildSongsList(Context MainActivity){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = MainActivity.getContentResolver().query(uri, projection, selection, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    //get metadata of song
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName = cursor.getString(3).substring(0,cursor.getString(3).length()-4);
                    String songDuration = cursor.getString(4);

                    //create new song object
                    Song new_song = new Song(title, artist, path, displayName, songDuration);
                    Log.e("MusicManager: ",title);
                    songsList.add(new_song);
                    cursor.moveToNext();
                }

            }

        } catch (Exception e) {
            Log.e("MusicManager: ", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Returns a song hashmaps with "songTitle", "songPath" for list view
     * @return allSongTitles
     */
    public  ArrayList<HashMap<String,String>> getSongsTitles()
    {
        ArrayList<HashMap<String,String>> songs = new ArrayList<HashMap<String,String>>();
        for(Song s: songsList) {
            HashMap<String,String> songConverted = new HashMap<>();     //temporary variable to create song hashmap
            songConverted.put("songTitle", s.get_displayName());
            songConverted.put("songPath", s.get_path());
            songs.add(songConverted);
        }

        return songs;
    }

    /**
     * Returns a song hashmaps with "songTitle", "songPath" for list view
     * @return allArtistsNames
     */
    public ArrayList<HashMap<String,String>> getArtistsList()
    {
        ArrayList<HashMap<String,String>> artistsFormattedList = new ArrayList<>();
        ArrayList<String> artists = new ArrayList<String>();

        //get a single copy of each artists name
        for(Song s: songsList)
            if(!artists.contains(s.get_artist()))
                artists.add(s.get_artist());

        //for each single copy of an artist, create a hashmap for it
        for(String artist: artists)
        {
            HashMap<String,String> artistConv = new HashMap<>();
            artistConv.put("artistName",  artist);
            artistConv.put("artistPath", null);
            artistsFormattedList.add(artistConv);
        }
        return artistsFormattedList;
    }

    /**
     * Returns an albums hashmaps with "albumTitle", "albumPath" for list view
     * @return allAlbumNames
     */
    public ArrayList<HashMap<String, String>> getAlbumsList()
    {
        ArrayList<HashMap<String,String>> albums = new ArrayList<HashMap<String,String>>();
        for(Song s: songsList) {
            HashMap<String,String> albumConverted = new HashMap<>();     //temporary variable to create song hashmap
            albumConverted.put("albumTitle", s.get_albumName());
            albumConverted.put("albumPath", s.get_path());
            albums.add(albumConverted);
        }
        return albums;
    }
}

