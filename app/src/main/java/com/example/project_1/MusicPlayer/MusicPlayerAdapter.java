package com.example.project_1.MusicPlayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.project_1.R;

public class MusicPlayerAdapter extends SimpleCursorAdapter {

    public MusicPlayerAdapter(Context context, int layout, Cursor c) {
        super(context, layout, c, new String[]{MediaStore.MediaColumns.TITLE,
                        MediaStore.Audio.Artists.ARTIST},
                new int []{R.id.tvDisplayName,R.id.tvAuthor},
                FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvDisplayName = view.findViewById(R.id.tvDisplayName);
        TextView tvAuthor = view.findViewById(R.id.tvAuthor);
        tvDisplayName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));
        tvAuthor.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
        view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.music_list_item,parent,false);
        bindView(view,context,cursor);
        return view;
    }
}
