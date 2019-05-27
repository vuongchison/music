package com.example.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Music {
    private  String filename = null;
    private AudioFile f = null;
    private Tag tag = null;
    private String title;
    private String singer;
    private String albumArt;
//    public Music(Cursor cursor) {
//            this.filename = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//            this.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//            this.singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//            this.albumlId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//            Log.d("VCS", "Music: " + this.albumlArt);
//            read();
//    }

    public Music(String filename, String title, String singer, String albumArt){
        this.filename = filename;
        this.title = title;
        this.singer = singer;
        this.albumArt = albumArt;
        Log.d("VCS", "Music: " + this.albumArt);
        read();
    }

    private void read(){
        try {
            this.f = AudioFileIO.read(new File(this.filename));
            this.tag = this.f.getTag();
        } catch (CannotReadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }

    }

    public String get(FieldKey id){

        if (this.tag != null)
            return this.tag.getFirst(id);
        return "";
    }


    private static String getAlbumArtPath(int albumId, ContentResolver context){
        Cursor cursor =  context.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {String.valueOf(albumId)},
                null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            // do whatever you need to do
        }
        return null;
    }

    public byte[] getArtwork(){
        if (albumArt != null){
            File file = new File(albumArt);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
                return bytes;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        if (tag == null)
            return null;
        Artwork artwork = tag.getFirstArtwork();
        if (artwork == null)
            return null;
        return tag.getFirstArtwork().getBinaryData();
    }

    public String getLyric(){
        return get(FieldKey.LYRICS);
    }

    public String getTitle(){
        return title;
    }



    public String toString(){
        return title;
    }

    public static ArrayList<Music> getAll(Context context){
        ArrayList res = new ArrayList<Music>();

        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor == null) {

            //Toast.makeText(MainActivity.this,"Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            //Toast.makeText(MainActivity.this,"No Music Found on SD Card.", Toast.LENGTH_LONG);

        }
        else {

            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);
//                cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                String songTitle = cursor.getString(Title);
                //Log.d("VCS", "GetAllMediaMp3Files: " + uri + '/' + cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
                //Log.d("VCS", "GetAllMediaMp3Files: " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                String filename = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //Log.d("VCS", "filename:" + filename);
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                    int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    Log.d("VCS", "getAll: " + albumId);
                    Music m = new Music(filename, songTitle, singer, getAlbumArtPath(albumId, contentResolver));
                    res.add(m);



            } while (cursor.moveToNext());
        }
        return res;
    }

    public String getFilename() {
        return filename;
    }

}
