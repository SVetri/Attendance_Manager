package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;


public class UpdateTTService extends IntentService {
    private static final String ACTION_UPCOMING = "com.delta.attendancemanager.action.UPCOMING";
    private static final String ACTION_TT = "com.delta.attendancemanager.action.TT";
    private static final String TIME = "com.delta.attendancemanager.extra.TIME";
    private static final String SUB = "com.delta.attendancemanager.extra.SUB";
    private static final String DAY = "com.delta.attendancemanager.extra.DAY";


    public static void startActionUpcoming(Context context, String time, String sub) {
        Intent intent = new Intent(context, UpdateTTService.class);
        intent.setAction(ACTION_UPCOMING);
        intent.putExtra(TIME, time);
        intent.putExtra(SUB, sub);
        context.startService(intent);

    }


    public static void startActionTT(Context context, String day, String time,String sub) {
        Intent intent = new Intent(context, UpdateTTService.class);
        intent.setAction(ACTION_TT);
        intent.putExtra(DAY, day);
        intent.putExtra(TIME, time);
        intent.putExtra(SUB, sub);
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
                final String time = intent.getStringExtra(TIME);
                final String sub = intent.getStringExtra(SUB);
                handleUpcoming(time, sub);
            } else if (ACTION_TT.equals(action)) {
                final String day = intent.getStringExtra(DAY);
                final String time = intent.getStringExtra(TIME);
                final String sub = intent.getStringExtra(SUB);
                handleTT(day, time, sub);
            }
        }
    }

 //TODO: API console
    private void handleUpcoming(String time, String sub) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleTT(String day, String time,String sub) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
