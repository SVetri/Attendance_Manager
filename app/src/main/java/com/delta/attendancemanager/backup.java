package com.delta.attendancemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

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
import java.util.ArrayList;

/**
 * Created by lakshmanaram on 14/1/16.
 */
public class backup extends AsyncTask<Void, Void, Boolean> {
    ProgressDialog dialog;
    ActionBarActivity activity;
    public static final String RNO = "rno";
    Handler toasthandler;
    Context cont;

    public backup(ActionBarActivity act, Context context) {
        this.activity = act;
        dialog = new ProgressDialog(act);
        cont = context;
        toasthandler = new Handler();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setCancelable(false);
        dialog.setMessage("Syncing Attendance");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        JSONObject result = new JSONObject();
        JSONObject js = new JSONObject();
        JSONArray jsarray = new JSONArray();
        AtAdapter atAdapter = new AtAdapter(cont.getApplicationContext());
        atAdapter.to_update_data();
        SharedPreferences prefs = cont.getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO,"default");
        ArrayList<String> subjects = atAdapter.getSubj(), datetime = atAdapter.getDt();
        ArrayList<Integer> present = atAdapter.getPresint();
        try {
            for(int i=0;i<subjects.size();i++){
                js = new JSONObject();
                js.put("rollno",rollno);
                js.put("date-time",datetime.get(i));
                js.put("subject",subjects.get(i));
                js.put("present",present.get(i));
                jsarray.put(js);
            }
            Log.i("sending something?", jsarray.toString());
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
            try {
                result = new JSONObject(jsons);
                if(result.getInt("BackedUp")==1) {
                    return true;
                }
                else{
                    return false;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (aBoolean) {
            toasthandler.post(new DisplayToast(cont, "Attendance successfully backed up"));
        } else {
            toasthandler.post(new DisplayToast(cont, "Error syncing Attendance! Try Again later!"));
        }
    }
}
