package com.example.music;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jaudiotagger.tag.FieldKey;

public class Tab3 extends Fragment {

    public String data = "tab3";
    public Music playing;

    private TextView lyricView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);
        lyricView = rootView.findViewById(R.id.lyric);
        return rootView;
    }

    public void updateLyric(){
        if (playing != null)
            lyricView.setText(playing.get(FieldKey.LYRICS));
    }

}