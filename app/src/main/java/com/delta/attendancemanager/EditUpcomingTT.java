package com.delta.attendancemanager;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle the view to edit the classes on the next day
 */
public class EditUpcomingTT extends ActionBarActivity {
    String[] all;
    MySqlAdapter handler;
    /**
     * Create a String list with the timetable hours
     */
    public static final String[] slots={"830","920","1030","1120","130","220","310","400"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_upcoming_tt);
        handler=new MySqlAdapter(this,null);
        //handler.add_day("tomorrow","DS","DSD","DC","HOLA","QWER","ASDF","ZXCV","LKJH");
        all=new String[9];
        int [] subsInt = {R.id.sub1, R.id.sub2, R.id.sub3, R.id.sub4, R.id.sub5, R.id.sub6, R.id.sub7, R.id.sub8};
        TextView sub [] = new TextView[8];
        for (int i= 0; i < sub.length; i++){
            sub[i] = (TextView) findViewById(subsInt[i]);
        }

        all=handler.get_tomo();
        sub[0].setText(all[1]);
        sub[1].setText(all[2]);
        sub[2].setText(all[3]);
        sub[3].setText(all[4]);
        sub[4].setText(all[5]);
        sub[5].setText(all[6]);
        sub[6].setText(all[7]);
        sub[7].setText(all[8]);
        sub[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,1);
            }
        });

        sub[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,2);
            }
        });

        sub[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v, 3);
            }
        });

        sub[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,4);
            }
        });

        sub[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,5);
            }
        });

        sub[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,6);
            }
        });

        sub[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,7);
            }
        });

        sub[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectsubdialog(v,8);
            }
        });
    }

    /**
     * Create a menu dialog that allows the user to select a subject
     * @param v where to construct the menu dialog
     * @param no number of element
     */
    public void selectsubdialog(View v, int no)
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
        RadioButton rb;
        for (String i : al){
            if(i.equals(Constants.BLANK_STRING))
                continue;
            rb=new RadioButton(this);
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
                        Log.e("EditUpcomingTT", e.toString());
                    }
                }
                UpdateTTService.startActionUpcoming(EditUpcomingTT.this,j);
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
