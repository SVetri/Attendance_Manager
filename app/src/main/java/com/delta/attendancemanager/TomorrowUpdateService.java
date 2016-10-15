package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;

import java.util.Calendar;

/**
 * Handles the view and the methods for update the Classes on the next day
 */
public class TomorrowUpdateService extends IntentService {
    MySqlAdapter handler;
    String[] all;

    /**
     * Create an object that update the attendances for the next day from the current one
     */
    public TomorrowUpdateService() {
        super("TomorrowUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handler = new MySqlAdapter(getApplicationContext(), null);
        Calendar c = Calendar.getInstance();
        all = new String[9];
        boolean today = isDayFinished();

        int dayw = c.get(Calendar.DAY_OF_WEEK);
        switch (dayw) {
            case Calendar.MONDAY:
                all = getDayText(today, handler.get_mon(), handler.get_tue());
                break;
            case Calendar.TUESDAY:
                all = getDayText(today, handler.get_tue(), handler.get_wed());
                break;
            case Calendar.WEDNESDAY:
                all = getDayText(today, handler.get_wed(), handler.get_thur());
                break;
            case Calendar.THURSDAY:
                all = getDayText(today, handler.get_thur(), handler.get_fri());
                break;
            case Calendar.FRIDAY:
                all = getDayText(today, handler.get_fri(), handler.get_mon());
                break;
            default:
                all = handler.get_mon();
        }
        handler.update_tomo(all);

    }

    /**
     * Check if last class has been passed
     *
     * @return true if passed, false otherwise
     */
    private boolean isDayFinished() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 16)
            return true;
        else
            return false;
    }

    /**
     * Check what subjects should be displayed
     *
     * @param day  false if day hasn't finished yet
     * @param day1 subjects for current day
     * @param day2 subjects for next day
     * @return Display subjects for current day if current day's not finished yet, nex tady subjects otherwise
     */
    private String[] getDayText(boolean day, String[] day1, String[] day2) {
        if (day)
            return day1;
        else
            return day2;
    }

}
