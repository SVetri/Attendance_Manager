package com.delta.attendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by lakshmanaram on 17/8/15.
 */
public class AtAdapter {
    Context context;
    Athelper athelper;
    int totalclasses,classes_attended,percentage,projected_percentage,pending_classes;
    ArrayList<String> subj = new ArrayList<>();
    ArrayList<String> dt = new ArrayList<>();
    ArrayList<Integer> presint = new ArrayList<>();
    public AtAdapter(Context contex)
    {
        this.context = contex;
        athelper = new Athelper(context);
    }

    public int getClasses_attended() {
        return classes_attended;
    }

    public int getPercentage() {
        percentage = classes_attended*100;
        percentage/=totalclasses;
        return percentage;
    }

    public int getProjected_percentage() {                                      //TODO finding out projected percentage
        return projected_percentage;
    }

    public int getTotalclasses() {
        return totalclasses;
    }

    public int getPending_classes() {
        return pending_classes;
    }

    public void to_update_data(){
        SQLiteDatabase db = athelper.getWritableDatabase();
        //SELECT subject,datetime FROM attendance WHERE update = 1;
        String[] columns = {Athelper.SUBJECT,Athelper.DATETIME};
        Cursor cursor = db.query(Athelper.TABLE_NAME,columns,Athelper.UPDATE + " =1",null,null,null,null);
        int index0 = cursor.getColumnIndex(Athelper.SUBJECT);
        int index1 = cursor.getColumnIndex(Athelper.DATETIME);
        int index2 = cursor.getColumnIndex(Athelper.PRESENT);
        subj.clear();
        dt.clear();
        presint.clear();
        while(cursor.moveToNext()){
            String tempst = cursor.getString(index0);  //subject string
            String tempdt = cursor.getString(index1);     //datetime
            int temppresentint = cursor.getInt(index2);
            subj.add(tempst);
            dt.add(tempdt);
            presint.add(temppresentint);
            update_up(tempst,tempdt,temppresentint);
        }
        cursor.close();
    }

