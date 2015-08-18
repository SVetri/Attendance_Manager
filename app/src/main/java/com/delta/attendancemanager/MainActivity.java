package com.delta.attendancemanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public static final String URL="http://10.0.0.109/~rahulzoldyck/login.php";

    String usernme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.passwordm);
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
                    String pass=password.getText().toString();
                    Log.d("TAG",user+pass);
                    Authenticate a=new Authenticate();
                    a.execute(user,pass);
                }
            }
        });

        crswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CRLogin.class);
                startActivity(i);
                finish();
            }
        });

    }


    class Authenticate extends AsyncTask<String,Void,Boolean>{
        final String TAG = "JsonParser.java";



        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser jp=new JSONParser();
            try {
                List<NameValuePair> aut=new ArrayList<>();
                aut.add(new BasicNameValuePair("username",params[0]));
                aut.add(new BasicNameValuePair("password",params[1]));
                JSONObject js=jp.makeHttpRequest(URL,"POST",aut);
                Log.d("TAG",js.toString());
                Log.d("TAG",js.toString());


               int success=js.getInt("success");
                jp=null;
                js=null;
                return success==1;
            }  catch (JSONException e) {
                e.printStackTrace();
            }

           return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Intent i = new Intent(MainActivity.this, UserHome.class);
               i.putExtra("rno",Integer.parseInt(usernme));
                startActivity(i);
                finish();
            }
            else{
                wrongpassword();
            }
        }
    }

    private void wrongpassword() {
        startActivity(new Intent(MainActivity.this,MainActivity.class));  //TODO: enhance with textview "Wrong password"

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
