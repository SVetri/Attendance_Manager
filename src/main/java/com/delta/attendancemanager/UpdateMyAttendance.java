package com.delta.attendancemanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class UpdateMyAttendance extends ActionBarActivity {
    ArrayList<String> subjects;
    ArrayList<Date> dates;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_attendance);

        AtAdapter atAdapter = new AtAdapter(getApplicationContext());

        atAdapter.fetch_pending_data();

        ArrayList<String> subjects, datetime;
        subjects = atAdapter.getSubj();
        datetime = atAdapter.getDt();

        RecyclerView reclist = (RecyclerView) findViewById(R.id.cardList);
        reclist.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reclist.setLayoutManager(llm);
        
        AttendanceAdapter attadapter = new AttendanceAdapter(createList(subjects,datetime),getApplicationContext());
        reclist.setAdapter(attadapter);
    }

    private List<CardInfo> createList(ArrayList<String> subject,ArrayList<String> datetime) {
        CardInfo ci;
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (int i=0; i < subject.size(); i++) {
            ci = new CardInfo();
            ci.coursename = subject.get(i);
            ci.classdate = formatDate(datetime.get(i));
            ci.classtime = formatHour(datetime.get(i));
            result.add(ci);

        }

        return result;
    }

    private String formatDate(String fullDate){
        String year = fullDate.substring(0, 4);
        String month = fullDate.substring(5, 7);
        String day = fullDate.substring(8, 10);
        String out = day + "/"+ month + "/" + year;
        return out;
    }

    private String formatHour(String fullDate){
        String entire = fullDate.substring(11);
        return entire;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_my_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_dummy) {
            AttendanceServerService.addAttendance(this);
            Toast.makeText(getApplicationContext(),"Today's attendance added",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
