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

    /**
     * Handle the methods for the update adapter
     * @param contex specify where to construct
     */
    public AtAdapter(Context contex)
    {
        this.context = contex;
        athelper = new Athelper(context);
    }

    /**
     * Get the number of classes attended
     * @return number of classes attended
     */
    public int getClasses_attended() {
        return classes_attended;
    }

    /**
     * Get the percentage of attending
     * @return percentage of attending
     */
    public int getPercentage() {
        percentage = classes_attended*100;
        percentage/=totalclasses;
        return percentage;
    }

    /**
     * Get a projection of the future attending
     * @return
     */
    public int getProjected_percentage() {                                      //TODO finding out projected percentage
        return projected_percentage;
    }

    /**
     * Get the number of the total classes attended
     * @return number of the total classes attended
     */
    public int getTotalclasses() {
        return totalclasses;
    }

    /**
     * Get the number of the class pending
     * @return number of the class pending
     */
    public int getPending_classes() {
        return pending_classes;
    }

    /**
     * Update data to the server
     */
    public void to_update_data(){
        Cursor cursor = null;
        try{
            Log.i("in AtAdapter","to update data called");
            SQLiteDatabase db = athelper.getWritableDatabase();
            //SELECT subject,datetime FROM attendance WHERE update = 1;
            String[] columns = {Athelper.SUBJECT,Athelper.DATETIME,Athelper.PRESENT};
            cursor = db.query(Athelper.TABLE_NAME,columns,Athelper.UPDATE + " =1",null,null,null,null);
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
        } finally {
            cursor.close();
        }
    }

    /**
     * Update an attending
     * @param subject
     * @param datetime
     * @param present
     */
    public void update_up(String subject, String datetime, int present){                     //updating an already existing attendance -----
        Log.i("in AtAdapter","update up called");
        if(subject==null||subject.isEmpty())
            return;
        SQLiteDatabase db = athelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Athelper.UPDATE, 0);
        db.update(Athelper.TABLE_NAME, cv, "(" + Athelper.SUBJECT + "=?) AND (" + Athelper.DATETIME + "=?) AND (" + Athelper.PRESENT + "=?)", new String[]{subject, datetime, String.valueOf(present)});
    }

    /**
     * Add an attending
     * @param subject
     * @param datetime
     * @param present
     */
    public void add_attendance(String subject, String datetime, int present){               //to add attendance
        Log.i("in AtAdapter","add attendance called");
        if(subject==null||subject.isEmpty()||datetime.isEmpty())
            return;
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
        Cursor cursor1 = null;
        int no_of_existing_records = 0;
        try{
            Log.i("in AtAdapter","find existing called");
            if(subject.isEmpty()||datetime.isEmpty())
                return false;
            SQLiteDatabase db = athelper.getWritableDatabase();
            cursor1 = db.query(Athelper.TABLE_NAME, new String[] {Athelper.DATETIME}, Athelper.SUBJECT + " =? AND " + Athelper.DATETIME +  " =? ", new String[]{subject, datetime}, null, null, null);   //pending classes
            cursor1.moveToNext();
            no_of_existing_records = cursor1.getCount();
        } finally {
            cursor1.close();
            return (no_of_existing_records==0);
        }
    }


    public ArrayList<String> getDt() {
        return dt;
    }

    /**
     * Get a list for the presence
     * @return list for the presence
     */
    public ArrayList<Integer> getPresint() {
        return presint;
    }

    /**
     * Get a list of subjects
     * @return list of subjects
     */
    public ArrayList<String> getSubj() {
        return subj;
    }

    public void refresh_delete_data(String subject, String datet){
        Log.i("in AtAdapter","refresh delete data called");
        if(subject==null||  subject.isEmpty()||datet.isEmpty())
            return;
        SQLiteDatabase db = athelper.getWritableDatabase();
        //DELETE * FROM attendance WHERE subject = subject AND datetime = datet AND present = 0;
        db.delete(Athelper.TABLE_NAME,Athelper.SUBJECT+ " =? AND "+Athelper.DATETIME+" =? AND "+Athelper.PRESENT+" == 0",new String[]{subject,datet});
    }

    /**
     * Delete an attendance
     * @param subject to delete
     * @param datet date to delete
     */
    public void delete_data(String subject, String datet){                                                   //as of now no use
        Log.i("in AtAdapter","delete data called");
        if(subject==null||  subject.isEmpty()||datet.isEmpty())
            return;
        SQLiteDatabase db = athelper.getWritableDatabase();
        //DELETE * FROM attendance WHERE subject = subject AND datetime = datet;
        db.delete(Athelper.TABLE_NAME,Athelper.SUBJECT+ " =? AND "+Athelper.DATETIME+" =? ",new String[]{subject,datet});
    }

    /**
     * Retrieve info of a subject
     * @param subject to retrieve
     */
    public void subject_info(String subject){
        Log.i("in AtAdapter","subject_info called");
        if(subject==null ||subject.isEmpty()) {
            classes_attended = 0;
            totalclasses = 0;
            pending_classes = 0;
            return;
        }
        SQLiteDatabase db = athelper.getWritableDatabase();
        String[] columns = {Athelper.DATETIME};

        Cursor cursor1 = null;
        Cursor cursor2 = null;
        Cursor cursor3 = null;
        try{
            cursor1 = db.query(Athelper.TABLE_NAME,columns,Athelper.SUBJECT + " =? AND "+Athelper.PRESENT+ " =1 ",new String[]{subject},null,null,null);   //pending classes
            cursor1.moveToNext();
            classes_attended = cursor1.getCount();
            cursor2 = db.query(Athelper.TABLE_NAME, columns, Athelper.SUBJECT + " =? ", new String[]{subject}, null, null, null);   //total classes
            cursor2.moveToNext();
            totalclasses = cursor2.getCount();
            cursor3 = db.query(Athelper.TABLE_NAME,columns,Athelper.SUBJECT + " =? AND "+Athelper.PRESENT+ " =0 ",new String[]{subject},null,null,null);   //pending classes
            cursor3.moveToNext();
            pending_classes = cursor3.getCount();
        } finally {
            cursor1.close();
            cursor2.close();
            cursor3.close();
        }
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

    /**
     * Fetch data of a subject
     * @param sub subject to fetch
     */
    public void fetch_subject_data(String sub){
        Cursor cursor = null;
        try{
            Log.i("in AtAdapter","fetch_subject_data called");
            SQLiteDatabase db = athelper.getReadableDatabase();
            //SELECT subject,datetime,present FROM attendance WHERE subject = sub;
            String[] columns = {Athelper.DATETIME,Athelper.PRESENT};
            cursor = db.query(Athelper.TABLE_NAME, columns, Athelper.SUBJECT + " =?",new String[]{sub}, null, null, null);
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
        } finally {
            cursor.close();
        }
    }

    /**
     * Fetch data of the current day
     */
    public void fetch_pending_data(){
        Cursor cursor = null;
        try{
            Log.i("in AtAdapter","fetch_pending_data called");
            SQLiteDatabase db = athelper.getWritableDatabase();
            //SELECT subject,datetime FROM attendance WHERE present = 0;
            String[] columns = {Athelper.SUBJECT,Athelper.DATETIME};
            cursor = db.query(Athelper.TABLE_NAME,columns,Athelper.PRESENT + " =0",null,null,null,null);
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
        } finally {
            cursor.close();
        }
    }

    /**
     * Updating an already existing attendance
     * @param subject to update
     * @param datetime new date
     * @param present 1 if present, 0 otherwise
     */
    public void update_attendance(String subject, String datetime, int present){
        Log.i("in AtAdapter", "update attendance called " + subject + " " + datetime + " " + Integer.toString(present));
        String[] columns = {Athelper.SUBJECT,Athelper.DATETIME,Athelper.PRESENT};
        SQLiteDatabase db = athelper.getWritableDatabase();
        Cursor cursor = null;

        try{
            cursor = db.query(Athelper.TABLE_NAME,columns,null,null,null,null,null);
            int index0 = cursor.getColumnIndex(Athelper.SUBJECT);
            int index1 = cursor.getColumnIndex(Athelper.DATETIME);
            int index2 = cursor.getColumnIndex(Athelper.PRESENT);
            while(cursor.moveToNext()){
                String tempst = cursor.getString(index0);  //subject string
                String tempdt = cursor.getString(index1);     //datetime
                Log.i("in AtAdapter",tempst+"  "+tempdt+" "+Integer.toString(cursor.getInt(index2)));
            }
        } finally {
            cursor.close();
        }
        //UPDATE attendance SET present  = present WHERE present = 0;
        ContentValues cv = new ContentValues();
        cv.put(Athelper.PRESENT, present);
        cv.put(Athelper.UPDATE, 1);
        int result = db.update(Athelper.TABLE_NAME,cv,"("+Athelper.SUBJECT+"=?) AND ("+Athelper.DATETIME + "=?)",new String[]{subject,datetime});
        Log.i("in AtAdapter","updated "+ Integer.toString(result));
    }

    /**
     * Support class for the handling of the SQLite database
     */
    protected static class Athelper extends SQLiteOpenHelper{
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
