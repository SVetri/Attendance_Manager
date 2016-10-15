//view my attendance option.
package com.delta.attendancemanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ViewMyAttendance extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_attendance);

        MySqlAdapter mySqlAdapter = new MySqlAdapter(getApplicationContext(),null);
        ArrayList<String> subjects = mySqlAdapter.getSubs();

        RecyclerView reclist = (RecyclerView) findViewById(R.id.usersubjectList);
        reclist.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reclist.setLayoutManager(llm);

        SubjectAdapter subadapter = new SubjectAdapter(createList(subjects), ViewMyAttendance.this);
        reclist.setAdapter(subadapter);
    }

    private List<SubjectInfo> createList(ArrayList<String> subjects) {

        List<SubjectInfo> result = new ArrayList<SubjectInfo>();

        for (int i=0; i < subjects.size(); i++) {
            SubjectInfo si = new SubjectInfo();
            if(subjects.get(i) == null || subjects.get(i).isEmpty() || subjects.get(i).equals(" "))
                continue;
            si.subjectname=subjects.get(i);
            result.add(si);
        }

        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_my_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_retrieve) {
            new retrieve(ViewMyAttendance.this,getApplicationContext()).execute();
            return true;
        }
        if(id == R.id.action_backup){
            new backup(ViewMyAttendance.this,getApplicationContext()).execute();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}