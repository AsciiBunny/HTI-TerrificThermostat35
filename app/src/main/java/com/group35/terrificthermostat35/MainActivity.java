package com.group35.terrificthermostat35;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int vtemp = 21;
    int cTempHold;
    TextView cTemp;
    TextView sTemp;
    private BottomNavigationView mBottomNav;


    public static final String EXTRA_MESSAGE = "com.example.s157888.terrificthermostat35.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cTemp = (TextView) findViewById(R.id.cTemp);
        sTemp = (TextView) findViewById(R.id.sTemp);
        Button plus = (Button) findViewById(R.id.plus);
        Button minus = (Button) findViewById(R.id.minus);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vtemp++;
                cTempHold = 11;
                cTemp.setText(vtemp + " \u2103");
                sTemp.setText(vtemp + " \u2103");
                new CountDownTimer(1000, 1000) {
                    public void onTick(long milisUntilFinished){

                    }
                    public void onFinish() {
                            cTemp.setText(cTempHold + " \u2103");
                    }
                }.start();



            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*vtemp--;
                cTemp.setText(vtemp + " \u2103");
                sTemp.setText(vtemp + " \u2103");
                new CountDownTimer(1000, 1000) {
                    public void onTick(long milisUntilFinished){

                    }
                    public void onFinish() {
                        cTemp.setText(cTempHold + " \u2103");
                    }
                }.start();*/

                startActivity(new Intent(MainActivity.this, WeekProgramActivity.class));

            }
        });



    }
}
