package com.example.tracy.socialmusicapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Tracy on 3/9/2015.
 */
public class SongsFragment extends Fragment {

    private ListView list = null;
    private View v = null;

    public SongsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.layout, null);
        list = (ListView) v.findViewById(R.id.list);
        list.setBackgroundColor(Color.WHITE);

        // Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(getActivity(),
                MainActivity.getMusicManager().getSongsTitles(),
                R.layout.songs_list_item,
                new String[]{"songTitle"},
                new int[]{R.id.songTitle });

        list.setAdapter(adapter);
        ListView lv = list;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Testing, returns the song path of the selected song on the songsScreen
                String temp = ((HashMap<String, String>)list.getAdapter().getItem(position)).get("songPath");

                //MainActivity.getMusicManager().mediaPlayerPlayAll(position);

                Toast.makeText(getActivity(), temp, Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
    //

}