package com.group35.terrificthermostat35;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.projectapi.thermometerapi.ThermostatClient;
import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.WeekDay;

import java.util.Calendar;


public class SettingsActivity extends BasicActivity {

    ImageButton setTimeButton;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    return true;
                case R.id.nav_weekprog:
                    startActivity(new Intent(SettingsActivity.this, ProgramListActivity.class));
                    return true;
                case R.id.nav_settings:
                    //startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        ImageButton plus = (ImageButton) findViewById(R.id.plus);
        ImageButton minus = (ImageButton) findViewById(R.id.minus);
        final TextView sDay = (TextView) findViewById(R.id.sDay);
        final TextView sTime = (TextView) findViewById(R.id.sTime);



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null) {
                    if (data.getWeekDay() == WeekDay.MONDAY) {
                        data.setWeekDay(WeekDay.TUESDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.TUESDAY) {
                        data.setWeekDay(WeekDay.WEDNESDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.WEDNESDAY) {
                        data.setWeekDay(WeekDay.THURSDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.THURSDAY) {
                        data.setWeekDay(WeekDay.FRIDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.FRIDAY) {
                        data.setWeekDay(WeekDay.SATURDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.SATURDAY) {
                        data.setWeekDay(WeekDay.SUNDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else {
                        data.setWeekDay(WeekDay.MONDAY);
                        sDay.setText((data.getWeekDay().name));
                    }
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null) {
                    if (data.getWeekDay() == WeekDay.MONDAY) {
                        data.setWeekDay(WeekDay.SUNDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.TUESDAY) {
                        data.setWeekDay(WeekDay.MONDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.WEDNESDAY) {
                        data.setWeekDay(WeekDay.TUESDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.THURSDAY) {
                        data.setWeekDay(WeekDay.WEDNESDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.FRIDAY) {
                        data.setWeekDay(WeekDay.THURSDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else if (data.getWeekDay() == WeekDay.SATURDAY) {
                        data.setWeekDay(WeekDay.FRIDAY);
                        sDay.setText((data.getWeekDay().name));
                    } else {
                        data.setWeekDay(WeekDay.SATURDAY);
                        sDay.setText((data.getWeekDay().name));
                    }
                }
            }
        });

        setTimeButton = (ImageButton) findViewById(R.id.setTimeButton);

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog TimePicker;
                TimePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String newHour = Integer.toString(selectedHour);
                        String newMinute = Integer.toString(selectedMinute);
                        data.setTime(newHour + ":" + newMinute);
                        sTime.setText(data.getTime());
                    }
                }, hour, minute, true);
                TimePicker.show();

            }
        });
    }

    @Override
    public void onThermostatDataUpdate(ThermostatData thermostatData) {

        TextView sDay = (TextView) findViewById(R.id.sDay);
        TextView sTime = (TextView) findViewById(R.id.sTime);

        if (thermostatData != null) {
            data = thermostatData;
            sDay.setText((thermostatData.getWeekDay().name));
            sTime.setText((thermostatData.getTime()));
        } else {
            Log.e("MainActivityDataHandler", "ThermostatData is null");
        }
    }
}
