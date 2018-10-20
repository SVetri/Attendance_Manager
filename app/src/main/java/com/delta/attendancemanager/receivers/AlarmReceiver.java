package com.delta.attendancemanager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.delta.attendancemanager.services.AttendanceServerService;
import com.delta.attendancemanager.services.TomorrowUpdateService;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AttendanceServerService.syncAttendance(context);
        Intent tomointent = new Intent(context,TomorrowUpdateService.class);
        context.startService(tomointent);

        AttendanceServerService.addAttendance(context);
    }
}
