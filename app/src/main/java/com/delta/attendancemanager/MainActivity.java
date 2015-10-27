package com.delta.attendancemanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    Context applicationContext=MainActivity.this;
    public static final String URL="http://sprocky.ddns.net:1501";
    public static final String GOOGLE_PROJ_ID="275730371821";
    String regId="";
    public static final String REG_ID="REG-ID";
    public static final String RNO="rno";
    static boolean wrong=false;

    GoogleCloudMessaging gcmObj;
    MySqlAdapter handler;
    String usernme;
    String pass;
    static boolean isfirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            boolean g = b.getBoolean("wrong");
            if (g)
                wrong = true;

        }
        SharedPreferences prefs = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO, "default");

        handler=new MySqlAdapter(this,null);
        if(handler.get_days()==null){
            isfirst=true;
        }
        else
            isfirst=false;
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.passwordm);
        if(!rollno.equals("default")){
            username.setText(rollno);
            password.setText("password");
        }
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
                    if(checkpref(user)){
                        startActivity(new Intent(MainActivity.this,Userhome.class));
                        finish();
                    }
                    else{
                        usernme=username.getText().toString();
                        pass=password.getText().toString();
                        Log.d("TAG", user + pass);
                        Authenticate a = new Authenticate();
                        a.execute(usernme, pass);
                    }
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

    private boolean checkpref(String user) {
        SharedPreferences share=getSharedPreferences("user",Context.MODE_PRIVATE);
        String rno=share.getString("rno","-1");
        if(rno.equals("-1"))
            return false;
        else if(user.equals(rno))
            return true;
        else
            return false;


    }

    private void InitialHandShake(String user) {
        startService(new Intent(this,APIManagerService.class));
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
                        Log.i("came here","dgyc");
                    }
                    regId = gcmObj
                            .register(GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;
                    Log.d("fbj",regId);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
//                List<NameValuePair> aut=new ArrayList<>();
                JSONObject js=new JSONObject();


                try {
                    js.put("rollnumber",rollnumber);
                    js.put("regno", regId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject jd=jp.makeHttpRequest(URL+"/register","POST",js);
//                Log.i("Json",js.toString());
                try {
                    int success=jd.getInt("Signed Up");
                    if(success!=1){
                        wrongpassword();
                    }
                } catch (JSONException e) {
                    msg = "Error :" + e.getMessage();
                }
                return msg;
            }

          //  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    // Store RegId created by GCM Server in SharedPref
                    storeRegIdinSharedPref(applicationContext, regId, rollnumber);
                    Toast.makeText(
                            applicationContext,
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();
                    SharedPreferences share=getSharedPreferences("user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=share.edit();
                    editor.putString("rno", usernme);
                    editor.commit();
                    Intent i = new Intent(MainActivity.this, Userhome.class);
                    i.putExtra("rno", usernme);
                    startActivity(i);
                    finish();
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
        SharedPreferences prefs = getSharedPreferences("user",
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

            try {
               JSONObject js=new JSONObject();

                js.put("username",params[0]);
                js.put("password", params[1]);
                JSONObject jd=jp.makeHttpRequest(URL+"/login","POST",js);
                Log.i(TAG,js.toString());
                int success=jd.getInt("logged_in");
                return success==1;                                                //authentication
            }  catch (Exception e) {
                e.printStackTrace();

            }


            return false;
        }

      //  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                SharedPreferences share1=getSharedPreferences("user",Context.MODE_PRIVATE);
                String rno=share1.getString(RNO,":)");
                if(rno.equals(":)"))
                    InitialHandShake(usernme);
                else {
                    SharedPreferences share=getSharedPreferences("user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=share.edit();
                    editor.putString(RNO, usernme);
                    editor.putString("pass",pass);
                    editor.commit();
                    Intent i = new Intent(MainActivity.this, Userhome.class);
                    i.putExtra("rno", usernme);

                   startActivity(i);
                  finish();
                }

                AttendanceServerService.retrieveAttendance(getApplicationContext());
                AlarmService.cancelAlarm(getApplicationContext());
                AlarmService.startActionSetDefaultAlarm(getApplicationContext());
                ComponentName receiver = new ComponentName(getApplicationContext(), BootReceiver.class);
                PackageManager pm = getApplicationContext().getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
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