    public void update_up(String subject, String datetime, int present){                     //updating an already existing attendance -----
        SQLiteDatabase db = athelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Athelper.UPDATE, 0);
        db.update(Athelper.TABLE_NAME, cv, "(" + Athelper.SUBJECT + "=?) AND (" + Athelper.DATETIME + "=?) AND (" + Athelper.PRESENT + "=?)", new String[]{subject, datetime, String.valueOf(present)});
    }

    public void add_attendance(String subject, String datetime, int present){               //to add attendance
        SQLiteDatabase sqLiteDatabase = athelper.getWritableDatabase();
        if(find_existing(subject,datetime)){
            ContentValues cv = new ContentValues();
            cv.put(Athelper.SUBJECT,subject);
            cv.put(Athelper.DATETIME,datetime);
            cv.put(Athelper.PRESENT,present);
            cv.put(Athelper.UPDATE,1);
            sqLiteDatabase.insert(Athelper.TABLE_NAME, null, cv);
        }
    }

    public boolean find_existing(String subject, String datetime){
        SQLiteDatabase db = athelper.getWritableDatabase();
        String[] columns = {Athelper.DATETIME};
        Cursor cursor1 = db.query(Athelper.TABLE_NAME, columns, Athelper.SUBJECT + " =? AND " + Athelper.DATETIME +  " =? ", new String[]{subject, datetime}, null, null, null);   //pending classes
        cursor1.moveToNext();
        int no_of_existing_records = cursor1.getCount();
        cursor1.close();
        return (no_of_existing_records==0);
    }

    public ArrayList<String> getDt() {
        return dt;
    }

    public ArrayList<Integer> getPresint() {
        return presint;
    }

    public ArrayList<String> getSubj() {
        return subj;
    }

    public void delete_data(String subject,String datet){                                                   //as of now no use
        SQLiteDatabase db = athelper.getWritableDatabase();
        //DELETE * FROM attendance WHERE subject = subject AND datetime = datet;
        db.delete(Athelper.TABLE_NAME,Athelper.SUBJECT+ " =? AND "+Athelper.DATETIME+" =? ",new String[]{subject,datet});
    }

    public void subject_info(String subject){
        SQLiteDatabase db = athelper.getWritableDatabase();
        String[] columns = {Athelper.DATETIME};
        Cursor cursor1 = db.query(Athelper.TABLE_NAME,columns,Athelper.SUBJECT + " =? AND "+Athelper.PRESENT+ " =1 ",new String[]{subject},null,null,null);   //pending classes
        cursor1.moveToNext();
        classes_attended = cursor1.getCount();
        cursor1.close();
        Cursor cursor2 = db.query(Athelper.TABLE_NAME, columns, Athelper.SUBJECT + " =? ", new String[]{subject}, null, null, null);   //total classes
        cursor2.moveToNext();
        totalclasses = cursor2.getCount();
        cursor2.close();
        Cursor cursor3 = db.query(Athelper.TABLE_NAME,columns,Athelper.SUBJECT + " =? AND "+Athelper.PRESENT+ " =0 ",new String[]{subject},null,null,null);   //pending classes
        cursor3.moveToNext();
        pending_classes = cursor3.getCount();
        cursor3.close();
//        fetch_subject_data(subject);
//        classes_attended = 0;
//        pending_classes = 0;
//        totalclasses = presint.size();
//        for(int i=0;i<totalclasses;i++){
//            if(presint.get(i)==0)
//                pending_classes++;
//            else if(presint.get(i)==1)
//                classes_attended++;
//        }
    }
    public void fetch_subject_data(String sub){
        SQLiteDatabase db = athelper.getReadableDatabase();
        //SELECT subject,datetime,present FROM attendance WHERE subject = sub;
        String[] columns = {Athelper.DATETIME,Athelper.PRESENT};
        Cursor cursor = db.query(Athelper.TABLE_NAME, columns, Athelper.SUBJECT + " =?",new String[]{sub}, null, null, null);
        int index1 = cursor.getColumnIndex(Athelper.DATETIME);
        int index2 = cursor.getColumnIndex(Athelper.PRESENT);
        subj.clear();
        dt.clear();
        presint.clear();
        while(cursor.moveToNext()) {
            String tempdt = cursor.getString(index1);     //datetime
            int temppr = cursor.getInt(index2);
            if(temppr!=0){
                dt.add(tempdt);
                presint.add(temppr);
            }
        }
        cursor.close();
    }
    public void fetch_pending_data(){
        SQLiteDatabase db = athelper.getWritableDatabase();
        //SELECT subject,datetime FROM attendance WHERE present = 0;
        String[] columns = {Athelper.SUBJECT,Athelper.DATETIME};
        Cursor cursor = db.query(Athelper.TABLE_NAME,columns,Athelper.PRESENT + " =0",null,null,null,null);
        int index0 = cursor.getColumnIndex(Athelper.SUBJECT);
        int index1 = cursor.getColumnIndex(Athelper.DATETIME);
        subj.clear();
        dt.clear();
        while(cursor.moveToNext()){
            String tempst = cursor.getString(index0);  //subject string
            String tempdt = cursor.getString(index1);     //datetime
            subj.add(tempst);
            dt.add(tempdt);
        }
        cursor.close();
    }
    public void update_attendance(String subject, String datetime, int present){                     //updating an already existing attendance -----
        SQLiteDatabase db = athelper.getWritableDatabase();
        //UPDATE attendance SET present  = present WHERE present = 0;
        ContentValues cv = new ContentValues();
        //cv.put(Athelper.SUBJECT, subject);
        //cv.put(Athelper.DATETIME,datetime);
        cv.put(Athelper.PRESENT, present);
        cv.put(Athelper.UPDATE,1);
        //int a = db.delete(Athelper.TABLE_NAME, Athelper.SUBJECT + " = ? AND " + Athelper.DATETIME + " = ? ", new String[]{subject, datetime});
        //db.insert(Athelper.TABLE_NAME,null,cv);
        db.update(Athelper.TABLE_NAME,cv,"("+Athelper.SUBJECT+"=?) AND ("+Athelper.DATETIME + "=?)",new String[]{subject,datetime});
        /*
        int a=0;
        try{
            a = 6;
            db.execSQL("UPDATE "+Athelper.TABLE_NAME+" SET "+Athelper.PRESENT+" = "+ Integer.toString(present)+" WHERE "+Athelper.SUBJECT+"='"+subject+"' AND "+Athelper.DATETIME+"='"+datetime+"';");
        }
        catch (Exception e){
            a = 5;
        }
        */
    }
    static class Athelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "semester.db";
        private static String TABLE_NAME = "attendance";
        Context context = null;
        private static final int DATABASE_VERSION = 5;
        private static final String ID = "_id";
        private static final String DATETIME = "datetime";
        private static final String SUBJECT = "subject";
        private static final String PRESENT = "present";
        private static final String UPDATE = "toupdate";
        private static final String create = "CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SUBJECT+" TEXT, "+DATETIME+" TEXT, "+PRESENT+" INTEGER, "+UPDATE+" INTEGER);";
        private static final String drop = "DROP TABLE IF EXISTS "+TABLE_NAME;
        public Athelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(create);
            } catch (SQLException e) {
                Log.d("hel", e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            try {
                sqLiteDatabase.execSQL(drop);
                onCreate(sqLiteDatabase);
            } catch (SQLException e) {
                Log.d("hel",e.toString());
            }
        }
    }
}
