package com.delta.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserHomeSliderFragment extends Fragment {

    MySqlAdapter handler;
    List<String[]> all;
    int page;

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page=getArguments().getInt("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userhome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager3);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(page);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs3);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    protected class SamplePagerAdapter extends PagerAdapter {

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
        public CharSequence getPageTitle(int position)
        {
            //return "Item " + (position + 1);

            switch(position)
            {
                case 0:
                    return "Timetable";
                case 1:
                    return "Attendance";
                case 2:
                    return "Announcements";

            }

            return null;
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Inflate a new layout from our resources
            View view ;
            // Add the newly created View to the ViewPager

            switch (position){
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.activity_upcoming_tt,
                            container, false);
                    handler=new MySqlAdapter(getActivity(),null);

                    //handler.add_day("tomorrow","DS","DSD","DC","HOLA","QWER","ASDF","ZXCV","LKJH");
                    String[] al=new String[9];
                    int [] subsInt = {R.id.sub1, R.id.sub2, R.id.sub3, R.id.sub4, R.id.sub5, R.id.sub6, R.id.sub7, R.id.sub8};
                    TextView sub [] = new TextView[8];
                    for (int i= 0; i < sub.length; i++){
                        sub[i] = (TextView) view.findViewById(subsInt[i]);
                    }

                    al=handler.get_tomo();
                    sub[0].setText(al[1]);
                    sub[1].setText(al[2]);
                    sub[2].setText(al[3]);
                    sub[3].setText(al[4]);
                    sub[4].setText(al[5]);
                    sub[5].setText(al[6]);
                    sub[6].setText(al[7]);
                    sub[7].setText(al[8]);
                    final Context context=getActivity();
                    FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.floating);
                    fab.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i=new Intent(getActivity(),WeeklyTimetable.class);
                                    startActivity(i);
                                }
                            }
                    );
                    break;

                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.layout_announcement,
                            container, false);
                    handler=new MySqlAdapter(getActivity(),null);
                    ListView l=(ListView)view.findViewById(R.id.chat);
                    Chat[] a=handler.getmsgs();
                    ChatAdapter adapter=new ChatAdapter(getActivity(),a);
                    l.setAdapter(adapter);
                    break;

                default:
                    view = getActivity().getLayoutInflater().inflate(R.layout.activity_manage_attendance,
                            container, false);
                    Button updateatt = (Button) view.findViewById(R.id.updatemyatt);
                    Button viewatt = (Button) view. findViewById(R.id.viewmyatt);

                    updateatt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(getActivity(), UpdateMyAttendance.class);
                            startActivity(i);

                        }
                    });


                    viewatt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(getActivity(), ViewMyAttendance.class);
                            startActivity(i);

                        }
                    });

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

