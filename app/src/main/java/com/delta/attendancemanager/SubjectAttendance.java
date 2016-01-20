package com.delta.attendancemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubjectAttendance extends ActionBarActivity {

    AtAdapter atAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        atAdapter = new AtAdapter(getApplicationContext());
        setContentView(R.layout.activity_subject_attendance);

        Button edithistory = (Button) findViewById(R.id.edithistory);
        TextView updnotice = (TextView) findViewById(R.id.updatenotice);
        TextView subjectname = (TextView) findViewById(R.id.subjectname);
        TextView classes = (TextView) findViewById(R.id.classes);
        TextView attendancepercent = (TextView) findViewById(R.id.attendancepercent);
        TextView totalclasses = (TextView) findViewById(R.id.totalclasses);

        final String subname = getIntent().getStringExtra("subname");
        subjectname.setText(subname);
        atAdapter.subject_info(subname);

        int ca = atAdapter.getClasses_attended();
        int tc = atAdapter.getTotalclasses();

        classes.setText(String.valueOf(ca));
        totalclasses.setText(String.valueOf(tc));
        int percent = 0;
        if(tc!=0)
            percent = ca*100 / tc;
        else
            percent = 100;
        attendancepercent.setText(String.valueOf(percent)+"%");

        Boolean dispnotice = getIntent().getBooleanExtra("pendupd", false);

        if(dispnotice.equals(true))
        {
            updnotice.setVisibility(View.VISIBLE);
        }
        else
        {
            updnotice.setVisibility(View.GONE);
        }
        edithistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubjectAttendance.this, EditHistory.class);
                i.putExtra("sname",subname);
                startActivity(i);
                finish();
            }
        });
    }

}
