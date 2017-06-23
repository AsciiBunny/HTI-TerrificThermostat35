package com.group35.terrificthermostat35; /**
 * Created by s163390 on 23-6-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group35.terrificthermostat35.ListItem;
import com.group35.terrificthermostat35.R;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<ListItem> {

    ArrayList<ListItem> ProgramList = new ArrayList<>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList<ListItem> objects) {
        super(context, textViewResourceId, objects);
        ProgramList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.simple_program_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        textView.setText(ProgramList.get(position).getProgramName());
        TextView textView1 = (TextView) v.findViewById(R.id.textView1);
        textView1.setText(ProgramList.get(position).getProgramName1());
        return v;
    }
}
