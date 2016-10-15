package com.delta.attendancemanager;

import android.app.ProgressDialog;
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

/**
 * Handle the lunch activity and its methods
 */
public class MainActivity extends ActionBarActivity {
    Context applicationContext = MainActivity.this;
    /**
     * Server Url
     */
    public static final String URL = "http://30d9e412.ngrok.com";
    /**
     * Rno String
     */
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
        checkBundle(b);

        SharedPreferences prefs = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String rollno = prefs.getString(RNO, "default");

        handler = setWeekDays();

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
                        List<String> subscribedchannels = ParseInstallation.getCurrentInstallation().getList("channels");
                        for (int i = 0; i < subscribedchannels.size(); i++) {
                            Log.d("Parse channel", subscribedchannels.get(i));
                        }
                        startActivity(new Intent(MainActivity.this, Userhome.class));
                        finish();

                        subscribedchannels = ParseInstallation.getCurrentInstallation().getList("channels");
                        for (int i = 0; i < subscribedchannels.size(); i++) {
                            Log.d("Parse channel", subscribedchannels.get(i));
                        }   //TODO just to check delete this

                    } else {
                        usernme = username.getText().toString();
                        pass = password.getText().toString();
                        String log = "user: " + user + " pwd: " + pass;
                        Log.d("TAG", log.replace('\n', '_').replace('\r', '_'));
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

    private void checkBundle (Bundle b) {
        if (b != null) {
            boolean g = b.getBoolean("wrong");
            if (g)
                wrong = true;

        }
    }

    /**
     * Set the week days for the Mysql Adapter
     * @return Mysql Adapter
     */
    private MySqlAdapter setWeekDays(){
        MySqlAdapter handler = new MySqlAdapter(this, null);
        if (handler.get_days().size() == 0) {
            handler.add_day("Monday", "", "", "", "", "", "", "", "");
            handler.add_day("Tuesday", "", "", "", "", "", "", "", "");
            handler.add_day("Wednesday", "", "", "", "", "", "", "", "");
            handler.add_day("Thursday", "", "", "", "", "", "", "", "");
            handler.add_day("Friday", "", "", "", "", "", "", "", "");
            handler.add_day("tomorrow", "", "", "", "", "", "", "", "");
            isfirst = true;
        } else
            isfirst = false;
        return handler;
    }

    /**
     * Check if a username has been stored into the preferences
     * @param user username to check if has been stored
     * @return true if found, false otherwise
     */
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

    /**
     * This class defines the authentication machanisms
     */
    protected class Authenticate extends AsyncTask<String, Void, Boolean> {
        final String TAG = "JsonParser.java";
        ProgressDialog dialog;

        /**
         * Handle the Authentication mechanisms
         */
        public Authenticate() {
            dialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setMessage("Logging in...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser jp = new JSONParser();

            try {
                JSONObject js = new JSONObject();

                js.put("username", params[0]);
                js.put("password", params[1]);
                JSONObject jd = jp.makeHttpRequest(URL + "/login", "POST", js);
                Log.i(TAG, js.toString());
                int success = 1; //jd.getInt("logged_in");
                return success == 1;                                                //authentication
            } catch (Exception e) {
                Log.e("MainActivity", e.toString());
                Toast.makeText(MainActivity.this, "Check Internet Connection and Try Again Later.", Toast.LENGTH_LONG).show();

            }


            return false;
        }

        //  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (dialog.isShowing())
                dialog.dismiss();
            if (aBoolean) {
                SharedPreferences share1 = getSharedPreferences("user", Context.MODE_PRIVATE);
                String rno = share1.getString(RNO, ":)");
                SharedPreferences share = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                editor.putString(RNO, usernme);
                editor.putString("pass", pass);
                startService(new Intent(MainActivity.this, APIManagerService.class));
                editor.apply();
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
        startActivity(i);

    }

}