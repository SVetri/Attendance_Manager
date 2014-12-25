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
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    private List<SubjectInfo> subjectList;

    public SubjectAdapter(List<SubjectInfo> subList) {
        this.subjectList = subList;
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder subjectviewholder, int i)
    {
        SubjectInfo si = subjectList.get(i);
        subjectviewholder.subject.setText(si.subjectcode + ": " + si.subjectname);
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
