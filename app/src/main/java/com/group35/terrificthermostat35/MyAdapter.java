package com.group35.terrificthermostat35; /**
 * Created by s163390 on 23-6-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.group35.terrificthermostat35.ListItem;
import com.group35.terrificthermostat35.R;
import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.WeekProgram;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.list;
import static com.group35.terrificthermostat35.R.color.colorPrimaryDark;

public class MyAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<WeekProgram> _ProgramListHeader;
    private HashMap<WeekProgram, ArrayList<String>> _ProgramListChild;

    ArrayList<WeekProgram> ProgramList = new ArrayList<>();

    public MyAdapter(Context context, ArrayList<WeekProgram> ProgramListHeader,
                                 HashMap<WeekProgram, ArrayList<String>> ProgramListChild) {
        this._context = context;
        this._ProgramListHeader = ProgramListHeader;
        this._ProgramListChild = ProgramListChild;
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._ProgramListChild.get(this._ProgramListHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_list_child, null);
        }

        Button active = (Button) convertView.findViewById(R.id.activeButton);
        Button edit = (Button) convertView.findViewById(R.id.editButton);
        Button delete = (Button) convertView.findViewById(R.id.delButton);
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("CLICKED POSITION", Integer.valueOf(pos).toString());
                //View parent = (View) v.getParent();
                //parent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));

                // Something that sets weekprogram to active???

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WeekProgramActivity.class);
                // hij moet nog iets van data doorsturen, weet niet of de volgende regel dat helemaal doet?
                intent.putExtra(WeekProgramActivity.WEEKPROGRAM_NAME_MESSAGE, WeekProgram.DEFAULT_NAME);
                v.getContext().startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ProgramListHeader.remove(groupPosition); //or some other task
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._ProgramListChild.get(this._ProgramListHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._ProgramListHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._ProgramListHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_list_head, null);
        }

        TextView textView = (TextView) convertView
                .findViewById(R.id.textView);

        textView.setText(_ProgramListHeader.get(groupPosition).getName());

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

    /*public MyAdapter(Context context, int textViewResourceId, ArrayList<WeekProgram> objects) {
        super(context, textViewResourceId, objects);
        ProgramList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.simple_program_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        textView.setText(ProgramList.get(position).getName());

        Button active = (Button) v.findViewById(R.id.activeButton);
        Button edit = (Button) v.findViewById(R.id.editButton);
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED POSITION", Integer.valueOf(pos).toString());
                View parent = (View) v.getParent();
                parent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));

                // Something that sets weekprogram to active???

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WeekProgramActivity.class);
                // hij moet nog iets van data doorsturen, weet niet of de volgende regel dat helemaal doet?
                intent.putExtra(WeekProgramActivity.WEEKPROGRAM_NAME_MESSAGE, WeekProgram.DEFAULT_NAME);
                v.getContext().startActivity(intent);
            }
        });


        return v;
    }*/







