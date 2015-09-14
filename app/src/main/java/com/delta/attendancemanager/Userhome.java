package com.delta.attendancemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Userhome extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        List<String[]> alls;
        alls=new ArrayList<>();
        MySqlAdapter handler=new MySqlAdapter(this,null);
        alls=handler.get_days();
        if(alls.size()==0){
            SharedPreferences share=getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = share.getString("rno","");
            Intent i=new Intent(this,APIManagerService.class);
            i.putExtra("rno",username);
            i.putExtra("mode",0);
            startService(i);
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            UserHomeSliderFragment fragment = new UserHomeSliderFragment();
            transaction.replace(R.id.fragment_content3, fragment);
            transaction.commit();
        }
    }
}