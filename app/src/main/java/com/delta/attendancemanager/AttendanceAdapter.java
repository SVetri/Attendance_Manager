package com.delta.attendancemanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<CardInfo> attendanceList;
    AtAdapter atAdapter;
    Context context;
    Date date = null,date2,date1;
    String format = "yyyy-MM-dd HH:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(format),sdf1;

    public AttendanceAdapter(List<CardInfo> contactList,Context context) {
        this.attendanceList = contactList;
        this.context = context;
        atAdapter = new AtAdapter(context);
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
        attendanceviewholder.time.setText(ci.classtime);

        sdf1 = new SimpleDateFormat("d/M/yy H:m");

        date1 = null;
        date2 = null;
        //date = new Date(date1.getYear(),date1.getMonth(),date1.getDate(),date2.getHours(),date2.getMinutes());
        attendanceviewholder.present.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                date1 = sdf1.parse(ci.classdate+" "+ci.classtime);
            }
            catch(Exception e){
                Toast.makeText(context,"date problem",Toast.LENGTH_LONG).show();
            }
            Toast.makeText(context,date1.toString(),Toast.LENGTH_LONG).show();
            int position = attendanceList.indexOf(ci);
            Toast.makeText(context,sdf.format(date1),Toast.LENGTH_LONG).show();
            atAdapter.update_attendance(ci.coursename, sdf.format(date1), 1);
            atAdapter.fetch_pending_data();
            attendanceList.remove(position);
            notifyItemRemoved(position);
        }
    });
        attendanceviewholder.absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = attendanceList.indexOf(ci);
                atAdapter.update_attendance(ci.coursename, sdf.format(date), -1);
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
        protected TextView time;
        protected Button present;
        protected Button absent;

        public AttendanceViewHolder(View v)
        {
            super(v);
            subject = (TextView) v.findViewById(R.id.subjectcard);
            date = (TextView) v.findViewById(R.id.datecard);
            time = (TextView) v.findViewById(R.id.timecard);
            present = (Button) v.findViewById(R.id.presentbutton);
            absent = (Button) v.findViewById(R.id.absentbutton);
        }

    }

}
