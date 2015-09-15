package com.delta.attendancemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver
{
    ALarm alarm = new ALarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.SetAlarm(context);
        }
    }
}
