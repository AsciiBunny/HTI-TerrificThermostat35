package com.group35.terrificthermostat35;
/**
 * Created by s163390 on 23-6-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.projectapi.thermometerapi.WeekProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MyAdapter extends BaseExpandableListAdapter {

    private Context context;
    private TerrificApplication app;
    private ArrayList<String> names;

    public MyAdapter(Context context, TerrificApplication app, String[] ProgramListHeader) {
        this.context = context;
        this.app = app;
        this.names = new ArrayList<>(Arrays.asList(ProgramListHeader));
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.names.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.exp_list_child, null);
        }

        Button active = (Button) convertView.findViewById(R.id.activeButton);
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeekProgram weekProgram = app.getFileManager().get(names.get(groupPosition));
                if (weekProgram != null) {
                    app.getThermostatData().setWeekProgram(weekProgram);
                }
            }
        });

        Button edit = (Button) convertView.findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WeekProgramActivity.class);
                // hij moet nog iets van data doorsturen, weet niet of de volgende regel dat helemaal doet?
                intent.putExtra(WeekProgramActivity.WEEKPROGRAM_NAME_MESSAGE, names.get(groupPosition));
                v.getContext().startActivity(intent);
            }
        });

        Button delete = (Button) convertView.findViewById(R.id.delButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getFileManager().delete(names.get(groupPosition));
                names.remove(groupPosition); //or some other task
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.names.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.names.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_list_head, null);
        }

        TextView textView = (TextView) convertView
                .findViewById(R.id.textView);

        textView.setText(names.get(groupPosition));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}







