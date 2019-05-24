package com.example.music;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jaudiotagger.tag.FieldKey;



public class Tab2 extends Fragment {

    public String data = "tab2";
    public Music playing;

    private ImageView image;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d("VCS", "onAttachFragment: tab2");
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
        image = rootView.findViewById(R.id.songImage);
        return rootView;
    }

    public void updateImage(){
        if (playing != null) {
            Log.d("VCS", "updateImage: " + playing.get(FieldKey.TITLE));
            byte[] imageData = playing.getArtwork();
            if (imageData != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
        }
    }
}
