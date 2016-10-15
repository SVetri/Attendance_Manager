package com.delta.attendancemanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Handle the view for the classes on the next days
 */
public class UpcomingTT extends ActionBarActivity {
    TextView sub1,sub2,sub3,sub4,sub5,sub6,sub7,sub8;
    String[] all;
    MySqlAdapter handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_tt);
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
    }

//    public void floatingclicked(){
//        Intent i=new Intent(this,WeeklyTimetable.class);
//        startActivity(i);
//    }

}
