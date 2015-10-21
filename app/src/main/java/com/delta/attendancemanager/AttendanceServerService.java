package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *service that syncs attendance and retrieves it.
 */
public class AttendanceServerService extends IntentService {
    private static final String SYNC = "com.delta.attendancemanager.action.sync.attendance";
    private static final String RETRIEVE = "com.delta.attendancemanager.action.retrieve.attendance";
    private static final String ADD = "com.delta.attendancemanager.action.add.local.attendance";
    private static final String DELETE = "com.delta.attendancemanager.action.delete.local.attendance";

    public static void syncAttendance(Context context) {
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(SYNC);
        context.startService(intent);
    }

    public static void addAttendance(Context context){
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(ADD);
        context.startService(intent);
    }

    public static void deleteAttendance(Context context){
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(DELETE);
        context.startService(intent);
    }

    public static void retrieveAttendance(Context context) {
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(RETRIEVE);
        context.startService(intent);
    }

    public AttendanceServerService() {
        super("AttendanceServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SYNC.equals(action)) {
                handleSync();
            } else if(ADD.equals(action)) {
                handleAdd();
            } else if(DELETE.equals(action)) {
                handledelete();
            } else if (RETRIEVE.equals(action)) {
                handleRetrieve();
            }
        }
    }

    private void handleSync(){

    }

    private void handleAdd(){
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        MySqlAdapter helper = new MySqlAdapter(getApplicationContext(),null);
        String[] subjects = helper.get_tomo();
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        for(int i=1;i<=8;i++){
            Date date = new Date(now.get(Calendar.YEAR)-1900, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),TTimings.hour[i], TTimings.min[i]);                                                                  //1900+yyyy;      TODO: check whther the normal date is working or change it to 1900+yyyy.
            atAdapter.add_attendance(subjects[i], sdf.format(date), 0);
        }
    }

    private void handledelete(){
        MySqlAdapter helper = new MySqlAdapter(getApplicationContext(),null);
        String[] subjects = helper.get_tomo();
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        for(int i=1;i<=8;i++){
            Date date = new Date(now.get(Calendar.YEAR)-1900, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),TTimings.hour[i], TTimings.min[i]);                                                                  //1900+yyyy;      TODO: check whther the normal date is working or change it to 1900+yyyy.
            atAdapter.delete_data(subjects[i],sdf.format(date));
        }
    }

    private void handleRetrieve(){

    }

}
