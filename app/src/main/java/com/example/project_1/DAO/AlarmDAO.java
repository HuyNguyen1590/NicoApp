package com.example.project_1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.project_1.Database.DbHelper;
import com.example.project_1.Model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmDAO {
    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public static final String TABLE_NAME = "Alarm";
    public static final String SQL_ALARM = "CREATE TABLE Alarm (stt integer primary key, hour text NOT NULL, min text NOT NULL, repeat text NOT NULL, status text NOT NULL);";

    public AlarmDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    //insert
    public void insertAlarm(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put("stt", alarm.getStt());
        values.put("hour", alarm.getHour());
        values.put("min", alarm.getMin());
        values.put("repeat", alarm.getRepeat());
        values.put("status", alarm.getStatus());

        db.insert(TABLE_NAME, null, values);
    }

    //getAll
    public List<Alarm> getAllAlarm() {
        List<Alarm> alarms = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            Alarm alarm = new Alarm();
            alarm.setStt(c.getInt(0));
            alarm.setHour(c.getString(1));
            alarm.setMin(c.getString(2));
            alarm.setRepeat(c.getString(3));
            alarm.setStatus(c.getString(4));
            alarms.add(alarm);
            c.moveToNext();
        }
        c.close();
        return alarms;
    }

    //update
    public void updateAlarm(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put("stt", alarm.getStt());
        values.put("hour", alarm.getHour());
        values.put("min", alarm.getMin());
        values.put("repeat", alarm.getRepeat());
        values.put("status", alarm.getStatus());

        db.update(TABLE_NAME, values, "stt=?", new String[]{alarm.getStt()+""});
    }

    //delete
    public void deleteAlarm(Alarm alarm){
        db.delete(TABLE_NAME, "stt=?", new String[]{alarm.getStt()+""});
        updateStt();
    }

    //update STT
    public void updateStt(){
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        int stt = 0;
        while (c.isAfterLast() == false) {
            ContentValues values = new ContentValues();
            values.put("stt",stt+1);
            db.update(TABLE_NAME, values, "stt>"+stt, null);
            stt+=1;
        }
        c.close();
    }
}
