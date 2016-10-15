package com.delta.attendancemanager;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;

import java.util.Calendar;

public class AlarmService extends IntentService {
    private static final String ACTION_SET_CUSTOM_ALARM = "com.delta.attendancemanager.action.SET_CUSTOM_ALARM";
    private static final String CANCEL_ALARM = "com.delta.attendancemanager.action.CANCEL_ALARM";
    private static final String HOUR = "com.delta.attendancemanager.hour";
    private static final String MIN = "com.delta.attendancemanager.min";
    public static void startActionSetCustomAlarm(Context context, int hour, int min) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(ACTION_SET_CUSTOM_ALARM);
        intent.putExtra(HOUR,hour);
        intent.putExtra(MIN,min);
        context.startService(intent);
    }
    public static void startActionSetDefaultAlarm(Context context){
        Intent intent = new Intent(context,AlarmService.class);
        intent.setAction(ACTION_SET_CUSTOM_ALARM);
        intent.putExtra(HOUR,16);
        intent.putExtra(MIN,0);
        context.startService(intent);
    }
    public static void cancelAlarm(Context context){
        Intent intent = new Intent(context,AlarmService.class);
        intent.setAction(CANCEL_ALARM);
        context.startService(intent);
    }

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SET_CUSTOM_ALARM.equals(action)) {
                final int hour = intent.getIntExtra(HOUR,16);
                final int min = intent.getIntExtra(MIN,0);
                handleActionSetcustomalarm(hour, min);
            }
            else if (CANCEL_ALARM.equals(action)){
                handleCancelAlarm();
            }
        }
    }
    private void handleActionSetcustomalarm(int lhour, int lmin) {
        Intent inten = new Intent(this, AlarmReceiver.class);
        Calendar cal = Calendar.getInstance();
        Calendar calComparison = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,lhour);
        cal.set(Calendar.MINUTE, lmin);
        if(cal.after(calComparison))
            cal.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH)+1);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , AlarmManager.INTERVAL_DAY , PendingIntent.getBroadcast(this, 0, inten, PendingIntent.FLAG_UPDATE_CURRENT));
    }


    private void handleCancelAlarm(){
        Intent inten = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,inten,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }
}
