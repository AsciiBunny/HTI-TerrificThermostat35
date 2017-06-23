package com.group35.terrificthermostat35;

/**
 * Created by s163390 on 21-6-2017.
 */

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ProgramListActivity extends AppCompatActivity{
    ListView myList;
    ArrayList<ListItem> ProgramList;
    MyAdapter myAdapter;


    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    startActivity(new Intent(ProgramListActivity.this, MainActivity.class));
                    return true;
                case R.id.nav_weekprog:
                    startActivity(new Intent(ProgramListActivity.this, ProgramListActivity.class));
                    return true;
                case R.id.nav_settings:
                    startActivity(new Intent(ProgramListActivity.this, SettingsActivity.class));
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
                startActivity(new Intent(ProgramListActivity.this, WeekProgramActivity.class));
            }

        });

    }
}
