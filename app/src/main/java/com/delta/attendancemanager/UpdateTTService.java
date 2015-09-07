package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateTTService extends IntentService {
    private static final String ACTION_UPCOMING = "com.delta.attendancemanager.action.UPCOMING";
    private static final String ACTION_TT = "com.delta.attendancemanager.action.TT";
    private static final String TIME = "com.delta.attendancemanager.extra.TIME";
    private static final String JSON = "com.delta.attendancemanager.extra.JSON";


    public static void startActionUpcoming(Context context, JSONObject json) {
        Intent intent = new Intent(context, UpdateTTService.class);
        intent.setAction(ACTION_UPCOMING);
        intent.putExtra(JSON, json.toString());
        context.startService(intent);

    }


    public static void startActionTT(Context context, JSONObject json) {
        Intent intent = new Intent(context, UpdateTTService.class);
        intent.setAction(ACTION_TT);
        intent.putExtra(JSON, json.toString());
        context.startService(intent);
    }

    public UpdateTTService() {
        super("UpdateTTService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPCOMING.equals(action)) {
                try {
                    final JSONObject json = new JSONObject(intent.getStringExtra(JSON));
                    handleUpcoming(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (ACTION_TT.equals(action)) {
                try {
                    final JSONObject json = new JSONObject(intent.getStringExtra(JSON));
                    handleTT(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

 //TODO: API console
    private void handleUpcoming(JSONObject json) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleTT(JSONObject json) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
