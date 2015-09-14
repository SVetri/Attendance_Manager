package com.delta.attendancemanager;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;


public class GCMMessageHandler extends IntentService {

    static final String MSG_TT="";
    static final String MSG_UT="";
    static String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday"};
    String[] times;
    int notifyID=1154;

    String mes;
    private MySqlAdapter handler;

    public GCMMessageHandler() {
        super("GCMMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new MySqlAdapter(this,null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        times=new String[9];

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification(0,"Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification(0,"Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                if(extras.get(MSG_TT)!=null){
                    try {
                        JSONObject js=new JSONObject((String) extras.get(MSG_TT));
                        updateTT(js);
                        sendNotification(0,"Timetable Updated");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(extras.get(MSG_UT)!=null){
                    try {
                        JSONObject js=new JSONObject((String) extras.get(MSG_UT));
                        updateUT(js);
                        sendNotification(1,"Upcoming Timetable Updated");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        GCMReceiver.completeWakefulIntent(intent);
    }

    private void updateUT(JSONObject js) throws JSONException {

        times[0] = "tomorrow";
        times[1] = js.getString("830");
        times[2] = js.getString("920");
        times[3] = js.getString("1030");
        times[4] = js.getString("1120");
        times[5] = js.getString("130");
        times[6] = js.getString("220");
        times[7] = js.getString("310");
        times[8] = js.getString("400");
        handler.update_tomo(times);
    }

    private void updateTT(JSONObject js) throws JSONException {
        //TODO:Drop Table
        for (String i : days) {
            JSONObject day = js.getJSONObject(i);
            times[0] = i;
            times[1] = day.getString("830");
            times[2] = day.getString("920");
            times[3] = day.getString("1030");
            times[4] = day.getString("1120");
            times[5] = day.getString("130");
            times[6] = day.getString("220");
            times[7] = day.getString("310");
            times[8] = day.getString("400");
            for (int k = 1; k < 9; k++)
                handler.add_sub(times[k]);
            Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
            Log.d("hel", "api manager: " + times[0] + " " + times[5]);
            handler.add_day(times[0], times[1], times[2], times[3], times[4], times[5], times[6], times[7], times[8]);
        }
        startService(new Intent(getApplicationContext(),TomorrowUpdateService.class));
    }

    private void sendNotification(int type,String msg) {
        switch (type){
            case 0:
                Intent resultIntent = new Intent(this, WeeklyTimetable.class);
                //resultIntent.putExtra("msg", msg);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                        resultIntent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder mNotifyBuilder;
                NotificationManager mNotificationManager;

                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                mNotifyBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("Alert")
                        .setContentText("You've received new message.")
                        .setSmallIcon(R.drawable.ic_launcher);
                // Set pending intent
                mNotifyBuilder.setContentIntent(resultPendingIntent);

                // Set Vibrate, Sound and Light
                int defaults = 0;
                defaults = defaults | Notification.DEFAULT_LIGHTS;
                defaults = defaults | Notification.DEFAULT_VIBRATE;
                defaults = defaults | Notification.DEFAULT_SOUND;

                mNotifyBuilder.setDefaults(defaults);
                // Set the content for Notification
                mNotifyBuilder.setContentText("New message from Server");
                // Set autocancel
                mNotifyBuilder.setAutoCancel(true);
                // Post a notification
                mNotificationManager.notify(notifyID, mNotifyBuilder.build());
                break;
            case 1:
                 resultIntent = new Intent(this, UpcomingTT.class);
                resultIntent.putExtra("msg", msg);
                 resultPendingIntent = PendingIntent.getActivity(this, 0,
                        resultIntent, PendingIntent.FLAG_ONE_SHOT);

                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                mNotifyBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("Alert")
                        .setContentText("You've received new message.")
                        .setSmallIcon(R.drawable.ic_launcher);
                // Set pending intent
                mNotifyBuilder.setContentIntent(resultPendingIntent);

                // Set Vibrate, Sound and Light
                defaults = 0;
                defaults = defaults | Notification.DEFAULT_LIGHTS;
                defaults = defaults | Notification.DEFAULT_VIBRATE;
                defaults = defaults | Notification.DEFAULT_SOUND;

                mNotifyBuilder.setDefaults(defaults);
                // Set the content for Notification
                mNotifyBuilder.setContentText("New message from Server");
                // Set autocancel
                mNotifyBuilder.setAutoCancel(true);
                // Post a notification
                mNotificationManager.notify(notifyID, mNotifyBuilder.build());
                break;

        }

    }

}