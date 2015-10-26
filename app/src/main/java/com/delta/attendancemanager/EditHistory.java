package com.delta.attendancemanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//TODO edit history class to edit the already marked history - local attendance database should be used.

public class EditHistory extends ActionBarActivity {
    AtAdapter atAdapter;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_history);
        atAdapter = new AtAdapter(getApplicationContext());
        final String subname = getIntent().getStringExtra("sname");

        RecyclerView reclist = (RecyclerView) findViewById(R.id.editcardList);
        reclist.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reclist.setLayoutManager(llm);
        atAdapter.fetch_subject_data(subname);
        EditAdapter edadapter = new EditAdapter(getApplicationContext(),createList(subname,atAdapter.getDt(),atAdapter.getPresint()));
        reclist.setAdapter(edadapter);
    }

    private List<EditCardInfo> createList(String sname, ArrayList<String> datetime, ArrayList<Integer> present) {

        List<EditCardInfo> result = new ArrayList<EditCardInfo>();
        for (int i=0; i < datetime.size(); i++) {
            EditCardInfo eci = new EditCardInfo();
            eci.coursename=sname;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try{
                date = sdf.parse(datetime.get(i));
            }
            catch(Exception e){
                Log.d("hel", e.toString());
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");                      //do not change the time format here. giving error in database. change it while displaying if necessary.
            SimpleDateFormat sdf2 = new SimpleDateFormat("h:m");
            eci.classdate = sdf1.format(date);
            eci.classtime = sdf2.format(date);
            if (present.get(i) == 1)
            eci.attendance=Boolean.TRUE;
            else
            eci.attendance=Boolean.FALSE;
            result.add(eci);

        }

        return result;
    }
    public void addclass(View v){

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_history, menu);
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
