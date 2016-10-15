package com.delta.attendancemanager;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseHandler extends IntentService {
    static final String MSG_TT = "tt";
    static final String MSG_UT = "ut";
    static final String MSG_CHAT = "chat";
    static String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    String[] times;
    String title, message;
    JSONObject data;
    int notifyID = 1154;
    JSONObject myjsonObject;
    String mes;
    private MySqlAdapter handler;

    public ParseHandler() {
        super("ParseHandler");
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new MySqlAdapter(this, null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle b = intent.getExtras();
        String a = b.getString("jsonData");

        times = new String[9];
            try {
                myjsonObject = new JSONObject(a);
                Log.d("happens", myjsonObject.toString());
                 message = myjsonObject.getString("type");

                //   String messageType = gcm.getMessageType(intent);        TODO message type

                if (message.equals(MSG_TT)) {
                    startService(new Intent(getApplicationContext(),APIManagerService.class));
                    sendNotification(0, "Timetable Updated");


                }
                if (message.equals(MSG_UT)) {
                    // sendNotification(0, "success");
                    try {
                        data = myjsonObject.getJSONObject("data");
                        String ex = data.getString("data");
                        JSONObject js = new JSONObject(ex);
                        AttendanceServerService.deleteAttendance(getApplicationContext());
                        updateUT(js);
                        AttendanceServerService.addAttendance(getApplicationContext());
                        sendNotification(1, "Upcoming Timetable Updated");
                    } catch (JSONException e) {
                        Log.e("ParseHandler", e.toString());
                    }
                }
                if (message.equals(MSG_CHAT)) {
//                    sendNotification(0,"success");


                    try {
                        data = myjsonObject.getJSONObject("data");
                        String js = data.getString("an");
                        JSONObject data = new JSONObject(js);
                        String date = data.getString("date");
                        String time = data.getString("time");
                        String msg = data.getString("msg");
                        announcements(msg, date, time);
                    } catch (JSONException e) {
                        Log.e("ParseHandler", e.toString());
                    }


//                        sendNotification(1,"Upcoming Timetable Updated");

                }

            } catch (JSONException e) {
                Log.e("hi helloo", "Push message json exception: " + e.getMessage());
            }

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

    private void updateTT() throws JSONException {
        //TODO:Drop Table
        startService(new Intent(getApplicationContext(),APIManagerService.class));
    }

    private void announcements(String msg, String date, String time) {
        handler.addmsg(msg, time, date);
        sendNotification(2, "Received Announcement From CR");

    }

    private void sendNotification(int type, String msg) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        switch (type) {
            case 0:
                Intent resultIntent = new Intent(this, WeeklyTimetable.class);
                //resultIntent.putExtra("msg", msg);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                        resultIntent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder mNotifyBuilder;

                mNotifyBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(msg)
                        .setContentText(msg)
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
                resultIntent = new Intent(this, Userhome.class);
                resultIntent.putExtra("msg", msg);
                resultPendingIntent = PendingIntent.getActivity(this, 0,
                        resultIntent, PendingIntent.FLAG_ONE_SHOT);

                mNotifyBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("Alert")
                        .setContentText(msg)
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
                mNotifyBuilder.setContentText(msg);
                // Set autocancel
                mNotifyBuilder.setAutoCancel(true);
                // Post a notification
                mNotificationManager.notify(notifyID, mNotifyBuilder.build());
                break;
            case 2:
                resultIntent = new Intent(this, Userhome.class);
                resultIntent.putExtra("msg", msg);
                resultIntent.putExtra("page",2);
                resultPendingIntent = PendingIntent.getActivity(this, 0,
                        resultIntent, PendingIntent.FLAG_ONE_SHOT);

                mNotifyBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("CR App")//TODO: name it
                        .setContentText(msg)
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
                mNotifyBuilder.setContentText(msg);
                // Set autocancel
                mNotifyBuilder.setAutoCancel(true);
                // Post a notification
                mNotificationManager.notify(notifyID, mNotifyBuilder.build());
                break;
        }

    }
}
