package com.delta.attendancemanager;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TooManyListenersException;


public class EditUpcomingTT extends ActionBarActivity {
    String[] all;
    MySqlHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_upcoming_tt);
        handler=new MySqlHandler(this,null);
        //handler.add_day("tomorrow","DS","DSD","DC","HOLA","QWER","ASDF","ZXCV","LKJH");
        all=new String[9];
        TextView sub1 = (TextView) findViewById(R.id.sub1);
        TextView sub2 = (TextView) findViewById(R.id.sub2);
        TextView sub3 = (TextView) findViewById(R.id.sub3);
        TextView sub4 = (TextView) findViewById(R.id.sub4);
        TextView sub5 = (TextView) findViewById(R.id.sub5);
        TextView sub6 = (TextView) findViewById(R.id.sub6);
        TextView sub7 = (TextView) findViewById(R.id.sub7);
        TextView sub8 = (TextView) findViewById(R.id.sub8);

        all=handler.get_tomo();
        sub1.setText(all[1]);
        sub2.setText(all[2]);
        sub3.setText(all[3]);
        sub4.setText(all[4]);
        sub5.setText(all[5]);
        sub6.setText(all[6]);
        sub7.setText(all[7]);
        sub8.setText(all[8]);
        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });

        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });

        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });

        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });

        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });

        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });

        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });

        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "vClicked", Toast.LENGTH_SHORT).show();
                //return true;
                selectsubdialog(v);
            }
        });
    }

    public void selectsubdialog(View v)
    {
        final TextView t = (TextView) v;
        String vname=t.getText().toString();
        Toast.makeText(EditUpcomingTT.this,vname,Toast.LENGTH_SHORT).show();
            /* When getting the number of subjects from the api, if no ofsubjects <11, set the appropriate number of radiobutton's visibility to gone */
        final Dialog dialog = new Dialog(EditUpcomingTT.this);
        dialog.setContentView(R.layout.edit_tt_dialog);
        dialog.setTitle("Select Subject");
        List<String> subs=new ArrayList<>();
        final RadioGroup rg= (RadioGroup)dialog.findViewById(R.id.rg);
        subs=handler.getSubs();
        String[] al=new String[subs.size()];
        al=subs.toArray(al);
        RadioButton ssub1 = (RadioButton) rg.findViewById(R.id.selsub1);
        RadioButton ssub2 = (RadioButton) rg.findViewById(R.id.selsub2);
        RadioButton ssub3 = (RadioButton) rg.findViewById(R.id.selsub3);
        RadioButton ssub4 = (RadioButton) rg.findViewById(R.id.selsub4);
        RadioButton ssub5 = (RadioButton) rg.findViewById(R.id.selsub5);
        RadioButton ssub6 = (RadioButton) rg.findViewById(R.id.selsub6);
        RadioButton ssub7 = (RadioButton) rg.findViewById(R.id.selsub7);
        RadioButton ssub8 = (RadioButton) rg.findViewById(R.id.selsub8);
//        for (String i : al){
//            Log.i("dvgf",i+(ssub1==null));
//
//        }
        ssub1.setText(al[0]);
        ssub2.setText(al[1]);
        ssub3.setText(al[2]);
        ssub4.setText(al[3]);
        ssub5.setText(al[4]);
        ssub6.setText(al[5]);
        ssub7.setText(al[6]);
        ssub8.setText(al[7]);


        Button dialogButton = (Button) dialog.findViewById(R.id.okbutton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=rg.getCheckedRadioButtonId();
                RadioButton rd= (RadioButton)rg.findViewById(pos);
                String su=rd.getText().toString();
                Toast.makeText(EditUpcomingTT.this,su,Toast.LENGTH_SHORT).show();
                t.setText(su);
            }
        });

        dialog.show();
    }
}
