package com.delta.attendancemanager;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CRhome extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crhome2);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CRhomesliderfragment fragment = new CRhomesliderfragment();
            transaction.replace(R.id.fragment_content2, fragment);
            transaction.commit();
        }
    }
    }

