package com.delta.attendancemanager;

import com.delta.attendancemanager.SlidingTabLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//TODO made a small change - find whether its working

/**
 * Created by S on 12/20/2014.
 */
public class SlidingTabsFragment extends Fragment {

    MySqlAdapter handler;
    List<String[]> all;

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weektt, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    protected class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 5;
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
                    return "Monday";
                case 1:
                    return "Tuesday";
                case 2:
                    return "Wednesday";
                case 3:
                    return "Thursday";
                case 4:
                    return "Friday";
            }

            return null;
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            handler=new MySqlAdapter(getActivity(),null);
            all=new ArrayList<>();

            String[] m,t,w,th,f,x;
            x=new String[9];

            m=handler.get_mon();
            t=handler.get_tue();
            w=handler.get_wed();
            th=handler.get_thur();
            f=handler.get_fri();

            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);
            int [] subsInt = {R.id.sub1, R.id.sub2, R.id.sub3, R.id.sub4, R.id.sub5, R.id.sub6, R.id.sub7, R.id.sub8};
            TextView sub [] = new TextView[8];
            for (int i= 0; i < sub.length; i++){
                sub[i] = (TextView) view.findViewById(subsInt[i]);
            }

            switch (position){
                case 0:
                    x=m;
                    break;
                case 1:
                    x=t;
                    break;
                case 2:
                    x=w;
                    break;
                case 3:
                    x=th;
                    break;
                case 4:
                    x=f;
                    break;
            }
            sub[0].setText(x[1]);
            sub[1].setText(x[2]);
            sub[2].setText(x[3]);
            sub[3].setText(x[4]);
            sub[4].setText(x[5]);
            sub[5].setText(x[6]);
            sub[6].setText(x[7]);
            sub[7].setText(x[8]);
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
