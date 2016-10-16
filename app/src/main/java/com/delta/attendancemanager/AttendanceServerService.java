package com.delta.attendancemanager;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Handler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *service that syncs attendance and retrieves it.
 */
public class AttendanceServerService extends IntentService {
    /**
     * Provide the Sync action
     */
    private static final String SYNC = "com.delta.attendancemanager.action.sync.attendance";
    /**
     * Provides the retrieve action
     */
    private static final String RETRIEVE = "com.delta.attendancemanager.action.Retrieve.attendance";
    /**
     * Provides the add action
     */
    private static final String ADD = "com.delta.attendancemanager.action.add.local.attendance";
    /**
     * Provide the delete action
     */
    private static final String DELETE = "com.delta.attendancemanager.action.delete.local.attendance";
    /**
     * String rno
     */
    public static final String RNO="rno";
    Handler toasthandler;

    /**
     * Syncs the the attendances with the server
     * @param context where it should be applied
     */
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

    public static void retrieveAttendance(Context context) {
        Intent intent = new Intent(context, AttendanceServerService.class);
        intent.setAction(RETRIEVE);
        context.startService(intent);
    }

    public AttendanceServerService() {
        super("AttendanceServerService");
        toasthandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SYNC.equals(action)) {
                getSync();
            } else if(ADD.equals(action)) {
                handleAdd();
            } else if(DELETE.equals(action)) {
                handledelete();
            } else {
                if (RETRIEVE.equals(action)) {
                    getRetrieve();
                }
            }
        }
    }

    private void getSync(){
        try {
            handleSync();
        } catch (IOException | JSONException e) {
            Log.e("AttendanceServerService", e.toString());
        }
    }

    /**
     * Get the retrieve operations
     */
    private void getRetrieve(){
        try {
            handleRetrieve();
        } catch (JSONException | IOException e) {
            Log.e("AttendanceServerService", e.toString());
        }
    }

    /**
     * Sync the attendace server with the current thread
     * @throws IOException
     * @throws JSONException
     */
    private void handleSync() throws IOException, JSONException {

        Log.i("in AttendanceServer","handleSync() called");
        JSONObject result = new JSONObject();
        JSONObject js = new JSONObject();
        JSONArray jsarray = new JSONArray();
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        atAdapter.to_update_data();
        SharedPreferences prefs = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO,"default");
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
        Log.i("sending something?",jsarray.toString());
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(MainActivity.URL+"/Backup");
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
        try {
            result = new JSONObject(jsons);
            if(result.getInt("BackedUp")==1) {
                Log.d("hel", "success");
                toasthandler.post(new DisplayToast(this, "Attendance successfully backed up"));
            }
            else{
                Log.d("hel", "failed");
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    }

    private void handleAdd(){
        Log.i("in AttendanceServer","handleAdd() called");
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        MySqlAdapter helper = new MySqlAdapter(getApplicationContext(),null);
        String[] subjects = helper.get_tomo();
        Calendar now = Calendar.getInstance();
        if(now.get(Calendar.HOUR_OF_DAY)>15)
            now.add(Calendar.DAY_OF_MONTH,1);
        for(int i=1;i<=8;i++){
            LocalDate ld = new LocalDate();
            String shour = "";
            String smin = "";
            if (TTimings.hour[i] < 10) {
                shour = "0" + TTimings.hour[i];
            } else {
                shour = shour.concat(String.valueOf(TTimings.hour[i]));
            }

            if (TTimings.min[i] < 10) {
                smin = "0" + TTimings.min[i];
            } else {
                smin = smin.concat(String.valueOf(TTimings.min[i]));
            }
            String f = ld + " " + shour + ":" + smin;
            atAdapter.add_attendance(subjects[i], f, 0);
        }
    }

    private void handledelete(){
        Log.i("in AttendanceServer","handleDelete() called");
        MySqlAdapter helper = new MySqlAdapter(getApplicationContext(),null);
        String[] subjects = helper.get_tomo();
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        Calendar now = Calendar.getInstance();
        if(now.get(Calendar.HOUR_OF_DAY)>15)
            now.add(Calendar.DAY_OF_MONTH,1);
        for(int i=1;i<=8;i++){
            LocalDate ld = new LocalDate();
            String shour = "";
            String smin = "";
            if (TTimings.hour[i] < 10) {
                shour = "0" + TTimings.hour[i];
            } else {
                shour = shour.concat(String.valueOf(TTimings.hour[i]));
            }

            if (TTimings.min[i] < 10) {
                smin = "0" + TTimings.min[i];
            } else {
                smin = smin.concat(String.valueOf(TTimings.min[i]));
            }
            String f = ld + " " + shour + ":" + smin;
            atAdapter.refresh_delete_data(subjects[i], f);
        }
    }

    private void handleRetrieve() throws JSONException, IOException {
        Log.i("in AttendanceServer", "handleRetrieve() called");
        JSONArray result;
        JSONObject js = new JSONObject();
        AtAdapter atAdapter = new AtAdapter(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO, "default");
        js.put("rollno",rollno);
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,MainActivity.URL+"/Backup",js,new Response.Listener<JSONArray>(){
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
        HttpPost httpPost = new HttpPost(MainActivity.URL+"/attendance");
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
                atAdapter.add_attendance(subject, dt, pres);
                toasthandler.post(new DisplayToast(this, "Attendance up-to-date"));
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    }

}
