package com.group35.terrificthermostat35;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectapi.thermometerapi.Switch;
import com.projectapi.thermometerapi.WeekProgram;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by s163390 on 25-6-2017.
 */

public class SwitchAdapter extends ArrayAdapter<SwitchItem> {

    ArrayList<SwitchItem> SwitchList = new ArrayList<>();

    public SwitchAdapter(Context context, int textViewResourceId, ArrayList<SwitchItem> objects) {
         super(context, textViewResourceId, objects);
         SwitchList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.switch_list_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        imageView.setImageResource(SwitchList.get(position).getImage());
        textView.setText(SwitchList.get(position).getTime());
        return v;
    }
}
