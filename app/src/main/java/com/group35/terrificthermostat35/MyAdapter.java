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
import android.widget.Button;
import android.widget.TextView;

import com.group35.terrificthermostat35.ListItem;
import com.group35.terrificthermostat35.R;
import com.projectapi.thermometerapi.WeekProgram;

import java.util.ArrayList;

import static android.R.id.list;
import static com.group35.terrificthermostat35.R.color.colorPrimaryDark;

public class MyAdapter extends ArrayAdapter<WeekProgram> {

    ArrayList<WeekProgram> ProgramList = new ArrayList<>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList<WeekProgram> objects) {
        super(context, textViewResourceId, objects);
        ProgramList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos= position;
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.simple_program_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        textView.setText(ProgramList.get(position).getName());

        Button active = (Button) v.findViewById(R.id.activeButton);
        Button edit = (Button) v.findViewById(R.id.editButton);
        active.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Log.d("CLICKED POSITION",Integer.valueOf(pos).toString());
                View parent =(View) v.getParent();
                parent.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryLight));

                // Something that sets weekprogram to active
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(v.getContext(), WeekProgramActivity.class);
                intent.putExtra(WeekProgramActivity.WEEKPROGRAM_NAME_MESSAGE, WeekProgram.DEFAULT_NAME);
                v.getContext().startActivity(intent);
            }
        });


        return v;
    }


}
