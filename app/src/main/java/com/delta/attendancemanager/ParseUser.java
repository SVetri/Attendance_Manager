package com.delta.attendancemanager;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by lakshmanaram on 2/1/16.
 */
public class ParseUser extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        try {
            Parse.initialize(this, "TrAIQXwU3FAEWHGeR4QAQETp9M8FXRHlUM9FtddN", "FHpgYG3H5n3YEvI5bzwGaUF5j4SLxGaj8c2hXjkL");
            ParseInstallation.getCurrentInstallation().saveInBackground();
//            Log.d("hello", "Parse got initialized");
    }catch (Exception e){
            e.printStackTrace();
        }

    }

}
