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

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;


public class GCMMessageHandler extends IntentService {

    static final String MSG_TT="";
    static final String MSG_UT="";
    int notifyID=1154;

    String mes;
    private Handler handler;

    public GCMMessageHandler() {
        super("GCMMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

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
                   //TODO:TT
                    try {
                        JSONObject js=new JSONObject((String) extras.get(MSG_TT));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(extras.get(MSG_UT)!=null){
                    //TODO:UT
                    try {
                        JSONObject js=new JSONObject((String) extras.get(MSG_UT));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        GCMReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(int type,String msg) {
        switch (type){
            case 0:
                Intent resultIntent = new Intent(this, MainActivity.class);
                resultIntent.putExtra("msg", msg);
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
                 resultIntent = new Intent(this, MainActivity.class);
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