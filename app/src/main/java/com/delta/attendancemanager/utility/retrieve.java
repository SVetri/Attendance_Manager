package com.delta.attendancemanager.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.delta.attendancemanager.DisplayToast;
import com.delta.attendancemanager.MainActivity;
import com.delta.attendancemanager.adapters.AtAdapter;

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
public class retrieve extends AsyncTask<Void, Void, Boolean> {
    ProgressDialog dialog;
    ActionBarActivity activity;
    public static final String RNO = "rno";
    Handler toasthandler;
    Context cont;

    public retrieve(ActionBarActivity act, Context context) {
        this.activity = act;
        dialog = new ProgressDialog(act);
        cont = context;
        toasthandler = new Handler();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setCancelable(false);
        dialog.setMessage("Retrieveing Attendance");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        JSONArray result;
        JSONObject js = new JSONObject();
        AtAdapter atAdapter = new AtAdapter(cont);
        SharedPreferences prefs = cont.getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO, "default");
        ArrayList<String> subjects = new ArrayList<>(), datetime = new ArrayList<>();
        ArrayList<Integer> present = new ArrayList<>();
        try {
            js.put("rollno", rollno);
            Log.i("hel", js.toString());
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(MainActivity.URL + "/attendance");
            StringEntity s = new StringEntity(js.toString());
            httpPost.setEntity(s);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            String jsons = "";
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
                return false;
            }
            try {
                result = new JSONArray(jsons);
                for (int i = 0; i < result.length(); i++) {
                    JSONObject temp = result.getJSONObject(i);
                    String subject = temp.getString("subject");
                    String dt = temp.getString("date-time");
                    int pres = temp.getInt("present");
                    atAdapter.add_attendance(subject, dt, pres);
                }
                return true;
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
            toasthandler.post(new DisplayToast(cont, "Attendance up-to-date"));
        } else {
            toasthandler.post(new DisplayToast(cont, "Error retrieving Attendance! Try Again later!"));
        }
    }
}
