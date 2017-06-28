package com.group35.terrificthermostat35;

/**
 * Created by s163390 on 21-6-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.WeekProgram;

public class ProgramListActivity extends BasicActivity {
    ExpandableListView myList;
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
                    //startActivity(new Intent(ProgramListActivity.this, ProgramListActivity.class));
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

        String[] names = app.getFileManager().getAllWeekProgramNames();

        myAdapter = new MyAdapter(this, app, names);
        myList = (ExpandableListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter);

        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ProgramListActivity.this, WeekProgramActivity.class);
                intent.putExtra(WeekProgramActivity.WEEKPROGRAM_NAME_MESSAGE, WeekProgram.DEFAULT_NAME);
                startActivity(intent);
            }

        });

    }

    @Override
    public void onThermostatDataUpdate(ThermostatData thermostatData) {
        if (thermostatData != null) {
            data = thermostatData;
        }
    }

}
