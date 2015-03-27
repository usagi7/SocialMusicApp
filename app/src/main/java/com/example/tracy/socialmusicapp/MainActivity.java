package com.example.tracy.socialmusicapp;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.Menu;


public class MainActivity extends FragmentActivity {

    private FragmentTabHost tabHostManager;
    private static MusicManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_layout);

        mManager = new MusicManager(this);
        tabHostManager = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHostManager.setup(this, getSupportFragmentManager(),R.id.realtabcontent);

        Bundle b = new Bundle();
        b.putString("key", "Songs");
        tabHostManager.addTab(tabHostManager.newTabSpec("songs").setIndicator("Songs"),
                SongsFragment.class, b);

        b = new Bundle();
        b.putString("key", "Artists");
        tabHostManager.addTab(tabHostManager.newTabSpec("artists")
                .setIndicator("Artists"), ArtistsFragment.class, b);

        b = new Bundle();
        b.putString("key", "Albums");
        tabHostManager.addTab(tabHostManager.newTabSpec("albums").setIndicator("Albums"),
                AlbumsFragment.class, b);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static MusicManager getMusicManager()
    {
        return mManager;
    }

}
