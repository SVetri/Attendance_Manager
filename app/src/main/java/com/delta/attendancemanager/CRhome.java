package com.delta.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the view after the CRLogin
 */
public class CRhome extends ActionBarActivity {
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crhome2);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            page = bundle.getInt("page");
        } else {
            page = 1;
        }
//        AlarmService.startActionSetDefaultAlarm(getApplicationContext());
        List<String[]> alls;
        alls = new ArrayList<>();
        MySqlAdapter handler = new MySqlAdapter(this, null);
        alls = handler.get_days();
        if (alls.size() == 0) {
//            Intent i=new Intent(this,AlarmSetterService.class);
//            startService(i);
            SharedPreferences share = getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = share.getString("rno", "");
//            Intent j=new Intent(this,APIManagerService.class);
//            j.putExtra("rno",username);
//            j.putExtra("mode",0);
//            startService(j);
        }
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CRhomesliderfragment fragment = new CRhomesliderfragment();
            Bundle b = new Bundle();
            b.putInt("page", page);
            fragment.setArguments(b);
            transaction.replace(R.id.fragment_content2, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crlogout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_POST) {
            Intent i = new Intent(this, Userhome.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

