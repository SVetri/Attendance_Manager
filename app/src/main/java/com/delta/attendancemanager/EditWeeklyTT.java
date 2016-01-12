package com.delta.attendancemanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EditWeeklyTT extends ActionBarActivity {
    public static final String[] Days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    public static boolean ischanged;
    MySqlAdapter handler;

    @Override
    public void onBackPressed() {
        if (ischanged) {
            List<String[]> all = new ArrayList<>();
            all.add(handler.get_mon());
            all.add(handler.get_tue());
            all.add(handler.get_wed());
            all.add(handler.get_thur());
            all.add(handler.get_fri());

            JSONObject j = new JSONObject();

            for (String[] k : all) {
                JSONObject js = new JSONObject();
                for (int i = 1; i <= 8; i++) {
                    try {
                        js.accumulate(EditUpcomingTT.slots[i - 1], k[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    j.put(k[0], js);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            UpdateTTService.startActionTT(this, j);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weekly_tt);
        ischanged = false;
        handler = new MySqlAdapter(this, null);
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
        if (id == R.id.action_POST) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.edit_tt_dialog);
            dialog.setTitle("POST to ALL");
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to post this online?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            List<String[]> all = new ArrayList<>();
                            all.add(handler.get_mon());
                            all.add(handler.get_tue());
                            all.add(handler.get_wed());
                            all.add(handler.get_thur());
                            all.add(handler.get_fri());

                            JSONObject j = new JSONObject();

                            for (String[] k : all) {
                                JSONObject js = new JSONObject();
                                for (int i = 1; i <= 8; i++) {
                                    try {
                                        js.accumulate(EditUpcomingTT.slots[i - 1], k[i]);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    j.put(k[0], js);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            UpdateTTService.startActionTT(EditWeeklyTT.this, j);

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
