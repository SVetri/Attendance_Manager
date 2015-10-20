package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

public class AttendanceBackup extends IntentService {
    private static final String ACTION_BACKUP = "com.delta.attendancemanager.action.BACKUP";
    private static final String ACTION_RETREIVE_ATTENDANCE = "com.delta.attendancemanager.action.RETREIVE.attendance";

    public static void backupattendance(Context context) {
        Intent intent = new Intent(context, AttendanceBackup.class);
        intent.setAction(ACTION_BACKUP);
        context.startService(intent);
    }

    public static void retreive_attendance(Context context) {
        Intent intent = new Intent(context, AttendanceBackup.class);
        intent.setAction(ACTION_RETREIVE_ATTENDANCE);
        context.startService(intent);
    }

    public AttendanceBackup() {
        super("AttendanceBackup");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BACKUP.equals(action)) {
                handleActionBackup();
            } else if (ACTION_RETREIVE_ATTENDANCE.equals(action)) {
                handleretreiveattendance();
            }
        }
    }

    private void handleActionBackup() {

    }
    private void handleretreiveattendance(){

    }
}
