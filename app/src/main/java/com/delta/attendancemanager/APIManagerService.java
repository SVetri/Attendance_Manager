package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class APIManagerService extends IntentService {
    MySqlHandler handler;
    List<String[]> all,subs;


    public APIManagerService() {
        super("APIManagerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        subs=new ArrayList<>();
        String user="";
        String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday"};
        int mode=0;
        String[] times;
        String URL = "http://10.0.0.109/~rahulzoldyck/timetable";

        all=new ArrayList<>();
        handler=new MySqlHandler(this,null);
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
//                    tt.add(new BasicNameValuePair("username", user));
                    // aut.add(new BasicNameValuePair("password",params[1]));
                    JSONObject js = jp.makeHttpRequest(URL, "POST", tt);
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
                            handler.add_sub(times[k]);
                        Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
                        Log.d("hel","api manager: "+times[0]+" "+times[5]);
                        handler.add_day(times[0],times[1],times[2],times[3],times[4],times[5],times[6],times[7],times[8]);
                    }

                    Log.d("TAG", js.toString());
                    Log.d("TAG", js.toString());

                } catch (JSONException e) {
                e.printStackTrace();
                }

                break;
            case 1:



        }
    }

}

