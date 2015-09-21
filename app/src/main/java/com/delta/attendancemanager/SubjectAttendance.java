package com.delta.attendancemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        TextView percentrequired = (TextView) findViewById(R.id.requiredpercent);                       //not touched as of now.
        TextView projectedpercent = (TextView) findViewById(R.id.projectedatt);                         //not touched as of now.

        final String subname = getIntent().getStringExtra("subname");
        subjectname.setText(subname);
        atAdapter.subject_info(subname);

        int ca = atAdapter.getClasses_attended();
        int tc = atAdapter.getTotalclasses();

        classes.setText(String.valueOf(ca)+" / "+String.valueOf(tc));
        int percent = 0;
        if(tc!=0)
            percent = ca*100 / tc;
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
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
