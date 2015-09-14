package com.delta.attendancemanager;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EditWeeklyTT extends ActionBarActivity {
    public static final String[] Days={"Monday","Tuesday","Wednesday","Thursday","Friday"};
    MySqlAdapter handler;
    public static boolean ischanged;
    @Override
    public void onBackPressed() {
        if(ischanged){
            List<String[]> all=new ArrayList<>();
            all=handler.get_days();
            JSONObject j=new JSONObject();

            for(String[] k : all){
                JSONObject js=new JSONObject();
                for (int i=1;i<=8;i++){
                    try {
                        js.put(EditUpcomingTT.slots[i-1],k[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    j.put(k[0],js);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            UpdateTTService.startActionTT(this,j);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weekly_tt);
        ischanged=false;
        handler=new MySqlAdapter(this,null);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CRSlidingTabsFragment fragment = new CRSlidingTabsFragment();
            transaction.replace(R.id.edit_fragment_content, fragment);
            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_weekly_tt, menu);
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
