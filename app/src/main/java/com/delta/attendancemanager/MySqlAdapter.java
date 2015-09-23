package com.delta.attendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakshmanaram on 14/9/15.
 */
public class MySqlAdapter {
    Context context;
    Mysqlhelper mysqlhelper;
    private static ArrayList<String> subs = new ArrayList<>();
    public MySqlAdapter(Context context,SQLiteDatabase.CursorFactory factory)
    {
        this.context = context;
        mysqlhelper = new Mysqlhelper(context,factory);

    }
    public void get_ub()
    {
        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TNAME, new String[] {
                        Mysqlhelper.CNAME},
                null,
                null,
                null, null, null , null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(Mysqlhelper.CNAME))!=null){
                subs.add(c.getString(c.getColumnIndex(Mysqlhelper.CNAME)));
            }
            c.moveToNext();
        }
        c.close();
    }

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

    public ArrayList<String> getSubs()
    {
        if(subs.isEmpty())
            get_ub();
        return subs;
    }

    public void delete_sub(String sub){                                                         //TODO for deleting subject from the cr side
        SQLiteDatabase db = mysqlhelper.getWritableDatabase();
        db.delete(Mysqlhelper.TNAME, Mysqlhelper.CNAME + " =? ", new String[]{sub});
    }

    public boolean ispresent(String day){
        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor cursor = db.query(Mysqlhelper.TABLENAME, new String[]{Mysqlhelper.DAY}, Mysqlhelper.DAY + "=?",
                new String[]{day}, null, null, null, null);
        if(!cursor.moveToFirst())
            return false;
        else
            return true;
    }

    public void delete_day(String day) {
        SQLiteDatabase db=mysqlhelper.getWritableDatabase();
        db.delete(Mysqlhelper.TABLENAME, Mysqlhelper.DAY + " = ?", new String[]{day});
    }

    public List<String[]> get_days(){
        String[] all=new String[9];
        List<String[]> s=new ArrayList<>();
        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
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
        c.close();
        return s;
    }

    public String[]  get_mon(){
        String[] all=new String[9];
        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
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
            if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals("Monday")){
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
        c.close();
        return all;
    }

    public String[]  get_tue(){
        String[] all=new String[9];

        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
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
            if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals("Tuesday")){
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
        c.close();
        return all;
    }

    public String[]  get_wed(){
        String[] all=new String[9];
        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
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
            if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals("Wednesday")){
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
        c.close();
        return all;
    }

    public String[]  get_thur(){
        String[] all=new String[9];

        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
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
            if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals("Thursday")){
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
        c.close();
        return all;
    }

    public String[]  get_fri(){
        String[] all=new String[9];

        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
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
            if(c.getString(c.getColumnIndex(Mysqlhelper.DAY)).equals("Friday")){
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
        c.close();
        return all;
    }

    public String[] get_tomo(){
        String[] all=new String[9];
        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.TABLENAME, new String[] {
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
        c.close();
        return  all;
    }

    public void add_day(String day,String s830,String s920,String s1030,String s1120,String s130,String s220,String s310,String s400){
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

    public void addmsg(String msg){
        ContentValues v=new ContentValues();
        v.put(Mysqlhelper.ACNAME,msg);
        SQLiteDatabase db=mysqlhelper.getWritableDatabase();
        db.insert(Mysqlhelper.TABLENAME, null, v);
    }

    public String[] getmsgs(){
        List<String> msg=new ArrayList<>();

        SQLiteDatabase db=mysqlhelper.getReadableDatabase();
        Cursor c=db.query(true, Mysqlhelper.ATNAME, new String[] {
                        Mysqlhelper.ACNAME},
                null,
                null,
                null, null, null , null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(Mysqlhelper.ACNAME))!=null){
                msg.add(c.getString(c.getColumnIndex(Mysqlhelper.ACNAME)));

            }
            c.moveToNext();

        }
        c.close();

        String[] msgs=new String[msg.size()];
        msgs=msg.toArray(msgs);

        return msgs;
    }

    public void update_tomo(String[] subs){
        delete_day(Mysqlhelper.TOMO);
        add_day(Mysqlhelper.TOMO,subs[1],subs[2],subs[3],subs[4],subs[5],subs[6],subs[7],subs[8]);
    }

    static class Mysqlhelper extends SQLiteOpenHelper{
        private static final int VERSION =5;
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
        private static final String TNAME = "subjects";
        private static final String CNAME = "subject";
        private static final String ATNAME="Announcements";
        private static final String ACNAME="announcements";

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
            String chats  = "CREATE TABLE "+ATNAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+ACNAME+" TEXT);";
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
