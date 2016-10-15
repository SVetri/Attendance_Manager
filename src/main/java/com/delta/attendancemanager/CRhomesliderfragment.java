package com.delta.attendancemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rahul on 9/14/15.
 */
public class CRhomesliderfragment extends Fragment {
    Context context;

    MySqlAdapter handler;
    List<String[]> all;
    int page;

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page=getArguments().getInt("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = new MySqlAdapter(getActivity(),null);
        return inflater.inflate(R.layout.fragment_crhome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager2);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(page);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs2);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            //return "Item " + (position + 1);

            switch (position) {
                case 0:
                    return "Timetable";
                case 1:
                    return "Manage Courses  ";
                case 2:
                    return " Announcements ";

            }

            return null;
        }

        public void selectsubdialog(View v, int no) {
            final TextView t = (TextView) v;
            final int n = no;
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.edit_tt_dialog);
            dialog.setTitle("Select Subject");
            List<String> subs = new ArrayList<>();
            final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.rg);
            subs = handler.getSubs();
            String[] al = new String[subs.size()];
            al = subs.toArray(al);
            RadioButton rb;
            for (String i : al) {
                if (i.equals(Constants.BLANK_STRING))
                    continue;
                rb = new RadioButton(getActivity());
                rb.setText(i);
                rg.addView(rb);
            }

            Button dialogButton = (Button) dialog.findViewById(R.id.okbutton);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] a = new String[9];
                    a = handler.get_tomo();
                    int pos = rg.getCheckedRadioButtonId();
                    RadioButton rd = (RadioButton) rg.findViewById(pos);
                    String su = rd.getText().toString();
                    t.setText(su);
                    a[n] = su;
                    handler.update_tomo(a);
                    JSONObject j = new JSONObject();
                    for (int i = 1; i <= 8; i++) {
                        try {
                            j.put(EditUpcomingTT.slots[i - 1], a[i]);
                        } catch (JSONException e) {
                            Log.e("CRhomesliderfragment", e.toString());
                        }
                    }
                    UpdateTTService.startActionUpcoming(getActivity(), j);
                    dialog.cancel();
                }
            });
            Button dialogCancel = (Button)dialog.findViewById(R.id.cancelbutton);
            dialogCancel.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }
            );

            dialog.show();
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            context = getActivity().getApplicationContext();
            // Inflate a new layout from our resources
            View view = null;
            TextView sub [] = new TextView[8];
            Button btns [] = new Button[3];
            int [] btnsInt = {R.id.refreshChat, R.id.chatbutton, R.id.addbutton};
            for (int i= 0; i < sub.length; i++){
                btns[i] = (Button) view.findViewById(btnsInt[i]);
            }
            // Add the newly created View to the ViewPager

            switch (position) {
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.activity_edit_upcoming_tt,
                            container, false);
                    String[] all;
                    handler = new MySqlAdapter(getActivity(), null);
                    final String[] slots = {"830", "920", "1030", "1120", "130", "220", "310", "400"};

                    handler = new MySqlAdapter(getActivity(), null);
                    //handler.add_day("tomorrow","DS","DSD","DC","HOLA","QWER","ASDF","ZXCV","LKJH");
                    all = new String[9];
                    int [] subsInt = {R.id.sub1, R.id.sub2, R.id.sub3, R.id.sub4, R.id.sub5, R.id.sub6, R.id.sub7, R.id.sub8};
                    for (int i= 0; i < sub.length; i++){
                        sub[i] = (TextView) view.findViewById(subsInt[i]);
                    }

                    all = handler.get_tomo();
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
                            selectsubdialog(v, 1);
                        }
                    });

                    sub[1].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectsubdialog(v, 2);
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
                            selectsubdialog(v, 4);
                        }
                    });

                    sub[4].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectsubdialog(v, 5);
                        }
                    });

                    sub[5].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectsubdialog(v, 6);
                        }
                    });

                    sub[6].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectsubdialog(v, 7);
                        }
                    });

                    sub[7].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectsubdialog(v, 8);
                        }
                    });
                    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floating1);
                    fab.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getActivity(), EditWeeklyTT.class);
                                    startActivity(i);
                                }
                            }
                    );


                    break;

                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.layout_cr_announcement,
                            container, false);
                    handler = new MySqlAdapter(getActivity(), null);
                    final ListView l = (ListView) view.findViewById(R.id.crchat);
                    Chat[] a = handler.getmsgs();
                    final ChatAdapter adapter = new ChatAdapter(getActivity(), a);
                    l.setAdapter(adapter);

                    btns[0].setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Chat[] a = handler.getmsgs();
                                    ChatAdapter adapter = new ChatAdapter(getActivity(), a);
                                    l.setAdapter(adapter);
                                }
                            }
                    );

                    final View v = view;
                    final LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll1);
                    btns[1].setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText et = (EditText) ll.findViewById(R.id.textmsg);
                                    String msg = et.getText().toString();
                                    et.setText("");
