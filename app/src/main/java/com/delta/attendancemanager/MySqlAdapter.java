package com.delta.attendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakshmanaram on 14/9/15.
 */
public class MySqlAdapter {
    Context context;
    Mysqlhelper mysqlhelper;
    private String [] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    /**
     * Provides a string arraylist for the textview text
     */
    private static ArrayList<String> subs = new ArrayList<>();

    /**
     * Construct a MySql Adapter
     * @param context specify in which view construct
     * @param factory specify in which shape construct
     */
    public MySqlAdapter(Context context,SQLiteDatabase.CursorFactory factory)
    {
        this.context = context;
        mysqlhelper = new Mysqlhelper(context,factory);

    }

    /**
     * Retrive subs from the SQLite server
     */
    public void get_ub()
    {
        Cursor c = null;
        try {
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c=db.query(true, Mysqlhelper.TNAME, new String[] {
                            Mysqlhelper.CNAME},
                    null,
                    null,
                    null, null, null , null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.CNAME))!=null || !c.getString(c.getColumnIndex(Mysqlhelper.CNAME)).isEmpty() || c.getString(c.getColumnIndex(Mysqlhelper.CNAME)) != Constants.BLANK_STRING){
                    subs.add(c.getString(c.getColumnIndex(Mysqlhelper.CNAME)));
                }
                c.moveToNext();
            }
        } finally {
            c.close();
        }


    }

    /**
     * Add a sub for the textview
     * @param sub to insert
     */
    public void add_sub(String sub)
    {
        if(subs.isEmpty())
            get_ub();
        if(subs.indexOf(sub)>=0)
            return;
        else{
            subs.add(sub);
            ContentValues v=new ContentValues();
            v.put(Mysqlhelper.CNAME,sub);
            SQLiteDatabase db=mysqlhelper.getWritableDatabase();
            db.insert(Mysqlhelper.TNAME,null,v);
        }
    }

    /**
     * Represents an arraylist with subs
     * @return arraylist with subs
     */
    public ArrayList<String> getSubs()
    {
        if(subs.isEmpty())
            get_ub();
        return subs;
    }

    /**
     * Delete a sub from the list
     * @param sub to delete
     */
    public void delete_sub(String sub){
        SQLiteDatabase db = mysqlhelper.getWritableDatabase();
        subs.remove(sub);
        db.delete(Mysqlhelper.TNAME, Mysqlhelper.CNAME + " =? ", new String[]{sub});
    }

    /**
     * Check if a day is present in a time table
     * @param day to check
     * @return true if found, else otherwise
     */
    public boolean ispresent(String day){
        Cursor cursor = null;
        try{
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            cursor = db.query(Mysqlhelper.TABLENAME, new String[]{Mysqlhelper.DAY}, Mysqlhelper.DAY + "=?",
                    new String[]{day}, null, null, null, null);
            if(!cursor.moveToFirst())
                return false;
            else
                return true;
        } finally {
            cursor.close();
        }

    }

    /**
     * Delete a day from the timetable
     * @param day to delete
     */
    public void delete_day(String day) {
        SQLiteDatabase db=mysqlhelper.getWritableDatabase();
        db.delete(Mysqlhelper.TABLENAME, Mysqlhelper.DAY + " = ?", new String[]{day});
    }

    /**
     * Retrive the list of attendance days
     * @return list of days
     */
    public ArrayList<String[]> get_days(){
        ArrayList<String[]> s=new ArrayList<>();
        Cursor c = null;
        try{
            String[] all=new String[9];

            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
                            Mysqlhelper.DAY,
                            Mysqlhelper.t830,
                            Mysqlhelper.t920,
                            Mysqlhelper.t1030,
                            Mysqlhelper.t1120,
                            Mysqlhelper.t130,
                            Mysqlhelper.t220,
                            Mysqlhelper.t310,
                            Mysqlhelper.t400},
                    null,
                    null,
                    null, null, null , null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.DAY))!=null){
                    all[0]=c.getString(c.getColumnIndex(Mysqlhelper.DAY));
                    all[1]=c.getString(c.getColumnIndex(Mysqlhelper.t830));
                    all[2]=c.getString(c.getColumnIndex(Mysqlhelper.t920));
                    all[3]=c.getString(c.getColumnIndex(Mysqlhelper.t1030));
                    all[4]=c.getString(c.getColumnIndex(Mysqlhelper.t1120));
                    all[5]=c.getString(c.getColumnIndex(Mysqlhelper.t130));
                    all[6]=c.getString(c.getColumnIndex(Mysqlhelper.t220));
                    all[7]=c.getString(c.getColumnIndex(Mysqlhelper.t310));
                    all[8]=c.getString(c.getColumnIndex(Mysqlhelper.t400));
                    s.add(all);
                }
                c.moveToNext();

            }
        } finally {
            c.close();
            return s;
        }

    }

    /**
     * Retrieve the list of subjects for mondays
     * @return list of subject
     */
    public String[]  get_mon(){
        String[] all=new String[9];
        Cursor c = null;
        try{
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c=db.query(true, Mysqlhelper.TABLENAME, new String[]{
                            Mysqlhelper.DAY,
                            Mysqlhelper.t830,
                            Mysqlhelper.t920,
                            Mysqlhelper.t1030,
                            Mysqlhelper.t1120,
                            Mysqlhelper.t130,
                            Mysqlhelper.t220,
                            Mysqlhelper.t310,
                            Mysqlhelper.t400},
                    null,
                    null,
                    null, null, null, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals(days[0])){
                    all[0]=c.getString(c.getColumnIndex(Mysqlhelper.DAY));
                    all[1]=c.getString(c.getColumnIndex(Mysqlhelper.t830));
                    all[2]=c.getString(c.getColumnIndex(Mysqlhelper.t920));
                    all[3]=c.getString(c.getColumnIndex(Mysqlhelper.t1030));
                    all[4]=c.getString(c.getColumnIndex(Mysqlhelper.t1120));
                    all[5]=c.getString(c.getColumnIndex(Mysqlhelper.t130));
                    all[6]=c.getString(c.getColumnIndex(Mysqlhelper.t220));
                    all[7]=c.getString(c.getColumnIndex(Mysqlhelper.t310));
                    all[8]=c.getString(c.getColumnIndex(Mysqlhelper.t400));

                }
                c.moveToNext();

            }
        }finally {
            c.close();
            return all;
        }
    }

    /**
     * Retrieve the list of subjects for tuesday
     * @return list of subject
     */
    public String[]  get_tue(){
        String[] all=new String[9];
        Cursor c = null;
        try{
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c=db.query(true, Mysqlhelper.TABLENAME, new String[]{
                            Mysqlhelper.DAY,
                            Mysqlhelper.t830,
                            Mysqlhelper.t920,
                            Mysqlhelper.t1030,
                            Mysqlhelper.t1120,
                            Mysqlhelper.t130,
                            Mysqlhelper.t220,
                            Mysqlhelper.t310,
                            Mysqlhelper.t400},
                    null,
                    null,
                    null, null, null, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals(days[1])){
                    all[0]=c.getString(c.getColumnIndex(Mysqlhelper.DAY));
                    all[1]=c.getString(c.getColumnIndex(Mysqlhelper.t830));
                    all[2]=c.getString(c.getColumnIndex(Mysqlhelper.t920));
                    all[3]=c.getString(c.getColumnIndex(Mysqlhelper.t1030));
                    all[4]=c.getString(c.getColumnIndex(Mysqlhelper.t1120));
                    all[5]=c.getString(c.getColumnIndex(Mysqlhelper.t130));
                    all[6]=c.getString(c.getColumnIndex(Mysqlhelper.t220));
                    all[7]=c.getString(c.getColumnIndex(Mysqlhelper.t310));
                    all[8]=c.getString(c.getColumnIndex(Mysqlhelper.t400));
                }
                c.moveToNext();
            }
        } finally {
            c.close();
            return all;
        }

    }

    /**
     * Retrieve the list of subjects for wednesday
     * @return list of subject
     */
    public String[]  get_wed(){
        String[] all=new String[9];
        Cursor c = null;
        try{
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c = db.query(true, Mysqlhelper.TABLENAME, new String[]{
                            Mysqlhelper.DAY,
                            Mysqlhelper.t830,
                            Mysqlhelper.t920,
                            Mysqlhelper.t1030,
                            Mysqlhelper.t1120,
                            Mysqlhelper.t130,
                            Mysqlhelper.t220,
                            Mysqlhelper.t310,
                            Mysqlhelper.t400},
                    null,
                    null,
                    null, null, null, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals(days[2])){
                    all[0]=c.getString(c.getColumnIndex(Mysqlhelper.DAY));
                    all[1]=c.getString(c.getColumnIndex(Mysqlhelper.t830));
                    all[2]=c.getString(c.getColumnIndex(Mysqlhelper.t920));
                    all[3]=c.getString(c.getColumnIndex(Mysqlhelper.t1030));
                    all[4]=c.getString(c.getColumnIndex(Mysqlhelper.t1120));
                    all[5]=c.getString(c.getColumnIndex(Mysqlhelper.t130));
                    all[6]=c.getString(c.getColumnIndex(Mysqlhelper.t220));
                    all[7]=c.getString(c.getColumnIndex(Mysqlhelper.t310));
                    all[8]=c.getString(c.getColumnIndex(Mysqlhelper.t400));
                }
                c.moveToNext();
            }
        } finally {
            c.close();
            return all;
        }
    }

    /**
     * Retrieve the list of subjects for thursday
     * @return list of subject
     */
    public String[]  get_thur(){
        String[] all=new String[9];
        Cursor c = null;
        try{
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
                            Mysqlhelper.DAY,
                            Mysqlhelper.t830,
                            Mysqlhelper.t920,
                            Mysqlhelper.t1030,
                            Mysqlhelper.t1120,
                            Mysqlhelper.t130,
                            Mysqlhelper.t220,
                            Mysqlhelper.t310,
                            Mysqlhelper.t400},
                    null,
                    null,
                    null, null, null , null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals(days[3])){
                    all[0]=c.getString(c.getColumnIndex(Mysqlhelper.DAY));
                    all[1]=c.getString(c.getColumnIndex(Mysqlhelper.t830));
                    all[2]=c.getString(c.getColumnIndex(Mysqlhelper.t920));
                    all[3]=c.getString(c.getColumnIndex(Mysqlhelper.t1030));
                    all[4]=c.getString(c.getColumnIndex(Mysqlhelper.t1120));
                    all[5]=c.getString(c.getColumnIndex(Mysqlhelper.t130));
                    all[6]=c.getString(c.getColumnIndex(Mysqlhelper.t220));
                    all[7]=c.getString(c.getColumnIndex(Mysqlhelper.t310));
                    all[8]=c.getString(c.getColumnIndex(Mysqlhelper.t400));
                }
                c.moveToNext();
            }
        } finally {
            c.close();
            return all;
        }

    }

    /**
     * Retrieve the list of subjects for friday
     * @return list of subject
     */
    public String[]  get_fri(){
        String[] all=new String[9];
        Cursor c = null;
        try{
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
                            Mysqlhelper.DAY,
                            Mysqlhelper.t830,
                            Mysqlhelper.t920,
                            Mysqlhelper.t1030,
                            Mysqlhelper.t1120,
                            Mysqlhelper.t130,
                            Mysqlhelper.t220,
                            Mysqlhelper.t310,
                            Mysqlhelper.t400},
                    null,
                    null,
                    null, null, null , null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals(days[4])){
                    all[0]=c.getString(c.getColumnIndex(Mysqlhelper.DAY));
                    all[1]=c.getString(c.getColumnIndex(Mysqlhelper.t830));
                    all[2]=c.getString(c.getColumnIndex(Mysqlhelper.t920));
                    all[3]=c.getString(c.getColumnIndex(Mysqlhelper.t1030));
                    all[4]=c.getString(c.getColumnIndex(Mysqlhelper.t1120));
                    all[5]=c.getString(c.getColumnIndex(Mysqlhelper.t130));
                    all[6]=c.getString(c.getColumnIndex(Mysqlhelper.t220));
                    all[7]=c.getString(c.getColumnIndex(Mysqlhelper.t310));
                    all[8]=c.getString(c.getColumnIndex(Mysqlhelper.t400));
                }
                c.moveToNext();
            }
        } finally {
            c.close();
            return all;
        }
    }

    /**
     * Retrieve the list of subjects for the next day
     * @return list of subject
     */
    public String[] get_tomo(){
        String[] all=new String[9];
        Cursor c = null;
        try {
            SQLiteDatabase db=mysqlhelper.getReadableDatabase();
            c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
                            Mysqlhelper.DAY,
                            Mysqlhelper.t830,
                            Mysqlhelper.t920,
                            Mysqlhelper.t1030,
                            Mysqlhelper.t1120,
                            Mysqlhelper.t130,
                            Mysqlhelper.t220,
                            Mysqlhelper.t310,
                            Mysqlhelper.t400},
                    Mysqlhelper.DAY + "=?" ,
                    new String[] {Mysqlhelper.TOMO},
                    null, null, null , null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(Mysqlhelper.DAY))!=null){
                    all[0]=c.getString(c.getColumnIndex(Mysqlhelper.DAY));
                    all[1]=c.getString(c.getColumnIndex(Mysqlhelper.t830));
                    all[2]=c.getString(c.getColumnIndex(Mysqlhelper.t920));
                    all[3]=c.getString(c.getColumnIndex(Mysqlhelper.t1030));
                    all[4]=c.getString(c.getColumnIndex(Mysqlhelper.t1120));
                    all[5]=c.getString(c.getColumnIndex(Mysqlhelper.t130));
                    all[6]=c.getString(c.getColumnIndex(Mysqlhelper.t220));
                    all[7]=c.getString(c.getColumnIndex(Mysqlhelper.t310));
                    all[8]=c.getString(c.getColumnIndex(Mysqlhelper.t400));
                }
                c.moveToNext();
            }
        } finally {
            c.close();
            return  all;
        }
    }

    /**
     * Add a day into the time table
     * @param day to insert
     * @param s830 subject at 8.30
     * @param s920 subject at 9.20
     * @param s1030 subject at 10.30
     * @param s1120 subject at 11.20
     * @param s130 subject at 13.00
     * @param s220 subject at 2.20
     * @param s310 subject at 3.10
     * @param s400 subject at 4.00
     */
    public void add_day(String day, String s830, String s920, String s1030, String s1120, String s130, String s220, String s310, String s400){
        try{
            delete_day(day);
        }catch(Exception e){
            Log.d("Attendance Manager","MySqlAdapter error");
        }
        if(ispresent(day))
            delete_day(day);
        ContentValues v=new ContentValues();
        v.put(Mysqlhelper.DAY,day);
        v.put(Mysqlhelper.t830,s830);
        v.put(Mysqlhelper.t920,s920);
        v.put(Mysqlhelper.t1030,s1030);
        v.put(Mysqlhelper.t1120,s1120);
        v.put(Mysqlhelper.t130,s130);
        v.put(Mysqlhelper.t220,s220);
        v.put(Mysqlhelper.t310,s310);
        v.put(Mysqlhelper.t400, s400);
        SQLiteDatabase db=mysqlhelper.getWritableDatabase();
        db.insert(Mysqlhelper.TABLENAME, null, v);
    }

    /**
     * Add a msg from the chat
     * @param msg message
     * @param time time when has been sent
     * @param date date when has been sent
     */
    public void addmsg(String msg, String time, String date){
        ContentValues v=new ContentValues();
        v.put(Mysqlhelper.ACNAME,msg);
        v.put(Mysqlhelper.ATIMES,time);
        v.put(Mysqlhelper.ADATES, date);
        SQLiteDatabase db=mysqlhelper.getWritableDatabase();
        db.insert(Mysqlhelper.ATNAME, null, v);
    }

    /**
     * Get the list of messages from the chat
     * @return messages from the chat
     */
    public Chat[] getmsgs(){
        List<Chat> msg=new ArrayList<>();
        Chat chat;

        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.ATNAME, new String[] {
                        Mysqlhelper.ACNAME,Mysqlhelper.ATIMES,Mysqlhelper.ADATES},
                null,
                null,
                null, null, "_id DESC" ,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            chat=new Chat();
            if(c.getString(c.getColumnIndex(Mysqlhelper.ACNAME))!=null){
                chat.setMsg(c.getString(c.getColumnIndex(Mysqlhelper.ACNAME)));
            }
            if(c.getString(c.getColumnIndex(Mysqlhelper.ATIMES))!=null){
                chat.setTime(c.getString(c.getColumnIndex(Mysqlhelper.ATIMES)));
            }
            if(c.getString(c.getColumnIndex(Mysqlhelper.ADATES))!=null){
                chat.setDate(c.getString(c.getColumnIndex(Mysqlhelper.ADATES)));
            }
            c.moveToNext();
            msg.add(chat);
        }
        c.close();

        Chat[] msgs=new Chat[msg.size()];
        msgs=msg.toArray(msgs);

        return msgs;
    }

    /**
     * update the subjects for the next day
     * @param subjects subjects
     */
    public void update_tomo(String[] subjects) {
        delete_day(Mysqlhelper.TOMO);
        add_day(Mysqlhelper.TOMO, subjects[1], subjects[2], subjects[3], subjects[4], subjects[5], subjects[6], subjects[7], subjects[8]);
    }

    /**
     * Support class for the handling of the SQLite database
     */
    protected static class Mysqlhelper extends SQLiteOpenHelper{
        /**
         * Indicate version
         */
        private static final int VERSION =6;
        /**
         * Tomorrow String
         */
        public static final  String TOMO="tomorrow";
        /**
         * Database Name
         */
        private static final String DATABASE_NAME="class.db";
        /**
         * Table name
         */
        private static final String TABLENAME="timetable";
        /**
         * Day
         */
        private static final String DAY="day";
        /**
         * 8,30 hour
         */
        private static final String t830="t830";
        /**
         * 9,20 hour
         */
        private static final String t920="t920";
        /**
         * 10,30 hour
         */
        private static final String t1030="t1030";
        /**
         * 11,20 hour
         */
        private static final String t1120="t1120";
        /**
         * 1,30 Hour
         */
        private static final String t130="t130";
        /**
         * 2,20 hour
         */
        private static final String t220="t220";
        /**
         * 3,10 hour
         */
        private static final String t310="t310";
        /**
         * 4,00 hour
         */
        private static final String t400="t400";
        /**
         * Subjects
         */
        private static final String TNAME = "subjects";
        /**
         * subject
         */
        private static final String CNAME = "subject";
        /**
         * Announcements
         */
        private static final String ATNAME="Announcements";
        /**
         * Announcements
         */
        private static final String ACNAME="announcements";
        /**
         * Time
         */
        private static final String ATIMES="time";
        /**
         * Date
         */
        private static final String ADATES="date";
        Context context = null;

        public Mysqlhelper(Context context,SQLiteDatabase.CursorFactory factory){
            super(context,DATABASE_NAME,factory,VERSION);
            this.context = context;
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
            String Create_subs  = "CREATE TABLE "+TNAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+CNAME+" TEXT);";
            db.execSQL(Create_subs);
            String chats  = "CREATE TABLE "+ATNAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ACNAME+" TEXT, " +
                    ADATES+" TEXT, " +
                    ATIMES+" TEXT );";
            db.execSQL(chats);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TNAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ATNAME);
            onCreate(sqLiteDatabase);
        }
    }
}
