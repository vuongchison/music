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
import java.util.HashMap;

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

    private static HashMap<Integer, String> getAllAlbum(ContentResolver content){
        Cursor cursor =  content.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART}, null, null, null);
        HashMap res = new HashMap<Integer, String>();
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int artColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            do {
                int id = cursor.getInt(idColumn);
                String art = cursor.getString(artColumn);
                res.put(id, art);

            } while (cursor.moveToNext());

        }
        return res;
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

        HashMap allAlbumArt = getAllAlbum(contentResolver);

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int Column = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int filenameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int singerColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do {
                String songTitle = cursor.getString(Column);
                String filename = cursor.getString(filenameColumn);
                String singer = cursor.getString(singerColumn);
                int albumId = cursor.getInt(albumIdColumn);
                Music m = new Music(filename, songTitle, singer, (String)allAlbumArt.get(albumId));
                res.add(m);



            } while (cursor.moveToNext());
        }
        return res;
    }

    public String getFilename() {
        return filename;
    }

}
