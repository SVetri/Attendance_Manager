package com.delta.attendancemanager;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lakshmanaram on 13/1/16.
 */
public class DisplayToast implements Runnable {
    private final Context mContext;
    String mText;

    /**
     * Create a new toast notification
     * @param mContext specify where create the toast notification
     * @param text sets the notification text
     */
    public DisplayToast(Context mContext, String text){
        this.mContext = mContext;
        mText = text;
    }

    public synchronized void run(){
        Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
    }
}