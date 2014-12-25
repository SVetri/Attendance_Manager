package com.delta.attendancemanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by S on 12/24/2014.
 */


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<CardInfo> attendanceList;

    public AttendanceAdapter(List<CardInfo> contactList) {
        this.attendanceList = contactList;
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder attendanceviewholder, final int i)
    {
        final CardInfo ci = attendanceList.get(i);
        attendanceviewholder.subject.setText(ci.coursename);
        attendanceviewholder.date.setText(ci.classdate);
        attendanceviewholder.present.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = attendanceList.indexOf(ci);
            attendanceList.remove(position);
            notifyItemRemoved(position);
        }
    });
        attendanceviewholder.absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = attendanceList.indexOf(ci);
                attendanceList.remove(position);
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_card, viewGroup, false);

        return new AttendanceViewHolder(itemView);
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        protected TextView subject;
        protected TextView date;
        protected Button present;
        protected Button absent;

        public AttendanceViewHolder(View v)
        {
            super(v);
            subject = (TextView) v.findViewById(R.id.subjectcard);
            date = (TextView) v.findViewById(R.id.datecard);
            present = (Button) v.findViewById(R.id.presentbutton);
            absent = (Button) v.findViewById(R.id.absentbutton);
        }

    }

}
