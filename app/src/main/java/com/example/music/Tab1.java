package com.example.music;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class Tab1 extends Fragment {
    public String data = "tab1";
    public Music playing;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        Log.d("VCS", "setArguments: ");
    }

    ListView mListView;
    private ArrayList mMusicList;
    ArrayAdapter mAdapter;

    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        context = getActivity().getApplicationContext();

        mListView = (ListView) view.findViewById(R.id.musicList);

        mMusicList = Music.getAll(context);
        mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mMusicList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music m = (Music) mMusicList.get(position);
//                Log.d("VCS", "onItemClick: " + m.get(FieldKey.LYRICS));
                playing = m;
                ((ViewPager) getActivity().findViewById(R.id.pager)).setCurrentItem(1);
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(m.getFilename());
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
            }
        });
        mListView.setAdapter(mAdapter);
        mAdapter.addAll(mMusicList);
        return view;
    }


}
