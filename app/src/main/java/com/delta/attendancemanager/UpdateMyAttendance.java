package com.delta.attendancemanager;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

        Date date=null;
        List<CardInfo> result = new ArrayList<CardInfo>();
        for (int i=0; i < subject.size(); i++) {
            CardInfo ci = new CardInfo();
            ci.coursename = subject.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try{
                date = sdf.parse(datetime.get(i));
            }
            catch(Exception e){
                Log.d("hel",e.toString());
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");                      //do not change the time format here. giving error in database. change it while displaying if necessary.
            SimpleDateFormat sdf2 = new SimpleDateFormat("h:m");
            ci.classdate = sdf1.format(date);
            ci.classtime = sdf2.format(date);

            result.add(ci);

        }

        return result;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
