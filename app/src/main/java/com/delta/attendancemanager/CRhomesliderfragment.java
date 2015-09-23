package com.delta.attendancemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 9/14/15.
 */
public class CRhomesliderfragment extends Fragment {

    MySqlAdapter handler;
    List<String[]> all;

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crhome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager2);
        mViewPager.setAdapter(new SamplePagerAdapter());

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
            return 2;
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
        public CharSequence getPageTitle(int position)
        {
            //return "Item " + (position + 1);

            switch(position)
            {
                case 0:
                    return "Timetable";
                case 1:
                    return "Manage Courses  ";

            }

            return null;
        }

        public void selectsubdialog(View v,String time,int no)
        {
            final TextView t = (TextView) v;
            final int n=no;
            final Dialog dialog = new Dialog(getActivity());
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
                RadioButton rb=new RadioButton(getActivity());
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
                    Toast.makeText(getActivity(), su, Toast.LENGTH_SHORT).show();
                    t.setText(su);
                    a[n]=su;
                    handler.update_tomo(a);
                    JSONObject j=new JSONObject();
                    for (int i=1;i<=8;i++){
                        try {
                            j.put(EditUpcomingTT.slots[i-1],a[i]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    UpdateTTService.startActionUpcoming(getActivity(),j);
                    dialog.cancel();
                }
            });

            dialog.show();
        }
        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Inflate a new layout from our resources
            View view ;
            TextView sub1,sub2,sub3,sub4,sub5,sub6,sub7,sub8;
            // Add the newly created View to the ViewPager

            switch (position){
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.activity_edit_upcoming_tt,
                            container, false);
                    String[] all;
                     handler=new MySqlAdapter(getActivity(),null);
                    final String[] slots={"830","920","1030","1120","130","220","310","400"};
                    
                    handler=new MySqlAdapter(getActivity(),null);
                    //handler.add_day("tomorrow","DS","DSD","DC","HOLA","QWER","ASDF","ZXCV","LKJH");
                    all=new String[9];
                     sub1 = (TextView)view. findViewById(R.id.sub1);
                     sub2 = (TextView)view. findViewById(R.id.sub2);
                     sub3 = (TextView)view. findViewById(R.id.sub3);
                     sub4 = (TextView) view.findViewById(R.id.sub4);
                     sub5 = (TextView) view.findViewById(R.id.sub5);
                     sub6 = (TextView) view.findViewById(R.id.sub6);
                     sub7 = (TextView) view.findViewById(R.id.sub7);
                     sub8 = (TextView) view.findViewById(R.id.sub8);

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
                            selectsubdialog(v, "830", 1);
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
                    FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.floating1);
                    fab.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i=new Intent(getActivity(),EditWeeklyTT.class);
                                    startActivity(i);
                                }
                            }
                    );


                    break;

                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.layout_cr_announcement,
                            container, false);
                    handler=new MySqlAdapter(getActivity(),null);
                    ListView l=(ListView)view.findViewById(R.id.crchat);
                    String[] a=handler.getmsgs();
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,a);
                    l.setAdapter(adapter);
                    Button bt= (Button) view.findViewById(R.id.chatbutton);
                    final View v=view;
                    bt.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText et= (EditText)v.findViewById(R.id.textmsg);
                                    String msg=et.getText().toString();
                                    UpdateTTService.startActionChat(getActivity(),msg);
                                }
                            }
                    );
                    break;

                default:
                    view = getActivity().getLayoutInflater().inflate(R.layout.activity_manage_course,
                            container, false);
                    
                    Button add=(Button)view.findViewById(R.id.addbutton);
                    Button del=(Button)view.findViewById(R.id.deletebutton);
//                    MySqlHandler handler=new MySqlHandler(getActivity(),null);
                    add.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    final EditText t=new EditText(getActivity());
                                    t.setInputType(InputType.TYPE_CLASS_TEXT);
                                    builder.setView(t);
                                    builder.setMessage("Enter the Name of the course")
                                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    handler.add_sub(t.getText().toString());
//                                                    ischanged=true;
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User cancelled the dialog
                                                }
                                            });
                                    builder.show();
                                }
                            }
                    );
                    ListView li=(ListView)view.findViewById(R.id.courses);
                    ArrayList<String> s =new ArrayList();

                    s=handler.getSubs();

                    String[] al =new String[s.size()];
                    al=s.toArray(al);
                    ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,al);
                    li.setAdapter(adapter1);
                    li.setOnItemLongClickListener(
                            new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    String subs = (String) parent.getItemAtPosition(position);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("Do you really want to delete the Course " + subs + " ?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //delete Subject
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
            return  view;

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

}


