package com.delta.attendancemanager;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class EditUpcomingTT extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_upcoming_tt);

        TextView sub1 = (TextView) findViewById(R.id.sub1);
        TextView sub2 = (TextView) findViewById(R.id.sub2);
        TextView sub3 = (TextView) findViewById(R.id.sub3);
        TextView sub4 = (TextView) findViewById(R.id.sub4);
        TextView sub5 = (TextView) findViewById(R.id.sub5);
        TextView sub6 = (TextView) findViewById(R.id.sub6);
        TextView sub7 = (TextView) findViewById(R.id.sub7);
        TextView sub8 = (TextView) findViewById(R.id.sub8);

        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });

        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });

        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });

        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });

        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });

        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });

        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });

        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog();
            }
        });
    }

    public void selectsubdialog()
    {
            /* When getting the number of subjects from the api, if no ofsubjects <11, set the appropriate number of radiobutton's visibility to gone */
        final Dialog dialog = new Dialog(EditUpcomingTT.this);
        dialog.setContentView(R.layout.edit_tt_dialog);
        dialog.setTitle("Select Subject");

        Button dialogButton = (Button) dialog.findViewById(R.id.okbutton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_upcoming_tt, menu);
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
