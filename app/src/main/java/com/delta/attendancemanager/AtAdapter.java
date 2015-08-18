package com.delta.attendancemanager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lakshmanaram on 17/8/15.
 */
public class AtAdapter {
    Context context;
    Athelper athelper;

    public AtAdapter(Context context)
    {
        this.context = context;
        athelper = new Athelper(context);
    }
    static class Athelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "semester";
        private static String TABLE_NAME = "attendance";
        Context context = null;
        private static final int DATABASE_VERSION = 1;
        private static final String ID = "_id";
        private static final String DATETIME = "datetime";
        private static final String SUBJECT = "subject";
        private static final String PRESENT = "present";
        private static final String create = "CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SUBJECT+" TEXT, "+DATETIME+" TEXT, "+PRESENT+" INTEGER;";
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
