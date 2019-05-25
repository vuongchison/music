package com.example.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class Player {

    public static Music playing;
    public static ArrayList<Music> musicList;
    public static ArrayList<Music> playList;

    private MediaPlayer mediaPlayer;
    Context context;
    private static final String TAG = "NONAME";
    MediaPlayer.OnCompletionListener onCompletionListener;

    public Player(Context context){
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
        musicList = Music.getAll(context);
        playList =(ArrayList<Music>) musicList.clone();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public static Music getPlaying() {
        return playing;
    }

    public static void setPlaying(Music playing) {
        Player.playing = playing;
    }

    public void play(Music music){
        stop();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Uri uri = Uri.fromFile(new File(music.getFilename()));
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playing = music;
            MainActivity.curentTab.update();
        } catch (Exception e){
            Log.e("NONAME", "Bug: " + e.getMessage());
            mediaPlayer.release();
        }

    }

    public void  play(int position){
        Music music = musicList.get(position);
        play(music);
    }

    public void stop(){
//        mediaPlayer.reset();
        Log.e("NONAME", "stop: " + mediaPlayer + mediaPlayer.isPlaying() );

        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            Log.e("NONAME", "stop: " );
            mediaPlayer.stop();
        }

    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void play(){
        mediaPlayer.start();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public  void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        onCompletionListener = listener;
    }

    public void next(){
//        stop();
        int i = playList.indexOf(playing);
        if (i < playList.size() - 1){
            play( playList.get(i + 1));
        }
    }

    public void prev(){
        int i = playList.indexOf(playing);
        if (i > 0){
            play( playList.get(i - 1));
        }
    }



    public static ArrayList<Music> getMusicList() {
        return musicList;
    }
}