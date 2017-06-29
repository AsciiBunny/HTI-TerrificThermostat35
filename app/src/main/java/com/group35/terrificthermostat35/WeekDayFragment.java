package com.group35.terrificthermostat35;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.projectapi.thermometerapi.SwitchType;
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

    private ArrayList<Switch> switches;

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

        final View rootView = inflater.inflate(R.layout.fragment_week_program, container, false);

        final TextView textView = (TextView) rootView.findViewById(R.id.textView4);
        final MultiSlider multiSlider = (MultiSlider) rootView.findViewById(R.id.range_slider5);

        WeekDay day = WeekDay.parseWeekDay(getArguments().getString(ARG_SECTION_DAY));
        switches = new ArrayList<>();
        for (Switch s : weekProgram.weekDaySwitchMap.get(day)) {
            switches.add(s);
            if (!s.getState()) {
                s.time = "24:00";
            }
        }


        final SwitchAdapter switchAdapter = new SwitchAdapter(getActivity(), R.layout.switch_list_item, switches, new Runnable() {
            @Override
            public void run() {
                sortSwitches(switches);
                rebindThumbs(switches, multiSlider);
            }
        });
        ListView myList = (ListView) rootView.findViewById(R.id.listView);
        myList.setAdapter(switchAdapter);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Switch changedSwitch = switches.get(position);
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker picker, int hourOfDay, int minute) {
                        int time = hourOfDay * 60 + minute;
                        changedSwitch.time = timeToString(time);

                        sortSwitches(switches);
                        multiSlider.post(new Runnable() {
                            @Override
                            public void run() {
                                rebindThumbs(switches, multiSlider);
                            }
                        });

                        switchAdapter.notifyDataSetChanged();
                    }
                }, 12, 0, true);
                dialog.updateTime(hoursFromString(changedSwitch.getTime()), minutesFromString(changedSwitch.getTime()));
                dialog.show();
            }
        });


        multiSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                textView.setText(timeToString(value));
                switches.get(thumbIndex).state = value != 60 * 24;
                switches.get(thumbIndex).time = timeToString(value);
                switchAdapter.notifyDataSetInvalidated();
            }
        });

        /*
        boolean isDay = false;
        for (Switch s : switches) {
            MultiSlider.Thumb thumb = multiSlider.addThumb(stringToRangeTime(s.getTime()));
            thumb.setRange(new ColorDrawable(getResources().getColor(isDay ? R.color.colorDay : R.color.colorNight)));
            isDay = !isDay;
        }
        */
        sortSwitches(switches);
        rebindThumbs(switches, multiSlider);


        // creates popup to ask for time of switch
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Switch inactive = getInactiveSwitch(switches);

                if (inactive != null) {
                    inactive.state = true;
                    inactive.time = timeToString(hourOfDay, minute);

                    sortSwitches(switches);
                    multiSlider.post(new Runnable() {
                        @Override
                        public void run() {
                            rebindThumbs(switches, multiSlider);
                        }
                    });

                    switchAdapter.notifyDataSetChanged();
                }

            }
        }, 12, 0, true);


        FloatingActionButton b = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (getInactiveSwitch(switches) != null) {
                    timePickerDialog.show();
                } else {
                    Snackbar.make(rootView, "You already have the maximum number of switches for this day!", Snackbar.LENGTH_SHORT).show();
                }
            }

        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (switches != null && !visible)
            Save();
    }

    public void onDetach() {
        super.onDetach();
        Save();
    }

    public void Save() {
        WeekDay day = WeekDay.parseWeekDay(getArguments().getString(ARG_SECTION_DAY));
        Log.i("Saving", "Writing " + day.name() + " to program " + weekProgram.name);
        weekProgram.weekDaySwitchMap.put(day, switches.toArray(new Switch[10]));
    }

    private void sortSwitches(ArrayList<Switch> switches) {
        Collections.sort(switches, new Comparator<Switch>() {
            @Override
            public int compare(Switch s1, Switch s2) {
                Integer time1 = stringToRangeTime(s1.getTime());
                Integer time2 = stringToRangeTime(s2.getTime());
                return time1.compareTo(time2);
            }
        });
        boolean isDay = true;
        for (Switch s : switches) {
            s.type = isDay ? SwitchType.DAY : SwitchType.NIGHT;
            isDay = !isDay;
        }
    }

    private void rebindThumbs(final ArrayList<Switch> switches, final MultiSlider multiSlider) {
        multiSlider.clearThumbs();
        for (Switch s : switches) {
            MultiSlider.Thumb thumb = multiSlider.addThumb(stringToRangeTime(s.getTime()));
        }

        boolean isDay = false;
        for (int i = 0; i < switches.size(); i++) {
            multiSlider.getThumb(i).setRange(new ColorDrawable(getResources().getColor(isDay ? R.color.colorDay : R.color.colorNight)));
            isDay = !isDay;
            multiSlider.getThumb(i).setValue(multiSlider.getThumb(i).getValue());
        }
        multiSlider.invalidate();
    }

    private Switch getInactiveSwitch(ArrayList<Switch> switches) {
        for (Switch s : switches) {
            if (!s.state)
                return s;
        }
        return null;
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
