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

public class CRhome extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crhome2);

        List<String[]> alls;
        alls=new ArrayList<>();
        MySqlAdapter handler=new MySqlAdapter(this,null);
        alls=handler.get_days();
        if(alls.size()==0){
//            Intent i=new Intent(this,AlarmSetterService.class);
//            startService(i);
            SharedPreferences share=getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = share.getString("rno","");
            Intent j=new Intent(this,APIManagerService.class);
            j.putExtra("rno",username);
            j.putExtra("mode",0);
            startService(j);
        }
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CRhomesliderfragment fragment = new CRhomesliderfragment();
            transaction.replace(R.id.fragment_content2, fragment);
            transaction.commit();
        }
    }
    }

