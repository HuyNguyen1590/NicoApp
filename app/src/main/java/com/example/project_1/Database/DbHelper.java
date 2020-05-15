package com.example.project_1.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project_1.DAO.AlarmDAO;
import com.example.project_1.DAO.MiniGame01DAO;
import com.example.project_1.MiniGame01.MiniGame01;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbNicoApp";
    public static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AlarmDAO.SQL_ALARM);
        db.execSQL(MiniGame01DAO.SQL_MINIGAME01);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+AlarmDAO.TABLE_NAME);
        db.execSQL("Drop table if exists "+MiniGame01DAO.TABLE_NAME);
    }
}
