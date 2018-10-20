package com.delta.attendancemanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delta.attendancemanager.Info.EditCardInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// TODO: editing the absent and present accordingly and updating the attendance database.

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.EditViewHolder> {

    SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy H:m");
    private List<EditCardInfo> attendanceList;
    private Context context;
    Date date = null;
    String format = "yyyy-MM-dd HH:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    AtAdapter atAdapter;

    public EditAdapter(Context context,List<EditCardInfo> contactList) {
        this.context = context;
        this.attendanceList = contactList;
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    @Override
    public void onBindViewHolder(final EditViewHolder attendanceviewholder, final int i)
    {
        final EditCardInfo eci = attendanceList.get(i);
        attendanceviewholder.subject.setText(eci.coursename);
        attendanceviewholder.date.setText(eci.classdate);
        attendanceviewholder.time.setText(eci.classtime);
        if(eci.attendance.equals(true))
        {
            attendanceviewholder.mark.setText("PRESENT");
            attendanceviewholder.change.setText("Mark as Absent");
        }
        else
        {
            attendanceviewholder.mark.setText("ABSENT");
            attendanceviewholder.change.setText("Mark as Present");
        }
        attendanceviewholder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atAdapter = new AtAdapter(context);
                try{
                    date = sdf1.parse(eci.classdate+" "+eci.classtime);
                }
                catch(Exception e){
                    Toast.makeText(context, "date problem", Toast.LENGTH_LONG).show();
                }
                if(eci.attendance.equals(true))
                    {
                        eci.attendance = false;
                        atAdapter.update_attendance(eci.coursename,sdf.format(date),-1);
                        attendanceviewholder.mark.setText("ABSENT");
                        attendanceviewholder.change.setText("Mark as Present");
                    }
                    else
                    {
                        eci.attendance = true;
                        atAdapter.update_attendance(eci.coursename,sdf.format(date),1);
                        attendanceviewholder.mark.setText("PRESENT");
                        attendanceviewholder.change.setText("Mark as Absent");
                    }
            }
        });
        attendanceviewholder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atAdapter = new AtAdapter(context);
                try{
                    date = sdf1.parse(eci.classdate+" "+eci.classtime);
                }
                catch(Exception e){
                    Toast.makeText(context, "date problem", Toast.LENGTH_LONG).show();
                }
                atAdapter.delete_data(eci.coursename,sdf.format(date));                                                         //TODO: Refreshing the screen should be added if required
                int position = attendanceList.indexOf(eci);
                attendanceList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public EditViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_editcard, viewGroup, false);

        return new EditViewHolder(itemView);
    }

    public class EditViewHolder extends RecyclerView.ViewHolder {

        protected TextView subject;
        protected TextView date;
        protected TextView time;
        protected TextView mark;
        protected Button change;
        protected Button remove;

        public EditViewHolder(View v)
        {
            super(v);
            subject = (TextView) v.findViewById(R.id.subjectcard);
            date = (TextView) v.findViewById(R.id.datecard);
            time = (TextView) v.findViewById(R.id.timecard);
            mark = (TextView) v.findViewById(R.id.mark);
            change = (Button) v.findViewById(R.id.changebutton);
            remove = (Button) v.findViewById(R.id.removebutton);
        }

    }

}
