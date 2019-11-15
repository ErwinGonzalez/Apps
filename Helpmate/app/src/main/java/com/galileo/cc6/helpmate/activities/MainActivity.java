package com.galileo.cc6.helpmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.CourseGridAdapter;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.database.CourseDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    // gridVIew used to display the notebooks on a grid
    GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* gridView is assigned the respective item in the layout
        then the courseList is called to retrieve the full course list
        that list is passed to a CourseGridAdapter which formats the
        output to diaplay.
         */
        gridView = (GridView) findViewById(R.id.gridView1);
        CourseDAO cDAO = new CourseDAO(this);
        final List<Course> cList = cDAO.getAllCourses();
        gridView.setAdapter(new CourseGridAdapter(this,cList));
        // a on click listener is created to be able to select a course
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(),HojasCuaderno.class);
                Bundle data = new Bundle();
                //course id must be passed on until a note is reached to propoertly save
                data.putLong("Curso",cList.get(position).getId());
                intent.putExtras(data);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        Intent intent;
        switch (item.getItemId()) {
            case  R.id.action_notebook:
                intent =  getIntent();
                finish();
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
                intent =  new Intent(getApplicationContext(),ListScheduleActivity.class);
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
}
