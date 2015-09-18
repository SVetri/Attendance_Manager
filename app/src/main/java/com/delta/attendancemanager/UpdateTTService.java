package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
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
import java.util.List;


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
            }
        }
    }

 //TODO: API console
    private void handleUpcoming(JSONObject json)  throws JSONException, IOException{
        JSONObject result=new JSONObject();
        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost("");



        Log.i("ulala", json.toString());
        JSONObject js=new JSONObject();
        js.put("batch", "110114070");
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
//        JSONObject js=jp.makeHttpRequest(MainActivity.URL,"POST",ut);

    }

    private void handleTT(JSONObject json) throws JSONException, IOException {
        JSONObject result=new JSONObject();
        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost("");



        Log.i("ulala", json.toString());
       JSONObject js=new JSONObject();
        js.put("batch", "110114070");
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
