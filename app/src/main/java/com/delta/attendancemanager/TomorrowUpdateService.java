package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;

import java.util.Calendar;


public class TomorrowUpdateService extends IntentService {
    MySqlHandler handler;
    String[] all;
    public TomorrowUpdateService() {
        super("TomorrowUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handler=new MySqlHandler(getApplicationContext(),null);
        Calendar c=Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        all=new String[9];
        boolean today;
        if(hour<16)
            today=true;
        else
            today=false;

        int dayw=c.get(Calendar.DAY_OF_WEEK);
        switch (dayw) {
            case Calendar.MONDAY:
                if(today)
                    all = handler.get_mon();
                else
                    all=handler.get_tue();
                break;
            case Calendar.TUESDAY:
                if(today)
                    all = handler.get_tue();
                else
                    all=handler.get_wed();
                break;
            case Calendar.WEDNESDAY:
                if(today)
                    all = handler.get_wed();
                else
                    all=handler.get_thur();
                break;
            case Calendar.THURSDAY:
                if(today)
                    all = handler.get_thur();
                else
                    all=handler.get_fri();
                break;
            case Calendar.FRIDAY:
                if(today)
                    all = handler.get_fri();
                else
                    all=handler.get_mon();
                break;
        }
        handler.update_tomo(all);

    }

}
