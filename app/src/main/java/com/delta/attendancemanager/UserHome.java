package com.delta.attendancemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class UserHome extends ActionBarActivity {
    public String username;
    MySqlHandler handler;
    List<String[]> alls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Bundle b=getIntent().getExtras();
        username="";
        if(b!=null){
            username=b.getString("rno");
        }
        handler=new MySqlHandler(this,null);
        alls=new ArrayList<>();
        alls=handler.get_days();
        if(alls.size()==0){
            Intent i=new Intent(this,APIManagerService.class);
            i.putExtra("rno",username);
            i.putExtra("mode",0);
            startService(i);
        }

        Button weeklytt = (Button) findViewById(R.id.weeklytt);
        Button tomtt = (Button) findViewById(R.id.tomtt);
        Button manageatt = (Button) findViewById(R.id.manage);
        Button logout = (Button) findViewById(R.id.logoutuserhome);
        logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(UserHome.this,MainActivity.class));
                    }
                }
        );

        weeklytt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserHome.this, WeeklyTimetable.class);
                startActivity(i);
            }
        });

        manageatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserHome.this, ManageAttendance.class);
                startActivity(i);
            }
        });

        tomtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserHome.this, UpcomingTT.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
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
