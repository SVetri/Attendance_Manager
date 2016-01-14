package com.delta.attendancemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