//                                    UpdateTTService.startActionChat(getActivity(), msg);
                                    chatOut(msg);
                                }
                            }
                    );
                    break;

                default:
                    view = getActivity().getLayoutInflater().inflate(R.layout.activity_manage_course,
                            container, false);

//                    MySqlHandler handler=new MySqlHandler(getActivity(),null);
                    final ListView li = (ListView) view.findViewById(R.id.courses);
                    btns[2].setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    final EditText t = new EditText(getActivity());
                                    t.setInputType(InputType.TYPE_CLASS_TEXT);
                                    builder.setView(t);
                                    builder.setMessage("Enter the Name of the course")
                                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    handler.add_sub(t.getText().toString());
                                                    ArrayList<String> s = new ArrayList();

                                                    s = handler.getSubs();

                                                    String[] al = new String[s.size()];
                                                    al = s.toArray(al);
                                                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al);
                                                    li.setAdapter(adapter1);

//                                                    ischanged=true;
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                }
                                            });
                                    builder.show();
                                }
                            }
                    );

                    ArrayList<String> s = new ArrayList();
                    if(handler.getSubs()==null){
                        handler.add_sub("");
                    }

                    s = handler.getSubs();

                    String[] al = new String[s.size()];
                    al = s.toArray(al);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al);
                    li.setAdapter(adapter1);
                    li.setOnItemLongClickListener(
                            new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    final String subs = (String) parent.getItemAtPosition(position);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("Do you really want to delete the Course " + subs + " ?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    handler.delete_sub(subs);
                                                    ArrayList<String> s = handler.getSubs();

                                                    String[] al = new String[s.size()];
                                                    al = s.toArray(al);
                                                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al);
                                                    li.setAdapter(adapter1);
                                                    UpdateSubs(subs);

                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    builder.show();
                                    return true;
                                }
                            }


                    );


                    break;
            }
            container.addView(view);
            return view;

        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
    public void UpdateSubs(String subs){
        final String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","tomorrow"};
        String[] dayTimetable = new String[9];
        AttendanceServerService.deleteAttendance(context);
        ArrayList<String> finalTimetable;
        for (String day : days ){
            switch(day){
                case "Monday":
                    dayTimetable = handler.get_mon();
                    break;
                case "Tuesday":
                    dayTimetable = handler.get_tue();
                    break;
                case "Wednesday":
                    dayTimetable = handler.get_wed();
                    break;
                case "Thursday":
                    dayTimetable = handler.get_thur();
                    break;
                case "Friday":
                    dayTimetable = handler.get_fri();
                    break;
                case "tomorrow":
                    dayTimetable = handler.get_tomo();
                    break;
            }
            finalTimetable=new ArrayList<>();
            for (String sub : dayTimetable){
                if(sub.equals(subs))
                    finalTimetable.add("");
                else
                    finalTimetable.add(sub);
            }
            handler.delete_day(day);
            handler.add_day(finalTimetable.get(0), finalTimetable.get(1), finalTimetable.get(2), finalTimetable.get(3),
                    finalTimetable.get(4), finalTimetable.get(5), finalTimetable.get(6), finalTimetable.get(7), finalTimetable.get(8));
        }
        String[] a = new String[9];
        a = handler.get_tomo();
        JSONObject j = new JSONObject();
        for (int i = 1; i <= 8; i++) {
            try {
                j.put(EditUpcomingTT.slots[i - 1], a[i]);
            } catch (JSONException e) {
                Log.e("CRhomesliderfragment", e.toString());
            }
        }
        UpdateTTService.startActionUpcoming(getActivity(), j);
        List<String[]> all = new ArrayList<>();
        all.add(handler.get_mon());
        all.add(handler.get_tue());
        all.add(handler.get_wed());
        all.add(handler.get_thur());
        all.add(handler.get_fri());

         j = new JSONObject();

        for (String[] k : all) {
            JSONObject js = new JSONObject();
            for (int i = 1; i <= 8; i++) {
                try {
                    js.accumulate(EditUpcomingTT.slots[i - 1], k[i]);
                } catch (JSONException e) {
                    Log.e("CRhomesliderfragment", e.toString());
                }
            }
            try {
                j.put(k[0], js);
            } catch (JSONException e) {
                Log.e("CRhomesliderfragment", e.toString());
            }
        }

        AttendanceServerService.addAttendance(context);

        UpdateTTService.startActionTT(getActivity(), j);

        getActivity().startActivity(new Intent(getActivity(),CRhome.class));
        getActivity().finish();
    }

    private void chatOut(String msg) {
        new AsyncTask<String,Void,Void>(){
            ProgressDialog dialog = new ProgressDialog(context);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Sending Message....");
                dialog.show();
            }

            @Override
            protected Void doInBackground(String... params) {
                SharedPreferences share1=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                String rno=share1.getString("crrno", ":)");
                String batch = rno.substring(0, rno.length() - 3);
                JSONObject data=new JSONObject();
                Calendar cal=Calendar.getInstance();
                int h=cal.get(Calendar.HOUR_OF_DAY);
                int min=cal.get(Calendar.MINUTE);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                int mon=cal.get(Calendar.MONTH)+1;
                int yy=cal.get(Calendar.YEAR);
                String time= String.valueOf(h)+":"+String.valueOf(min);
                String date=String.valueOf(day)+"/"+String.valueOf(mon)+"/"+String.valueOf(yy);
                JSONObject js =new JSONObject();
                try {
                    data.put("msg",params[0]);
                    data.put("date",date);
                    data.put("time",time);
                    JSONObject jb=new JSONObject();
                    jb.put("an",data);

                    js.put("data",jb);
                    js.put("type","chat");
                    SharedPreferences prefs = getActivity().getSharedPreferences("user",
                            Context.MODE_PRIVATE);
                    String rollno = prefs.getString(MainActivity.RNO, "default");
                    String secret = prefs.getString("secret", "default");
                    js.put("secret",secret);
                    js.put("username",rollno);
                    js.put("batch",batch);
                } catch (JSONException e) {
                    Log.e("CRhomesliderfragment", e.toString());
                }


                JSONObject result;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(MainActivity.URL+"/updateTT");
                StringEntity s= null;
                try {
                    s = new StringEntity(js.toString());
                } catch (UnsupportedEncodingException e) {
                    Log.e("CRhomesliderfragment", e.toString());
                }
                httpPost.setEntity(s);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = null;

                String jsons="";
                // 9. receive response as inputStream

                try {
                    httpResponse = httpclient.execute(httpPost);
                    InputStream is = httpResponse.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();

                    jsons = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    result = new JSONObject(jsons);
                    Log.i("connectionscheck",result.toString());
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
               return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                Toast.makeText(context, "Sent SuccessFully. Refresh To GetAnnouncement.", Toast.LENGTH_SHORT).show();
            }
        }.execute(msg);
    }

}


