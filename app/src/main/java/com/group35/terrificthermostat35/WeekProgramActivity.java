package com.group35.terrificthermostat35;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.projectapi.thermometerapi.ThermostatData;
import com.projectapi.thermometerapi.WeekDay;
import com.projectapi.thermometerapi.WeekProgram;

public class WeekProgramActivity extends BasicActivity {

    public static final String WEEKPROGRAM_NAME_MESSAGE = "com.group35.terrificthermostat35.weekProgramNameMessage";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    private WeekProgram weekProgram;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_program);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get weekProgram name from intent and set it as title
        Intent intent = getIntent();
        String name = intent.getStringExtra(WEEKPROGRAM_NAME_MESSAGE);
        getSupportActionBar().setTitle(name);
        weekProgram = app.getFileManager().get(name);
        if (weekProgram == null) {
            weekProgram = app.getThermostatData().getCopyOfWeekProgram();
            weekProgram.name = WeekProgram.DEFAULT_NAME;
        } else {
            lastName = weekProgram.getName();
        }

        // Create the adapter that will return a fragment for each of the seven
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_week_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {

            app.getFileManager().save(weekProgram);
            Log.i("Saving", "Saving " + weekProgram.name);

            if (lastName != null && !lastName.equals(weekProgram.getName()))
                app.getFileManager().delete(lastName);

            startActivity(new Intent(WeekProgramActivity.this, ProgramListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String actionBarTitle) {
        getSupportActionBar().setTitle(actionBarTitle);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return WeekProgramMainFragment.newInstance(getIntent().getStringExtra(WEEKPROGRAM_NAME_MESSAGE), weekProgram);
            }
            return WeekDayFragment.newInstance(position, getPageTitle(position).toString(), weekProgram);
        }

        @Override
        public int getCount() {
            // Show 8 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Main";
                case 1:
                    return "Monday";
                case 2:
                    return "Tuesday";
                case 3:
                    return "Wednesday";
                case 4:
                    return "Thursday";
                case 5:
                    return "Friday";
                case 6:
                    return "Saturday";
                case 7:
                    return "Sunday";
            }
            return null;
        }
    }

    @Override
    public void onThermostatDataUpdate(ThermostatData thermostatData) {

    }
}
