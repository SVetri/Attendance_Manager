package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class APIManagerService extends IntentService {
    MySqlAdapter handler;
    String[] allString;
    MySqlAdapter adapter;
    List<String[]> all,subs;


    public APIManagerService() {
        super("APIManagerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        subs=new ArrayList<>();
        String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday"};
        int mode=0;
        String[] times;
        SharedPreferences share1=getSharedPreferences("user", Context.MODE_PRIVATE);
        String rno=share1.getString(MainActivity.RNO, ":)");
        String batch = rno.substring(0,rno.length()-3);
        String URL = MainActivity.URL+"/getTimetable/"+batch;
//TODO:send username using SharedPref
        all=new ArrayList<>();
        adapter=new MySqlAdapter(this,null);
//        Bundle b=intent.getExtras();
//        if(b!=null){
//            user=b.getString("rno");
//            user=user.substring(0, 6);
//            mode=b.getInt("mode");
//        }
        switch(mode) {
            case 0:
                try {
                    times=new String[9];
                    JSONParser jp = new JSONParser();
                    List<NameValuePair> tt = new ArrayList<>();
                    JSONObject jd=new JSONObject();
//                    tt.add(new BasicNameValuePair("username", user));
                    JSONObject js = jp.makeHttpRequest(URL, "POST", jd);
                    for(String i : days) {
                        JSONObject day = js.getJSONObject(i);
                        times[0]=i;
                        times[1]=day.getString("830");
                        times[2]=day.getString("920");
                        times[3]=day.getString("1030");
                        times[4]=day.getString("1120");
                        times[5]=day.getString("130");
                        times[6]=day.getString("220");
                        times[7]=day.getString("310");
                        times[8]=day.getString("400");
                        for(int k=1;k<9;k++)
                            adapter.add_sub(times[k]);
                        Log.d("hel","api manager: "+times[0]+" "+times[5]);
                        adapter.add_day(times[0],times[1],times[2],times[3],times[4],times[5],times[6],times[7],times[8]);
                    }

                    Log.d("TAG", js.toString());
                    Log.d("TAG", js.toString());

                } catch (JSONException e) {
                    Log.e("APIManagerService", e.toString());
                }

                break;
            case 1:



        }
        handler=new MySqlAdapter(getApplicationContext(),null);
        Calendar c=Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        allString=new String[9];
        boolean today;
        if(hour<16)
            today=true;
        else
            today=false;

        int dayw=c.get(Calendar.DAY_OF_WEEK);
        switch (dayw) {
            case Calendar.MONDAY:
                if(today)
                    allString = handler.get_mon();
                else
                    allString=handler.get_tue();
                break;
            case Calendar.TUESDAY:
                if(today)
                    allString = handler.get_tue();
                else
                    allString=handler.get_wed();
                break;
            case Calendar.WEDNESDAY:
                if(today)
                    allString = handler.get_wed();
                else
                    allString=handler.get_thur();
                break;
            case Calendar.THURSDAY:
                if(today)
                    allString = handler.get_thur();
                else
                    allString=handler.get_fri();
                break;
            case Calendar.FRIDAY:
                if(today)
                    allString = handler.get_fri();
                else
                    allString=handler.get_mon();
                break;
            default:
                allString=handler.get_mon();

        }
        handler.update_tomo(allString);
        startActivity(new Intent(getApplicationContext(),Userhome.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}

