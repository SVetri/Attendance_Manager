package com.delta.attendancemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySqlHandler extends SQLiteOpenHelper {
    private static final int VERSION =1;
    private static final String DATABASE_NAME="class.db";
    private static final String TABLENAME="timetable";
    private static final String DAY="day";
    private static final String t830="t830";
    private static final String t920="t920";
    private static final String t1030="t1030";
    private static final String t1120="t1120";
    private static final String t130="t130";
    private static final String t220="t220";
    private static final String t310="t310";
    


    public MySqlHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TABLENAME+"(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DAY +" TEXT," +
                t830+" TEXT," +
                TOTALCHAP+" INT," +
                "fav INT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
