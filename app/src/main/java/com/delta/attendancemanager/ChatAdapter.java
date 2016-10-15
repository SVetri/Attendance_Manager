package com.delta.attendancemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ChatAdapter extends ArrayAdapter<Chat> {
    public ChatAdapter(Context context, Chat[] objects) {
        super(context, R.layout.chat_layout, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater r = LayoutInflater.from(getContext());
        View v = r.inflate(R.layout.chat_layout, parent, false);

        int [] subsInt = {R.id.atimes, R.id.adates, R.id.amsgs};
        TextView sub [] = new TextView[3];
        for (int i= 0; i < sub.length; i++){
            sub[i] = (TextView) v.findViewById(subsInt[i]);
        }

        Chat temp=getItem(position);
        sub[0].setText(temp.getTime());
        sub[1].setText(temp.getDate());
        sub[2].setText(temp.getMsg());
        return v;
    }
}
