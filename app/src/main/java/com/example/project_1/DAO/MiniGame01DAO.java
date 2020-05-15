package com.example.project_1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_1.Database.DbHelper;
import com.example.project_1.Model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class MiniGame01DAO {
    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public static final String TABLE_NAME = "MiniGame01";
    public static final String SQL_MINIGAME01 = "CREATE TABLE MiniGame01 (stt int primary key AUTOINCREMENT, score text NOT NULL);";

    public MiniGame01DAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    //insert
    public void insertScore(String score){
        ContentValues values = new ContentValues();
        values.put("score", score);

        db.insert(TABLE_NAME, null, values);
        //delete lowest score if break limit (10)
        if (DatabaseUtils.queryNumEntries(db, TABLE_NAME)>10){
            deleteScore();
        }
    }

    //getAll
    public List<String> getAllScore() {
        List<String> scores = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            scores.add(c.getString(1));
            c.moveToNext();
        }
        c.close();
        return scores;
    }

    //delete lowest score
    public void deleteScore(){
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, "score ASC");
        c.moveToFirst();
        db.delete(TABLE_NAME, "score=?", new String[]{c.getString(1)});
        c.close();
    }
}
