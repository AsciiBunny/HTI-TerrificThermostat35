package com.group35.terrificthermostat35;

/**
 * Created by s163390 on 21-6-2017.
 */

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProgramList extends AppCompatActivity{
    //ExpandableRelativeLayout expandableLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_list);


    final ConstraintLayout mainLayout = (ConstraintLayout)findViewById(R.id.program_list);

    /*View view = getLayoutInflater().inflate(R.layout.fragment_program_list, mainLayout,false);

    mainLayout.addView(view);*/

    final LinearLayout ll = (LinearLayout) findViewById(R.id.program_list1);


    Button b=(Button) findViewById(R.id.button);
    b.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Context c=getApplicationContext();
            TextView tvv=new TextView(c);
            tvv.setText("textview");
            ll.addView(tvv);*/
            View view = getLayoutInflater().inflate(R.layout.fragment_program_list, ll,false);

            ll.addView(view);
        }
    });
    }

    /*public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }*/


}
