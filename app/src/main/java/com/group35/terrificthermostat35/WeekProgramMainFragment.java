package com.group35.terrificthermostat35;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

        EditText nameInput = (EditText) rootView.findViewById(R.id.editTextName);
        final EditText dayInput = (EditText) rootView.findViewById(R.id.editTextDay);
        final EditText nightInput = (EditText) rootView.findViewById(R.id.editTextNight);

        nameInput.setText(weekProgram.getName());
        nameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String value = v.getText().toString();
                    weekProgram.name = value;
                    ((WeekProgramActivity)getActivity()).setActionBarTitle(value);
                    handled = true;
                    hideKeyboard(getActivity());
                }
                return handled;
            }
        });

        dayInput.setText(weekProgram.dayTemperature + "");
        dayInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    double value = Double.parseDouble(dayInput.getText().toString());
                    value = limit(value);
                    v.setText(value + "");
                    weekProgram.dayTemperature = value;
                    handled = true;
                    hideKeyboard(getActivity());
                }
                return handled;
            }
        });

        nightInput.setText(weekProgram.nightTemperature + "");
        nightInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    double value = Double.parseDouble(nightInput.getText().toString());
                    value = limit(value);
                    v.setText(value + "");
                    weekProgram.nightTemperature = value;
                    handled = true;
                    hideKeyboard(getActivity());
                }
                return handled;
            }
        });


        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser)
            hideKeyboard(getActivity());
    }

    public double limit(double value)  {
        value = Math.min(value, 30.0);
        value = Math.max(value, 5.0);
        value += 0.05;
        int i = (int) (value * 10);
        double r = (double)i / 10;
        return r;
    }

    public static void hideKeyboard(Context ctx) {
        if (ctx == null)
            return;

        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
