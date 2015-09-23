package com.delta.attendancemanager;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

public class AlarmService extends IntentService {
    private static final String ACTION_SET_CUSTOM_ALARM = "com.delta.attendancemanager.action.SET_CUSTOM_ALARM";
    private static final String ACTION_SET_DEFAULT_ALARM = "com.delta.attendancemanager.action.SET_DEFAULT_ALARM";
    private static final String CANCEL_ALARM = "com.delta.attendancemanager.action.CANCEL_ALARM";
    private static final String HOUR_PARAM = "com.delta.attendancemanager.extra.hour";
    private static final String MIN_PARAM = "com.delta.attendancemanager.extra.min";

    public static void startActionSetCustomAlarm(Context context, int hour, int min) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(ACTION_SET_CUSTOM_ALARM);
        intent.putExtra(HOUR_PARAM, hour);
        intent.putExtra(MIN_PARAM, min);
        context.startService(intent);
    }

    public static void startActionSetDefaultAlarm(Context context) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(ACTION_SET_DEFAULT_ALARM);
        context.startService(intent);
    }

    public static void cancelAlarm(Context context){
        Intent intent = new Intent(context,AlarmService.class);
        intent.setAction(CANCEL_ALARM);
        context.startActivity(intent);
    }

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SET_CUSTOM_ALARM.equals(action)) {
                final int hour = intent.getIntExtra(HOUR_PARAM,16);
                final int min = intent.getIntExtra(MIN_PARAM,0);
                handleActionSetcustomalarm(hour, min);
            } else if (ACTION_SET_DEFAULT_ALARM.equals(action)) {
                handleActionSetdefaultalarm();
            }
            else if (CANCEL_ALARM.equals(action)){
                handleCancelAlarm();
            }
        }
    }
    private void handleActionSetcustomalarm(int lhour, int lmin) {
        Intent inten = new Intent(this, AlarmReceiver.class);                                                    //TODO: randomize it depending upon the roll number later.
        Long min = Long.valueOf(lmin * 60 * 1000);
        Long hour = Long.valueOf(lhour * 60 * 60 *1000);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, min+hour, AlarmManager.INTERVAL_DAY , PendingIntent.getBroadcast(this, 0, inten, PendingIntent.FLAG_UPDATE_CURRENT));
        Toast.makeText(this, "Custom alarm set", Toast.LENGTH_LONG).show();
    }

    private void handleActionSetdefaultalarm(){
        Intent inten = new Intent(this, AlarmReceiver.class);
        Long min = Long.valueOf(0 * 60 * 1000);
        Long hour = Long.valueOf(16 * 60 * 60 *1000);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, min+hour, AlarmManager.INTERVAL_DAY , PendingIntent.getBroadcast(this, 0, inten, PendingIntent.FLAG_UPDATE_CURRENT));
        Toast.makeText(this, "Default alarm set", Toast.LENGTH_LONG).show();
    }

    private void handleCancelAlarm(){
        Intent inten = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,inten,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_LONG).show();
    }
}
