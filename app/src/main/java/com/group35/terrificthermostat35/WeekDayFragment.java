package com.group35.terrificthermostat35;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.projectapi.thermometerapi.Switch;
import com.projectapi.thermometerapi.WeekDay;
import com.projectapi.thermometerapi.WeekProgram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.apptik.widget.MultiSlider;

/**
 * Created by AsciiBunny on 25-6-2017.
 */

public class WeekDayFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_DAY = "section_day";
    private WeekProgram weekProgram;

    public WeekDayFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WeekDayFragment newInstance(int sectionNumber, String day, WeekProgram weekProgram) {
        WeekDayFragment fragment = new WeekDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_DAY, day);
        fragment.setArguments(args);

        fragment.weekProgram = weekProgram;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_week_program, container, false);

        final TextView textView = (TextView) rootView.findViewById(R.id.textView4);

        WeekDay day = WeekDay.parseWeekDay(getArguments().getString(ARG_SECTION_DAY));
        final ArrayList<Switch> switches = new ArrayList<>();
        for (Switch s : weekProgram.weekDaySwitchMap.get(day)) {
            switches.add(s);
            if (!s.getState()) {
                s.time = "24:00";
            }
        }


        final SwitchAdapter switchAdapter = new SwitchAdapter(getActivity(), R.layout.switch_list_item, switches);
        ListView myList = (ListView) rootView.findViewById(R.id.listView);
        myList.setAdapter(switchAdapter);
        final MultiSlider multiSlider = (MultiSlider) rootView.findViewById(R.id.range_slider5);
        multiSlider.clearThumbs();

        /*
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker picker, int hourOfDay, int minute) {
                        int time = hourOfDay * 60 + minute;

                        Switch changedSwitch = switches.get(position);
                        changedSwitch.time = timeToString(time);

                        sortSwitches(switches, multiSlider);

                        switchAdapter.notifyDataSetChanged();
                    }
                }, 12, 0, true).show();
            }
        });
        */



        multiSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                textView.setText(timeToString(value));
                switches.get(thumbIndex).state = value != 60 * 24;
                switches.get(thumbIndex).time = timeToString(value);
                switchAdapter.notifyDataSetInvalidated();
            }
        });

        multiSlider.clearThumbs();
        boolean isDay = false;
        for (Switch s : switches) {
            MultiSlider.Thumb thumb = multiSlider.addThumb(stringToRangeTime(s.getTime()));
            thumb.setRange(new ColorDrawable(getResources().getColor(isDay ? R.color.colorDay : R.color.colorNight)));
            isDay = !isDay;
        }


        // creates popup to ask for time of switch
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //INSERT TIME
                int time = hourOfDay * 60 + minute;
                boolean active = true;

                for (int i = 0; i < 10; i++) {
                    Switch s = switches.get(i);
                    MultiSlider.Thumb thumb = multiSlider.getThumb(i);
                    int sTime = thumb.getValue();
                    if (sTime > time) {
                        s.time = timeToString(time);
                        s.state = true;
                        thumb.setValue(time);
                        time = sTime;
                    }
                }

                switchAdapter.notifyDataSetChanged();

            }
        }, 12, 0, true);


        FloatingActionButton b = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                timePickerDialog.show();
            }

        });

        return rootView;
    }

    private void sortSwitches(ArrayList<Switch> switches, MultiSlider multiSlider) {
        Switch switchA = switches.get(0);
        int timeA = stringToRangeTime(switchA.getTime());

        for (int i = 1; i < 10; i++) {
            Switch switchB = switches.get(i);
            int timeB = stringToRangeTime(switchB.getTime());

            Log.i("timeA", timeA + "");
            Log.i("timeB", timeB + "");

            if (timeA > timeB) {
                switchA.time = timeToString(timeB);
                switchB.time = timeToString(timeA);
                timeB = timeA;
            } else if(timeA == 60 * 24) {
                switchA.state = false;
                return;
            }
            switchA = switchB;
            timeA = timeB;
        }
        rebindThumbs(switches, multiSlider);
    }

    private void rebindThumbs(final ArrayList<Switch> switches, final MultiSlider multiSlider) {

        multiSlider.post(new Runnable() {
            @Override
            public void run() {
                multiSlider.clearThumbs();
                boolean isDay = false;
                for (Switch s : switches) {
                    MultiSlider.Thumb thumb = multiSlider.addThumb(stringToRangeTime(s.getTime()));
                    thumb.setRange(new ColorDrawable(getResources().getColor(isDay ? R.color.colorDay : R.color.colorNight)));
                    isDay = !isDay;
                }
            }
        });
    }

    private String timeToString(int rangeTime) {
        return timeToString(rangeTime / 60, rangeTime % 60);
    }

    private String timeToString(int hours, int minutes) {
        return hours + ":" + (minutes < 10 ? "0" + minutes : minutes);
    }

    private int stringToRangeTime(String time) {
        return hoursFromString(time) * 60 + minutesFromString(time);
    }

    private int hoursFromString(String time) {
        return Integer.parseInt(time.substring(0, time.indexOf(":")));
    }

    private int minutesFromString(String time) {
        return Integer.parseInt(time.substring(time.indexOf(":") + 1));
    }
}
