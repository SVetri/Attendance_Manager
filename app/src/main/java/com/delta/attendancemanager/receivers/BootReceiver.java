package com.delta.attendancemanager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.delta.attendancemanager.services.AlarmService;


public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            AlarmService.cancelAlarm(context);
            AlarmService.startActionSetDefaultAlarm(context);
        }
    }
}
