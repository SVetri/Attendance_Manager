package com.delta.attendancemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Handle the view and the methods for the subjects view
 */
public class SubjectAttendance extends ActionBarActivity {

    AtAdapter atAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        atAdapter = new AtAdapter(getApplicationContext());
        setContentView(R.layout.activity_subject_attendance);

        Button edithistory = (Button) findViewById(R.id.edithistory);
        int [] subsInt = {R.id.updatenotice, R.id.subjectname, R.id.classes, R.id.attendancepercent, R.id.totalclasses};
        TextView text [] = new TextView[5];
        for (int i= 0; i < text.length; i++){
            text[i] = (TextView) findViewById(subsInt[i]);
        }

        final String subname = getIntent().getStringExtra("subname");
        text[1].setText(subname);
        atAdapter.subject_info(subname);

        int ca = atAdapter.getClasses_attended();
        int tc = atAdapter.getTotalclasses();

        text[2].setText(String.valueOf(ca));
        text[4].setText(String.valueOf(tc));
        int percent = 0;
        if(tc!=0)
            percent = ca*100 / tc;
        else
            percent = 100;
        text[3].setText(String.valueOf(percent)+"%");

        Boolean dispnotice = getIntent().getBooleanExtra("pendupd", false);

        if(dispnotice.booleanValue())
        {
            text[0].setVisibility(View.VISIBLE);
        }
        else
        {
            text[0].setVisibility(View.GONE);
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
