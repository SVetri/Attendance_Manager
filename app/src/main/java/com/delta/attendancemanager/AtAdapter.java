package com.delta.attendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import javax.security.auth.Subject;

/**
 * Created by lakshmanaram on 17/8/15.
 */
public class AtAdapter {
    Context context;
    Athelper athelper;
    int totalclasses,classes_attended,percentage,projected_percentage,pending_classes;
    ArrayList<String> subj = new ArrayList<>();
    ArrayList<String> dt = new ArrayList<>();
    ArrayList<Integer> presint;
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

    public void add_attendance(String subject, String datetime, int present){               //to add attendance
        SQLiteDatabase sqLiteDatabase = athelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(athelper.SUBJECT,subject);
        cv.put(athelper.DATETIME,datetime);
        cv.put(athelper.PRESENT,present);
        sqLiteDatabase.insert(athelper.TABLE_NAME,null,cv);
        return;
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
        db.delete(athelper.TABLE_NAME,athelper.SUBJECT+ " =? AND "+athelper.DATETIME+" =? ",new String[]{subject,datet});
    }

    public void subject_info(String subject){                                                               //TODO for individual subject information
        SQLiteDatabase db = athelper.getWritableDatabase();
        String[] columns = {athelper.DATETIME};
        Cursor cursor1 = db.query(athelper.TABLE_NAME,columns,athelper.SUBJECT + " =? AND "+athelper.PRESENT+ " =1 ",new String[]{subject},null,null,null);   //classes attended
        cursor1.moveToNext();
        classes_attended = cursor1.getCount();
        Cursor cursor2 = db.query(athelper.TABLE_NAME, columns, athelper.SUBJECT + " =? ", new String[]{subject}, null, null, null);   //total classes
        cursor2.moveToNext();
        totalclasses = cursor2.getCount();
        Cursor cursor3 = db.query(athelper.TABLE_NAME,columns,athelper.SUBJECT + " =? AND "+athelper.PRESENT+ " =0 ",new String[]{subject},null,null,null);   //pending classes
        cursor3.moveToNext();
        pending_classes = cursor3.getCount();


    }
    public void fetch_subject_data(String sub){                                                         //TODO for individual subject history
        SQLiteDatabase db = athelper.getWritableDatabase();
        //SELECT subject,datetime,present FROM attendance WHERE subject = sub;
        String[] columns = {athelper.DATETIME,athelper.PRESENT};
        Cursor cursor = db.query(athelper.TABLE_NAME, columns, athelper.SUBJECT + " =? ", new String[]{sub}, null, null, null);
        int index1 = cursor.getColumnIndex(athelper.DATETIME);
        int index2 = cursor.getColumnIndex(athelper.PRESENT);
        subj.clear();
        dt.clear();
        presint.clear();
        while(cursor.moveToNext()) {
            String tempdt = cursor.getString(index1);     //datetime
            int temppr = cursor.getInt(index2);
            if(temppr!=0)                                                                                               //only updated classes
            {
                dt.add(tempdt);
                presint.add(temppr);
            }
        }
    }
    public void fetch_pending_data(){                                                                //TODO: for update my attendance activity
        SQLiteDatabase db = athelper.getWritableDatabase();
        //SELECT subject,datetime FROM attendance WHERE present = 0;
        String[] columns = {athelper.SUBJECT,athelper.DATETIME};
        Cursor cursor = db.query(athelper.TABLE_NAME,columns,athelper.PRESENT + " =0",null,null,null,null);
        int index0 = cursor.getColumnIndex(athelper.SUBJECT);
        int index1 = cursor.getColumnIndex(athelper.DATETIME);
        subj.clear();
        dt.clear();
        while(cursor.moveToNext()){
            String tempst = cursor.getString(index0);  //subject string
            String tempdt = cursor.getString(index1);     //datetime
            subj.add(tempst);
            dt.add(tempdt);
        }
    }
    public void update_attendance(String subject, String datetime, int present){                     //updating an already existing attendance ----- TODO:for both update my attendance and view my attendance's subject
        SQLiteDatabase db = athelper.getWritableDatabase();
        //UPDATE attendance SET present  = present WHERE present = 0;
        ContentValues cv = new ContentValues();
        //cv.put(athelper.SUBJECT, subject);
        //cv.put(Athelper.DATETIME,datetime);
        cv.put(Athelper.PRESENT, present);
        //int a = db.delete(Athelper.TABLE_NAME, Athelper.SUBJECT + " = ? AND " + Athelper.DATETIME + " = ? ", new String[]{subject, datetime});
        //db.insert(Athelper.TABLE_NAME,null,cv);
        int a = db.update(Athelper.TABLE_NAME,cv,"("+Athelper.SUBJECT+"=?) AND ("+Athelper.DATETIME + "=?)",new String[]{subject,datetime});
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
        return;
    }
    static class Athelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "semester.db";
        private static String TABLE_NAME = "attendance";
        Context context = null;
        private static final int DATABASE_VERSION = 2;
        private static final String ID = "_id";
        private static final String DATETIME = "datetime";
        private static final String SUBJECT = "subject";
        private static final String PRESENT = "present";
        private static final String create = "CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SUBJECT+" TEXT, "+DATETIME+" TEXT, "+PRESENT+" INTEGER);";
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
