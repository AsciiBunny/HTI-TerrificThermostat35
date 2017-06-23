package com.group35.terrificthermostat35;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    double vtemp = 21.0;
    TextView cTemp;
    TextView sTemp;
    //private BottomNavigationView mBottomNav;

	public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.nav_weekprog:
                    startActivity(new Intent(MainActivity.this, ProgramListActivity.class));
                    return true;
                case R.id.nav_settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    return true;
            }
            return false;
        }

    };

    public static final String EXTRA_MESSAGE = "com.example.s157888.terrificthermostat35.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cTemp = (TextView) findViewById(R.id.cTemp);
        sTemp = (TextView) findViewById(R.id.sTemp);
        ImageButton plus = (ImageButton) findViewById(R.id.plus);
        ImageButton minus = (ImageButton) findViewById(R.id.minus);

		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vtemp+= 0.1;
                sTemp.setText(String.format ("%.1f", vtemp) +" \u2103");

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vtemp-=0.1;
                sTemp.setText(String.format ("%.1f", vtemp) +" \u2103");

            }
        });



    }
}
