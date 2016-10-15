package com.delta.attendancemanager;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Handles the view in which are displayed the attendaces
 */
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<CardInfo> attendanceList;
    AtAdapter atAdapter;
    Context context;
    Date date = null;

    /**
     * Buil the view in which display the attendances
     *
     * @param contactList list of the attendances
     * @param context     specify where to construct
     */
    public AttendanceAdapter(List<CardInfo> contactList, Context context) {
        this.attendanceList = contactList;
        this.context = context;
        atAdapter = new AtAdapter(context);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder attendanceviewholder, final int i) {
        final CardInfo ci = attendanceList.get(i);
        attendanceviewholder.sub[0].setText(ci.coursename);
        attendanceviewholder.sub[1].setText(ci.classdate);
        attendanceviewholder.sub[2].setText(ci.classtime);

        attendanceviewholder.sub[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = attendanceList.indexOf(ci);
                atAdapter.update_attendance(ci.coursename, formatDate(ci.classdate + " " + ci.classtime), 1);
                attendanceList.remove(position);
                notifyItemRemoved(position);
            }
        });
        attendanceviewholder.sub[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = attendanceList.indexOf(ci);
                atAdapter.update_attendance(ci.coursename, formatDate(ci.classdate + " " + ci.classtime), -1);
                attendanceList.remove(position);
                notifyItemRemoved(position);
            }
        });
        attendanceviewholder.sub[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = attendanceList.indexOf(ci);
                atAdapter.delete_data(ci.coursename, formatDate(ci.classdate + " " + ci.classtime));
                attendanceList.remove(position);
                notifyItemRemoved(position);
            }
        });

    }

    private String formatDate(String fullDate) {
        String year = fullDate.substring(6, 10);
        String month = fullDate.substring(3, 5);
        String day = fullDate.substring(0, 2);
        String hour = fullDate.substring(11);
        String out = year + "-" + month + "-" + day + " " + hour;
        return out;
    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_card, viewGroup, false);
        return new AttendanceViewHolder(itemView);
    }

    /**
     * This class defines the design for the attendance view
     */
    protected class AttendanceViewHolder extends RecyclerView.ViewHolder {

        protected TextView sub[] = new TextView[6];

        public AttendanceViewHolder(View v) {
            super(v);
            int[] subsInt = {R.id.subjectcard, R.id.datecard, R.id.timecard, R.id.presentbutton, R.id.absentbutton, R.id.removebutton1};

            for (int i = 0; i < sub.length; i++) {
                sub[i] = (TextView) v.findViewById(subsInt[i]);
            }
        }

    }

}
