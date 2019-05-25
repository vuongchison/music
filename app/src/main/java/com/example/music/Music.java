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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Music {
    private  String filename = null;
    private AudioFile f = null;
    private Tag tag = null;
    private String title;


    public Music(String filename, String title) {
            this.filename = filename;
            this.title = title;
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
        if (this.f == null){
            read();
        }
        if (this.tag != null)
            return this.tag.getFirst(id);
        return "";
    }


    public byte[] getArtwork(){
        if (this.f == null){
            read();
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

    public boolean hasTag(){
        if (this.f == null){
            read();
        }
        return this.tag != null;
    }

    public String toString(){
        return this.title;
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
                Music m = new Music(filename, songTitle);
                res.add(m);

            } while (cursor.moveToNext());
        }
        return res;
    }

    public String getFilename() {
        return filename;
    }

}
