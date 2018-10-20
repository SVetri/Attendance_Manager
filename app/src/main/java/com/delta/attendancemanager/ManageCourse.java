package com.delta.attendancemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.delta.attendancemanager.adapters.MySqlAdapter;

import java.util.ArrayList;

public class ManageCourse extends ActionBarActivity {
    MySqlAdapter handler;
    boolean ischanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                                //TODO delete_subs created, use it to delete a subject
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_course);
        ischanged=false;
        Button add=(Button)findViewById(R.id.addbutton);
        handler=new MySqlAdapter(this,null);
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManageCourse.this);
                        final EditText t=new EditText(ManageCourse.this);
                        t.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(t);
                        builder.setMessage("Enter the Name of the course")
                                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        handler.add_sub(t.getText().toString());
                                        ischanged=true;
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        builder.show();
                    }
                }
        );
        ListView l=(ListView)findViewById(R.id.courses);
        ArrayList<String> s =new ArrayList();

        s=handler.getSubs();

         String[] a =new String[s.size()];
        a=s.toArray(a);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,a);
        l.setAdapter(adapter);
        l.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String subs=(String)parent.getItemAtPosition(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManageCourse.this);
                        builder.setMessage("Do you really want to delete the Course "+subs+" ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //delete Subject
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        builder.show();
                        return  true;
                    }
                }


        );

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(ischanged){
            //update server
        }
    }
}
