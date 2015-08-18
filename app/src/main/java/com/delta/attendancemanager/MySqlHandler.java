package com.delta.attendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MySqlHandler extends SQLiteOpenHelper {
    private static final int VERSION =2;
    public static final  String TOMO="tomorrow";
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
    private static final String t400="t400";


    public MySqlHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TABLENAME+"(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DAY +" TEXT," +
                t830+" TEXT," +
                t920+" TEXT," +
                t1030+" TEXT," +
                t1120+" TEXT," +
                t130+" TEXT," +
                t220+" TEXT," +
                t310+" TEXT," +
                t400+" TEXT" +
                ");";
        db.execSQL(query);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        Log.d("hel","drop");
        onCreate(db);
    }
    public void add_day(String day,String s830,String s920,String s1030,String s1120,String s130,String s220,String s310,String s400){
        if(ispresent(day))
            delete_day(day);
        ContentValues v=new ContentValues();
        v.put(DAY,day);
        v.put(t830,s830);
        v.put(t920,s920);
        v.put(t1030,s1030);
        v.put(t1120,s1120);
        v.put(t130,s130);
        v.put(t220,s220);
        v.put(t310,s310);
        v.put(t400,s400);
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLENAME,null,v);
    }

    private void delete_day(String day) {
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLENAME, DAY + " = ?", new String[] { day });
    }

    public boolean ispresent(String day){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(TABLENAME, new String[] { DAY }, DAY + "=?",
                new String[] { day }, null, null, null, null);
       if(!cursor.moveToFirst())
            return false;
        else
           return true;
    }

    public List<String[]>  get_days(){
        String[] all=new String[9];
        List<String[]> s=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(true, TABLENAME, new String[] {
                        DAY,
                        t830,
                        t920,
                        t1030,
                        t1120,
                        t130,
                        t220,
                        t310,
                t400},
                null,
                null,
                null, null, null , null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(DAY))!=null){
                all[0]=c.getString(c.getColumnIndex(DAY));
                all[1]=c.getString(c.getColumnIndex(t830));
                all[2]=c.getString(c.getColumnIndex(t920));
                all[3]=c.getString(c.getColumnIndex(t1030));
                all[4]=c.getString(c.getColumnIndex(t1120));
                all[5]=c.getString(c.getColumnIndex(t130));
                all[6]=c.getString(c.getColumnIndex(t220));
                all[7]=c.getString(c.getColumnIndex(t310));
                all[8]=c.getString(c.getColumnIndex(t400));
                s.add(all);
            }
            c.moveToNext();

        }
        c.close();
        return s;
    }

    public String[] get_tomo(){
        String[] all=new String[9];
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(true, TABLENAME, new String[] {
                        DAY,
                        t830,
                        t920,
                        t1030,
                        t1120,
                        t130,
                        t220,
                        t310,
                        t400},
                DAY + "=?" ,
                new String[] {TOMO},
                null, null, null , null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(DAY))!=null){
                all[0]=c.getString(c.getColumnIndex(DAY));
                all[1]=c.getString(c.getColumnIndex(t830));
                all[2]=c.getString(c.getColumnIndex(t920));
                all[3]=c.getString(c.getColumnIndex(t1030));
                all[4]=c.getString(c.getColumnIndex(t1120));
                all[5]=c.getString(c.getColumnIndex(t130));
                all[6]=c.getString(c.getColumnIndex(t220));
                all[7]=c.getString(c.getColumnIndex(t310));
                all[8]=c.getString(c.getColumnIndex(t400));
            }
            c.moveToNext();

        }
        c.close();
        return  all;
    }
}
