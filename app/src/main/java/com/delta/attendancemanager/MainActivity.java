package com.delta.attendancemanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import org.json.JSONObject;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    Context applicationContext = MainActivity.this;
    public static final String URL = "https://4049c3fb.ngrok.com";
    public static final String RNO = "rno";
    static boolean wrong = false;
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

        handler = new MySqlAdapter(this, null);
        if (handler.get_days().size() == 0) {
            isfirst = true;
        } else
            isfirst = false;
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.passwordm);
        if (!rollno.equals("default")) {
            Intent i = new Intent(MainActivity.this, Userhome.class);
            i.putExtra("rno", rollno);
            startActivity(i);
            finish();
        }
        if (wrong) {
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
                    String user = username.getText().toString();
                    if (checkpref(user)) {
                        ParsePush.subscribeInBackground("nlr" + usernme.substring(0, Math.min(6, usernme.length())));
                        Log.i("parse init", "nlr" + usernme.substring(0, Math.min(6, usernme.length())));
                        startActivity(new Intent(MainActivity.this, Userhome.class));
                        finish();

                        List<String> subscribedchannels = ParseInstallation.getCurrentInstallation().getList("channels");
                        for (int i = 0; i < subscribedchannels.size(); i++) {
                            Log.d("Parse channel", subscribedchannels.get(i));
                        }   //TODO just to check delete this

                    } else {
                        usernme = username.getText().toString();
                        pass = password.getText().toString();
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
                startActivity(new Intent(applicationContext, CRLogin.class));
            }
        });

    }

    private boolean checkpref(String user) {
        SharedPreferences share = getSharedPreferences("user", Context.MODE_PRIVATE);
        String rno = share.getString("rno", "-1");
        if (rno.equals("-1"))
            return false;
        else if (user.equals(rno))
            return true;
        else
            return false;


    }

    class Authenticate extends AsyncTask<String, Void, Boolean> {
        final String TAG = "JsonParser.java";


        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser jp = new JSONParser();

            try {
                JSONObject js = new JSONObject();

                js.put("username", params[0]);
                js.put("password", params[1]);
                JSONObject jd = jp.makeHttpRequest(URL + "/login", "POST", js);
                Log.i(TAG, js.toString());
                int success = jd.getInt("logged_in");
                return success == 1;                                                //authentication
            } catch (Exception e) {
                e.printStackTrace();

            }


            return false;
        }

        //  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                SharedPreferences share1 = getSharedPreferences("user", Context.MODE_PRIVATE);
                String rno = share1.getString(RNO, ":)");
                SharedPreferences share = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                editor.putString(RNO, usernme);
                editor.putString("pass", pass);
                editor.commit();
                ParsePush.subscribeInBackground("nlr" + usernme.substring(0, Math.min(6, usernme.length())));
                Log.i("parse init", "nlr" + usernme.substring(0, Math.min(6, usernme.length())));
                Intent i = new Intent(MainActivity.this, Userhome.class);
                i.putExtra("rno", usernme);

                List<String> subscribedchannels = ParseInstallation.getCurrentInstallation().getList("channels");
                for (int j = 0; j < subscribedchannels.size(); j++) {
                    Log.d("Parse channel", subscribedchannels.get(j));
                }   //TODO just to check delete this


                AttendanceServerService.retrieveAttendance(getApplicationContext());
                AlarmService.cancelAlarm(getApplicationContext());
                AlarmService.startActionSetDefaultAlarm(getApplicationContext());
                ComponentName receiver = new ComponentName(getApplicationContext(), BootReceiver.class);
                PackageManager pm = getApplicationContext().getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                startActivity(i);
                finish();
            } else {
                wrongpassword();
            }
        }
    }

    private void wrongpassword() {
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        i.putExtra("wrong", true);
        startActivity(i);  //TODO: enhance with textview "Wrong password"

    }

}