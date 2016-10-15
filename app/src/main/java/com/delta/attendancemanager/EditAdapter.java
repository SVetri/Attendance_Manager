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

// TODO: editing the absent and present accordingly and updating the attendance database.

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.EditViewHolder> {

    private List<EditCardInfo> attendanceList;
    private Context context;
    Date date = null;
    AtAdapter atAdapter;

    public EditAdapter(Context context, List<EditCardInfo> contactList) {
        this.context = context;
        this.attendanceList = contactList;
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    @Override
    public void onBindViewHolder(final EditViewHolder attendanceviewholder, final int i) {
        final EditCardInfo eci = attendanceList.get(i);
        attendanceviewholder.sub[0].setText(eci.coursename);
        attendanceviewholder.sub[1].setText(eci.classdate);
        attendanceviewholder.sub[2].setText(eci.classtime);
        if (eci.attendance.equals(true)) {
            attendanceviewholder.sub[3].setText("PRESENT");
            attendanceviewholder.sub[4].setText("Mark as Absent");
        } else {
            attendanceviewholder.sub[3].setText("ABSENT");
            attendanceviewholder.sub[4].setText("Mark as Present");
        }
        attendanceviewholder.sub[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atAdapter = new AtAdapter(context);
                try {
                    date = new Date(eci.classdate + " " + eci.classtime);
                } catch (Exception e) {
                    Toast.makeText(context, "date problem", Toast.LENGTH_LONG).show();
                }
                if (eci.attendance.equals(true)) {
                    eci.attendance = false;
                    atAdapter.update_attendance(eci.coursename, formatDate(eci.classdate + " " + eci.classtime), -1);
                    attendanceviewholder.sub[3].setText("ABSENT");
                    attendanceviewholder.sub[4].setText("Mark as Present");
                } else {
                    eci.attendance = true;
                    atAdapter.update_attendance(eci.coursename, formatDate(eci.classdate + " " + eci.classtime), 1);
                    attendanceviewholder.sub[3].setText("PRESENT");
                    attendanceviewholder.sub[4].setText("Mark as Absent");
                }
            }
        });
        attendanceviewholder.sub[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atAdapter = new AtAdapter(context);
                atAdapter.delete_data(eci.coursename, formatDate(eci.classdate + " " + eci.classtime));                                                         //TODO: Refreshing the screen should be added if required
                int position = attendanceList.indexOf(eci);
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
    public EditViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_editcard, viewGroup, false);

        return new EditViewHolder(itemView);
    }

    protected class EditViewHolder extends RecyclerView.ViewHolder {

        protected TextView sub[] = new TextView[6];

        public EditViewHolder(View v) {
            super(v);
            int[] subsInt = {R.id.subjectcard, R.id.datecard, R.id.timecard, R.id.mark, R.id.changebutton, R.id.removebutton};

            for (int i = 0; i < sub.length; i++) {
                sub[i] = (TextView) v.findViewById(subsInt[i]);
            }
        }
    }

}
