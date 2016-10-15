package com.delta.attendancemanager;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


//TODO edit history class to edit the already marked history - local attendance database should be used.

/**
 * Handle the view and the methods to edit the history of the attendaces
 */
public class EditHistory extends ActionBarActivity {
    AtAdapter atAdapter;
    String subname;
    DatePicker dp;
    RecyclerView reclist;
    int p = 1;
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_history);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating12);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClass();
            }
        });
        atAdapter = new AtAdapter(getApplicationContext());
        subname = getIntent().getStringExtra("sname");

        reclist = (RecyclerView) findViewById(R.id.editcardList);
        reclist.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reclist.setLayoutManager(llm);
        atAdapter.fetch_subject_data(subname);
        EditAdapter edadapter = new EditAdapter(getApplicationContext(),createList(subname,atAdapter.getDt(),atAdapter.getPresint()));
        reclist.setAdapter(edadapter);
    }

    /**
     * Create the objects for the attendances editing
     * @param sname
     * @param datetime
     * @param present
     * @return
     */
    private List<EditCardInfo> createList(String sname, ArrayList<String> datetime, ArrayList<Integer> present) {
        EditCardInfo eci;
        List<EditCardInfo> result = new ArrayList<EditCardInfo>();
        for (int i=0; i < datetime.size(); i++) {
            eci = new EditCardInfo();
            eci.coursename=sname;
            eci.classdate = formatDate(datetime.get(i));
            eci.classtime = formatHour(datetime.get(i));
            if (present.get(i) == 1)
            eci.attendance=Boolean.TRUE;
            else
            eci.attendance=Boolean.FALSE;
            result.add(eci);

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


    /**
     * Allows the user to add an attendance manually
     */
    public void addClass(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_att_dialog);
        dialog.setTitle("Add Attendance");
        Button cancel = (Button) dialog.findViewById(R.id.cancelbutton);
        Button add = (Button) dialog.findViewById(R.id.addclass);
        Spinner sp = (Spinner) dialog.findViewById(R.id.spinner);
        dp = (DatePicker) dialog.findViewById(R.id.datePicker);
        ToggleButton toggleButton= (ToggleButton) dialog.findViewById(R.id.toggleButton);
        List<String> categories = new ArrayList<String>();
        for(int i = 1;i<=8;i++) {
            categories.add(String.format("%02d", TTimings.hour[i]) + ":" + String.format("%02d", TTimings.min[i]));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.addrg);
        for(int i=1;i<=8;i++){
            RadioButton rb = new RadioButton(getApplicationContext());
            rb.setText(Integer.toString(TTimings.hour[i])+" "+Integer.toString(TTimings.min[i]));
            rb.setTextColor(getResources().getColor(R.color.darkb));
            rg.addView(rb);
        }*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        p = 1;
        toggleButton.setChecked(true);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    p = 1;
                else
                    p = -1;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int pos = rg.getCheckedRadioButtonId();
                AtAdapter atAdapter = new AtAdapter(getApplicationContext());
                LocalDate ld = new LocalDate(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                String f = ld + " " + TTimings.hour[pos + 1] + ":" + TTimings.min[pos + 1];
                atAdapter.add_attendance(subname, f, p);
                reclist = (RecyclerView) findViewById(R.id.editcardList);
                reclist.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                reclist.setLayoutManager(llm);
                atAdapter.fetch_subject_data(subname);
                EditAdapter edadapter = new EditAdapter(getApplicationContext(),createList(subname,atAdapter.getDt(),atAdapter.getPresint()));
                dialog.cancel();
                reclist.setAdapter(edadapter);
            }
        });

        dialog.show();


    }
}