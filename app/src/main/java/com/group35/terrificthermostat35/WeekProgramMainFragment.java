package com.group35.terrificthermostat35;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.projectapi.thermometerapi.Switch;
import com.projectapi.thermometerapi.WeekDay;
import com.projectapi.thermometerapi.WeekProgram;


public class WeekProgramMainFragment extends Fragment {

    WeekProgram weekProgram;

    public WeekProgramMainFragment() {
    }

    public static WeekProgramMainFragment newInstance(String name, WeekProgram weekProgram) {
        WeekProgramMainFragment fragment = new WeekProgramMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.weekProgram = weekProgram;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_week_program_main, container, false);

        EditText nameInput = (EditText) rootView.findViewById(R.id.editTextDay);
        EditText dayInput = (EditText) rootView.findViewById(R.id.editTextName);
        EditText nightInput = (EditText) rootView.findViewById(R.id.editTextNight);

        



        return rootView;
    }

    public void onDetach() {
        super.onDetach();
        Save();
    }

    public void Save() {
        TerrificApplication app = (TerrificApplication) getActivity().getApplication();
        app.getThermostatData().setWeekProgram(weekProgram);
    }

    public int limit(Editable edit, int min, int max) {
        int x = Integer.parseInt(edit.toString());
        x = Math.min(x, max);
        x = Math.max(min, x);
        return x;
    }
}
