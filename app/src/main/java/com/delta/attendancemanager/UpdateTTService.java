package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class UpdateTTService extends IntentService {
    private static final String ACTION_UPCOMING = "com.delta.attendancemanager.action.UPCOMING";
    private static final String ACTION_CHAT = "com.delta.attendancemanager.action.CHAT";
    private static final String ACTION_TT = "com.delta.attendancemanager.action.TT";
    private static final String MSG = "com.delta.attendancemanager.extra.MSG";
    private static final String JSON = "com.delta.attendancemanager.extra.JSON";

    public static void startActionChat(Context context, String msg){
        Intent intent = new Intent(context, UpdateTTService.class);
        intent.setAction(ACTION_CHAT);
        intent.putExtra(MSG, msg);
        context.startService(intent);
    }


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
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            } else if (ACTION_TT.equals(action)) {
                try {
                    final JSONObject json = new JSONObject(intent.getStringExtra(JSON));
                    handleTT(json);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }else if (ACTION_CHAT.equals(action)){
                try {
                    handlechat(intent.getStringExtra(MSG));
                } catch (JSONException  | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handlechat(String msg) throws JSONException ,IOException {
        SharedPreferences share1=getSharedPreferences("user", Context.MODE_PRIVATE);
        String rno=share1.getString(MainActivity.RNO,":)");
        String batch = rno.substring(0,rno.length()-3);
        JSONObject data=new JSONObject();
        Calendar cal=Calendar.getInstance();
        int h=cal.get(Calendar.HOUR_OF_DAY);
        int min=cal.get(Calendar.MINUTE);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int mon=cal.get(Calendar.MONTH)+1;
        int yy=cal.get(Calendar.YEAR);
        String time= String.valueOf(h)+":"+String.valueOf(min);
        String date=String.valueOf(day)+"/"+String.valueOf(mon)+"/"+String.valueOf(yy);
        data.put("msg",msg);
        data.put("date",date);
        data.put("time",time);
        JSONObject jb=new JSONObject();
        jb.put("an",data);
        JSONObject js =new JSONObject();
        js.put("data",jb);
        js.put("type","chat");
        js.put("batch",batch);
        JSONObject result;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(MainActivity.URL+"/updateTT");
        StringEntity s=new StringEntity(js.toString());
        httpPost.setEntity(s);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpclient.execute(httpPost);
        String jsons="";
        // 9. receive response as inputStream
        InputStream is = httpResponse.getEntity().getContent();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            jsons = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            result = new JSONObject(jsons);
            Log.i("connectionscheck",result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    }

    //TODO: API console
    private void handleUpcoming(JSONObject json)  throws JSONException, IOException{
        SharedPreferences share1=getSharedPreferences("user", Context.MODE_PRIVATE);
        String rno=share1.getString(MainActivity.RNO,":)");
        String batch = rno.substring(0,rno.length()-3);
        JSONObject result=new JSONObject();
        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost(MainActivity.URL+"/updateTT");



        Log.i("ulala", json.toString());
        JSONObject js=new JSONObject();
        js.put("batch", batch);
        JSONObject ex=new JSONObject();
        ex.put("data", json);
        js.put("data",ex);
        js.put("type","ut");
        StringEntity s=new StringEntity(js.toString());
        httpPost.setEntity(s);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpclient.execute(httpPost);
        String jsons="";
        // 9. receive response as inputStream
        InputStream is = httpResponse.getEntity().getContent();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            jsons = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            result = new JSONObject(jsons);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
//        JSONObject js=jp.makeHttpRequest(MainActivity.URL,"POST",ut);

    }

    private void handleTT(JSONObject json) throws JSONException, IOException {
        SharedPreferences share1=getSharedPreferences("user", Context.MODE_PRIVATE);
        String rno=share1.getString(MainActivity.RNO,":)");
        String batch = rno.substring(0,rno.length()-3);
        JSONObject result=new JSONObject();
        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost(MainActivity.URL+"/setTimeTable");



        Log.i("ulala", json.toString());
       JSONObject js=new JSONObject();
        js.put("batch", batch);
        js.put("data", json);

        StringEntity s=new StringEntity(js.toString());
        httpPost.setEntity(s);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpclient.execute(httpPost);
        String jsons="";
        // 9. receive response as inputStream
        InputStream is = httpResponse.getEntity().getContent();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            jsons = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            result = new JSONObject(jsons);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
