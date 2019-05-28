package com.example.music;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



public class Tab2 extends Tab {

    private Music playing;
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

    public void update(){
        Music playing = getPlaying();
        if (playing != null && playing != this.playing) {
            this.playing = playing;
            byte[] imageData = playing.getArtwork();
            if (imageData != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
            else
                image.setImageResource(R.drawable.music_default_icon);
        }
    }
}
