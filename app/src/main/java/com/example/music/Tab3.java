package com.example.music;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Tab3 extends Tab {


    private TextView lyricView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);
        lyricView = rootView.findViewById(R.id.lyric);
        return rootView;
    }

    public void update(){
        Music playing = getPlaying();
        if (playing != null)
            lyricView.setText(playing.getLyric());
    }

}