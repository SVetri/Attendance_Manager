package com.delta.attendancemanager;

import android.content.Context;
import android.content.Intent;
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
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    private List<SubjectInfo> subjectList;
    Context cont;

    public SubjectAdapter(List<SubjectInfo> subList, Context context) {
        this.subjectList = subList;
        cont = context;
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder subjectviewholder, final int i)
    {
        SubjectInfo si = subjectList.get(i);
        subjectviewholder.subject.setText(si.subjectname);

        subjectviewholder.subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent( cont, SubjectAttendance.class);
                if(i%2==0)
                {
                    in.putExtra("pendupd", true);
                }
                cont.startActivity(in);
            }
        });
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_item_subjectlist, viewGroup, false);

        return new SubjectViewHolder(itemView);
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {

        protected Button subject;

        public SubjectViewHolder(View v)
        {
            super(v);
            subject = (Button) v.findViewById(R.id.subjectbutton);
        }

    }
}
