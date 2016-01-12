package com.delta.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParsePush;

import java.util.ArrayList;
import java.util.List;

public class Userhome extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(Userhome.this,AlarmReceiver.class);
        startService(intent);
        setContentView(R.layout.activity_userhome);
        List<String[]> alls;
        alls=new ArrayList<>();
        MySqlAdapter handler=new MySqlAdapter(this,null);
        alls=handler.get_days();
        if(alls.size()==0){
//            Intent i=new Intent(this,AlarmSetterService.class);
//            startService(i);
            SharedPreferences share=getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = share.getString("rno","");
//            Intent j=new Intent(this,APIManagerService.class);
//            j.putExtra("rno",username);
//            j.putExtra("mode",0);
//            startService(j);
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            UserHomeSliderFragment fragment = new UserHomeSliderFragment();
            transaction.replace(R.id.fragment_content3, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userhome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_crlogin){
            Intent i = new Intent(Userhome.this,CRLogin.class);
            startActivity(i);
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_logout) {
            Intent i =new Intent(Userhome.this,MainActivity.class);
            SharedPreferences share=getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = share.getString("rno", "");
            SharedPreferences.Editor editor=share.edit();
            ParsePush.unsubscribeInBackground("nlr" + username.substring(0, Math.min(6, username.length())));
            Log.i("parse init", "nlr" + username.substring(0, Math.min(6, username.length())) + " unsubscribed");
            editor.putString(MainActivity.RNO, "default");
            editor.putString("pass","");
            editor.commit();
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}