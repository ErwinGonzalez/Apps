package com.galileo.cc6.helpmate.activities;

import android.support.design.widget.TabLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.DayPagerAdapter;
import com.galileo.cc6.helpmate.dataModels.Schedule;
import com.galileo.cc6.helpmate.database.CourseDAO;
import com.galileo.cc6.helpmate.database.ScheduleDAO;

import java.util.Calendar;
import java.util.List;


/**
 * Created by EEVGG on 14/10/2016.
 */

public class ListScheduleActivity extends AppCompatActivity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {

    public static final String TAG = "ListScheduleActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private DayPagerAdapter dayPagerAdapter;
    private int day;
    private int iDay;

    private long courseID = -1;
    private int scheduleDAY = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_schedule);
        Calendar c =  Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_WEEK);
        switch(day){
            case Calendar.SUNDAY:
                iDay=0;
                break;
            case Calendar.MONDAY:
                iDay=1;
                break;
            case Calendar.TUESDAY:
                iDay=2;
                break;
            case Calendar.WEDNESDAY:
                iDay=3;
                break;
            case Calendar.THURSDAY:
                iDay=4;
                break;
            case Calendar.FRIDAY:
                iDay=5;
                break;
            case Calendar.SATURDAY:
                iDay=6;
                break;
        }
        // initialize views
        initViews();

    }
    @Override
    public void onRestart(){
        super.onRestart();
        dayPagerAdapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }
    private void initViews() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager dayPagerAdapter
        dayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager(), 7);

        //Adding dayPagerAdapter to pager
        viewPager.setAdapter(dayPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Adding onTabSelectedListener to swipe view
        viewPager.setCurrentItem(iDay);
        tabLayout.setupWithViewPager(viewPager);




    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        //TextView mTextView = (TextView) findViewById(R.id.Test);
        Intent intent;
        switch (item.getItemId()) {
            case  R.id.action_notebook:
                intent =  new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_list_course:
                intent =  new Intent(getApplicationContext(),ListCourseActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent =  new Intent(getApplicationContext(),ConfigurationActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_schedule:
                intent =  getIntent();
                finish();
                startActivity(intent);
                return true;
            case R.id.action_assingments:
                intent =  new Intent(getApplicationContext(),AssigmentActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}

