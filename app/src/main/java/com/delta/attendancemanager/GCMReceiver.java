package com.delta.attendancemanager;


import android.app.Activity;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.support.v4.content.WakefulBroadcastReceiver;

public class GCMReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmMessageHandler will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMMessageHandler.class.getName());

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }
}