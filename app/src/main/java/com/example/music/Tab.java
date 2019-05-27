package com.example.music;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

public abstract class Tab extends Fragment {
    public abstract void update();

    public Music getPlaying(){
        return MainActivity.player.getPlaying();
    }

}
