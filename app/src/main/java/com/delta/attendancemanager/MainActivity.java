package com.delta.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    Context applicationContext=MainActivity.this;
    public static final String URL="http://10.0.0.109/~rahulzoldyck/login.php";
    public static final String GOOGLE_PROJ_ID="";
    String regId="";
    public static final String REG_ID="REG-ID";
    public static final String RNO="RNO";
    boolean wrong=false;

    GoogleCloudMessaging gcmObj;
    MySqlHandler handler;
    String usernme;
    String pass;
    boolean isfirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            boolean g=b.getBoolean("wrong");
            if(g)
            wrong=true;

        }
        handler=new MySqlHandler(this,null);
        if(handler.get_days()==null){
            isfirst=true;
        }
        else
            isfirst=false;
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.passwordm);
        if(wrong){
            YoYo.with(Techniques.Tada).duration(700).playOn(username);
            YoYo.with(Techniques.Tada).duration(700).playOn(password);
        }
        Button loginbutton = (Button) findViewById(R.id.login);
        Button crswitch = (Button) findViewById(R.id.crmodeswitch);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter a Roll.No", Toast.LENGTH_SHORT).show();
                } else if (password.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                } else {
                    String user=username.getText().toString();
                    usernme=username.getText().toString();
                    pass=password.getText().toString();
                    Log.d("TAG", user + pass);
                    Authenticate a = new Authenticate();
                    a.execute(usernme, pass);
                    finish();

                }
            }
        });

        crswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(applicationContext,CRLogin.class));
            }
        });

    }

    private void InitialHandShake(String user) {
        registerInBackground(user);


    }
    private void registerInBackground(final String rollnumber) {
        new AsyncTask<Void, Void, String>() {
            JSONParser jp;
            @Override
            protected String doInBackground(Void... params) {
                jp=new JSONParser();
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                List<NameValuePair> aut=new ArrayList<>();
                aut.add(new BasicNameValuePair("rollnumber",rollnumber));
                aut.add(new BasicNameValuePair("regno",regId));
                JSONObject js=jp.makeHttpRequest(URL,"POST",aut);
                Log.i("Json",js.toString());
                try {
                    int success=js.getInt("success");
                    if(success!=1){
                        wrongpassword();
                    }
                } catch (JSONException e) {
                    msg = "Error :" + e.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    // Store RegId created by GCM Server in SharedPref
                    storeRegIdinSharedPref(applicationContext, regId, rollnumber);
                    Toast.makeText(
                            applicationContext,
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
    private void storeRegIdinSharedPref(Context context, String regId,
                                        String rollnumber) {
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putString(RNO, rollnumber);
        editor.commit();


    }


    class Authenticate extends AsyncTask<String,Void,Boolean>{
        final String TAG = "JsonParser.java";



        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser jp=new JSONParser();
            /*
            try {
                List<NameValuePair> aut=new ArrayList<>();
                aut.add(new BasicNameValuePair("username",params[0]));
                aut.add(new BasicNameValuePair("password",params[1]));
                JSONObject js=jp.makeHttpRequest(URL,"POST",aut);
                Log.i(TAG,js.toString());
                int success=js.getInt("success");
                jp=null;
                js=null;
                return success==1;                                                //authentication
            }  catch (Exception e) {
                e.printStackTrace();
            }
            */

           return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                if(isfirst)
                    InitialHandShake(usernme);
                else {
                    Intent i = new Intent(MainActivity.this, Userhome.class);
                    i.putExtra("rno", Integer.parseInt(usernme));
                    startActivity(i);
                    finish();
                }
            }
            else{
                wrongpassword();
            }
        }
    }

    private void wrongpassword() {
        Intent i =new Intent(MainActivity.this,MainActivity.class);
        i.putExtra("wrong",true);
        startActivity(i);  //TODO: enhance with textview "Wrong password"

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
