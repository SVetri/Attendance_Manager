package com.delta.attendancemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
// SCREEN WHICH GUIDES U TO VIEW MY ATTENDANCE AND MANAGE MY ATTENDANCE

/**
 * Handle the view to mange the attendances
 */
public class ManageAttendance extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_attendance);

        Button updateatt = (Button) findViewById(R.id.updatemyatt);
        Button viewatt = (Button) findViewById(R.id.viewmyatt);

        updateatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ManageAttendance.this, UpdateMyAttendance.class);
                startActivity(i);

            }
        });


        viewatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ManageAttendance.this, ViewMyAttendance.class);
                startActivity(i);

            }
        });
    }
}
