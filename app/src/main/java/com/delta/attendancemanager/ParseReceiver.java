package com.delta.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handles the push notification
 */
public class ParseReceiver extends ParsePushBroadcastReceiver {

    /**
     * Main constructor
     */
    public ParseReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.e("yes hellooooo", "Push received: " + json);
            Intent i = new Intent(context,ParseHandler.class);
            i.putExtra("jsonData",json.toString());
            context.startService(i);

        } catch (JSONException e) {
            Log.e("yes hellooooo", "Push message json exception: " + e.getMessage());
        }

    }
}
