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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TooManyListenersException;


public class EditUpcomingTT extends ActionBarActivity {
    String[] all;
    MySqlAdapter handler;
    public static final String[] slots={"830","920","1030","1120","130","220","310","400"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_upcoming_tt);
        handler=new MySqlAdapter(this,null);
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
                selectsubdialog(v,"830",1);
            }
        });

        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,"920",2);
            }
        });

        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,"1030",3);
            }
        });

        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,"1120",4);
            }
        });

        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,"130",5);
            }
        });

        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,"220",6);
            }
        });

        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,"310",7);
            }
        });

        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,"400",8);
            }
        });
    }

    public void selectsubdialog(View v,String time,int no)
    {
        final TextView t = (TextView) v;
        final int n=no;
        final Dialog dialog = new Dialog(EditUpcomingTT.this);
        dialog.setContentView(R.layout.edit_tt_dialog);
        dialog.setTitle("Select Subject");
        List<String> subs=new ArrayList<>();
        final RadioGroup rg= (RadioGroup)dialog.findViewById(R.id.rg);
        subs=handler.getSubs();
        String[] al=new String[subs.size()];
        al=subs.toArray(al);

        for (String i : al){
            if(i.equals(" "))
                continue;
            RadioButton rb=new RadioButton(this);
            rb.setText(i);
            rg.addView(rb);
        }

        Button dialogButton = (Button) dialog.findViewById(R.id.okbutton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] a=new String[9];
                a=handler.get_tomo();
                int pos=rg.getCheckedRadioButtonId();
                RadioButton rd= (RadioButton)rg.findViewById(pos);
                String su=rd.getText().toString();
                Toast.makeText(EditUpcomingTT.this,su,Toast.LENGTH_SHORT).show();
                t.setText(su);
                a[n]=su;
                handler.update_tomo(a);
                JSONObject j=new JSONObject();
                for (int i=1;i<=8;i++){
                    try {
                        j.put(slots[i-1],a[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                UpdateTTService.startActionUpcoming(EditUpcomingTT.this,j);
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
