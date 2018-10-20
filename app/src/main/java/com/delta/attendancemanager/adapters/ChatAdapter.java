package com.delta.attendancemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delta.attendancemanager.utility.Chat;


public class ChatAdapter extends ArrayAdapter<Chat> {
    public ChatAdapter(Context context, Chat[] objects) {
        super(context, R.layout.chat_layout, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater r = LayoutInflater.from(getContext());
        View v = r.inflate(R.layout.chat_layout, parent, false);
        TextView time=(TextView)v.findViewById(R.id.atimes);
        TextView date=(TextView)v.findViewById(R.id.adates);
        TextView msg=(TextView)v.findViewById(R.id.amsgs);
        Chat temp=getItem(position);
        time.setText(temp.getTime());
        date.setText(temp.getDate());
        msg.setText(temp.getMsg());
        return v;
    }
}
