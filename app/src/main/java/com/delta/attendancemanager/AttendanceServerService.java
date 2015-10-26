package com.delta.attendancemanager;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *service that syncs attendance and retrieves it.
 */
public class AttendanceServerService extends IntentService {
    private static final String SYNC = "com.delta.attendancemanager.action.sync.attendance";
    private static final String RETRIEVE = "com.delta.attendancemanager.action.retrieve.attendance";
    private static final String ADD = "com.delta.attendancemanager.action.add.local.attendance";
    private static final String DELETE = "com.delta.attendancemanager.action.delete.local.attendance";
    public static final String RNO="rno";

    public static void syncAttendance(Context context) {
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(SYNC);
        context.startService(intent);
    }

    public static void addAttendance(Context context){
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(ADD);
        context.startService(intent);
    }

    public static void deleteAttendance(Context context){
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(DELETE);
        context.startService(intent);
    }

    public static void retrieveAttendance(Context context) {                                                //TODO: use it to retrieve attendnace whenever we start
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(RETRIEVE);
        context.startService(intent);
    }

    public AttendanceServerService() {
        super("AttendanceServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SYNC.equals(action)) {
                try {
                    handleSync();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else if(ADD.equals(action)) {
                handleAdd();
            } else if(DELETE.equals(action)) {
                handledelete();
            } else {
                if (RETRIEVE.equals(action)) {
                    try {
                        handleRetrieve();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleSync() throws IOException, JSONException {
        JSONObject result = new JSONObject();
        JSONObject js = new JSONObject();
        JSONArray jsarray = new JSONArray();
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        atAdapter.to_update_data();
        SharedPreferences prefs = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO, "default");
        ArrayList<String> subjects = atAdapter.getSubj(), datetime = atAdapter.getDt();
        ArrayList<Integer> present = atAdapter.getPresint();
        for(int i=0;i<subjects.size();i++){
            js = new JSONObject();
            js.put("rollno",rollno);
            js.put("date-time",datetime.get(i));
            js.put("subject",subjects.get(i));
            js.put("present",present.get(i));
            jsarray.put(js);
        }
        Log.i("hel",jsarray.toString());
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(MainActivity.URL+"/backup");
        StringEntity s=new StringEntity(jsarray.toString());
        httpPost.setEntity(s);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpclient.execute(httpPost);
        String jsons="";
        InputStream is = httpResponse.getEntity().getContent();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            jsons = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
Log.i("hel",jsons);
        // try parse the string to a JSON object
        try {
            result = new JSONObject(jsons);
            if(result.getInt("BackedUp")==1)
                Log.d("hel","success");
            else{
                Log.d("hel", "failed");
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    }

    private void handleAdd(){
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        MySqlAdapter helper = new MySqlAdapter(getApplicationContext(),null);
        String[] subjects = helper.get_tomo();
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        for(int i=1;i<=8;i++){
            Date date = new Date(now.get(Calendar.YEAR)-1900, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),TTimings.hour[i], TTimings.min[i]);                                                                  //1900+yyyy;      TODO: check whther the normal date is working or change it to 1900+yyyy.
            atAdapter.add_attendance(subjects[i], sdf.format(date), 0);
        }
    }

    private void handledelete(){
        MySqlAdapter helper = new MySqlAdapter(getApplicationContext(),null);
        String[] subjects = helper.get_tomo();
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        for(int i=1;i<=8;i++){
            Date date = new Date(now.get(Calendar.YEAR)-1900, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),TTimings.hour[i], TTimings.min[i]);                                                                  //1900+yyyy;      TODO: check whther the normal date is working or change it to 1900+yyyy.
            atAdapter.delete_data(subjects[i],sdf.format(date));
        }
    }

    private void handleRetrieve() throws JSONException, IOException {
        JSONArray result;
        JSONObject js = new JSONObject();
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO, "default");
        ArrayList<String> subjects = new ArrayList<>(), datetime = new ArrayList<>();
        ArrayList<Integer> present = new ArrayList<>();
        js.put("rollno",rollno);
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,MainActivity.URL+"/backup",js,new Response.Listener<JSONArray>(){
//            @Override
//            public void onResponse(JSONArray jsonArray) {
//
//            }
//        }, new Response.ErrorListener(){
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        }
//        );
        Log.i("hel",js.toString());
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(MainActivity.URL+"/backup");
        StringEntity s=new StringEntity(js.toString());
        httpPost.setEntity(s);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpclient.execute(httpPost);
        String jsons="";
        InputStream is = httpResponse.getEntity().getContent();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            jsons = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        Log.i("hel",jsons);
        // try parse the string to a JSON object
        try {
            result = new JSONArray(jsons);
            for(int i=0;i<result.length();i++){
                JSONObject temp = result.getJSONObject(i);
                String subject = temp.getString("subject");
                String dt = temp.getString("date-time");
                int pres = temp.getInt("present");
                atAdapter.add_attendance(subject,dt,pres);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    }

}
