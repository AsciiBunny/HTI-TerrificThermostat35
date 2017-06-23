package com.group35.terrificthermostat35;

/**
 * Created by s163390 on 21-6-2017.
 */

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.id.list;

public class ProgramList extends AppCompatActivity{
    ListView myList;
    ArrayList<ListItem> ProgramList;
    MyAdapter myAdapter;


    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    startActivity(new Intent(ProgramList.this, MainActivity.class));
                    return true;
                case R.id.nav_weekprog:
                    startActivity(new Intent(ProgramList.this, ProgramList.class));
                    return true;
                case R.id.nav_settings:
                    startActivity(new Intent(ProgramList.this, SettingsActivity.class));
                    return true;
            }
            return false;
        }

    };
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_list);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ProgramList=new ArrayList<>();

        myAdapter=new MyAdapter(this,R.layout.simple_program_item, ProgramList);
        myList=(ListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter);

        ProgramList.add(new ListItem("Current program","Example Program"));

        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ProgramList.add(new ListItem("", "Something"));
                myAdapter.notifyDataSetChanged();
            }

        });

    }
}
