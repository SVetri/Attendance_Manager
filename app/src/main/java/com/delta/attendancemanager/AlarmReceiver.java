package com.delta.attendancemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class handles the alarms notifications.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AttendanceServerService.syncAttendance(context);
        Intent tomointent = new Intent(context,TomorrowUpdateService.class);
        context.startService(tomointent);

        AttendanceServerService.addAttendance(context);
    }
}
