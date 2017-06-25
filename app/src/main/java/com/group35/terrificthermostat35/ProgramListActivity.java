package com.group35.terrificthermostat35;

/**
 * Created by s163390 on 21-6-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.WeekProgram;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ProgramListActivity extends BasicActivity{
    ExpandableListView myList;
    ArrayList<WeekProgram> ProgramListHeader;
    HashMap<WeekProgram, ArrayList<String>> ProgramListChild;
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

        ProgramListHeader = new ArrayList<WeekProgram>();
        ProgramListChild = new HashMap<WeekProgram ,ArrayList<String>>();

        myAdapter = new MyAdapter(this, ProgramListHeader, ProgramListChild);
        myList = (ExpandableListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter);

        WeekProgram test =data.getCopyOfWeekProgram();
        test.setName("Week Program");
        data.setWeekProgram(test);

        ProgramListHeader.add(data.getCopyOfWeekProgram());

        ArrayList<String> testing = new ArrayList<String>();
        testing.add("test");

        ProgramListChild.put(ProgramListHeader.get(0), testing);


     /*try {
         FileInputStream fis = this.openFileInput("/thermostaat");
        ObjectInputStream is = new ObjectInputStream(fis);
        ProgramList = (ArrayList<WeekProgram>) is.readObject();
        is.close();
        fis.close();
    }
            catch(Exception ex){
            System.out.println (ex.toString());
            System.out.println("Could not read file ");
    }*/



        FloatingActionButton b = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ProgramListHeader.add(new WeekProgram("test"));
                myAdapter.notifyDataSetChanged();

                /*try {
                    FileOutputStream fos = arg0.getContext().openFileOutput("/thermostaat", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(ProgramList);
                    os.close();
                    fos.close();
                }
                catch(Exception ex){
                    System.out.println (ex.toString());
                    System.out.println("Could not write file ");
                }*/

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
