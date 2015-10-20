package com.delta.attendancemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"inside receiver",Toast.LENGTH_LONG).show();

        Intent in = new Intent(context,Userhome.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);

        Intent tomointent = new Intent(context,TomorrowUpdateService.class);
        context.startService(tomointent);
    }
}
