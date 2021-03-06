package com.example.tracy.socialmusicapp;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tracy on 3/9/2015.
 */
public class MusicManager {

    public static final int BUFFER_SIZE = 4096;

    private ArrayList<Song> songsList = new ArrayList<Song>();

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

    public void getFile(String urlEndpoint)
    {
       DownloadFile downloadFile = new DownloadFile();
       downloadFile.execute(urlEndpoint);
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {

            try {
                String urlEndpoint = params[0];
                URL url = new URL(urlEndpoint);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    String fileName = "";
                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    String contentType = httpConn.getContentType();
                    int contentLength = httpConn.getContentLength();

                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10,
                                    disposition.length() - 1);
                        }
                    } else {
                        // extracts file name from URL
                        fileName = urlEndpoint.substring(urlEndpoint.lastIndexOf("/") + 1,
                                urlEndpoint.length());
                    }

                    System.out.println("Content-Type = " + contentType);
                    System.out.println("Content-Disposition = " + disposition);
                    System.out.println("Content-Length = " + contentLength);
                    System.out.println("fileName = " + fileName);

                    // opens input stream from the HTTP connection
                    InputStream inputStream = httpConn.getInputStream();
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC);
                    File newSongFile =  new File(path, "/" + "uniqueID");

                    // opens an output stream to save into file
                    FileOutputStream outputStream = new FileOutputStream(newSongFile);

                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.close();
                    inputStream.close();

                    System.out.println("File downloaded");
                } else {
                    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                }
                httpConn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}