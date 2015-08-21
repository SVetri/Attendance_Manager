package com.delta.attendancemanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ManageCourse extends ActionBarActivity {
    MySqlHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_course);
        ListView l=(ListView)findViewById(R.id.courses);
        ArrayList<String> s =new ArrayList();
        handler=new MySqlHandler(this,null);
        s=handler.getSubs();
        String[] a =new String[s.size()];
        a=s.toArray(a);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,a);
        l.setAdapter(adapter);

    }


}
