package com.galileo.cc6.helpmate.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galileo.cc6.helpmate.adapters.ListCourseAdapter;
import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.database.CourseDAO;
import com.galileo.cc6.helpmate.database.HojasDAO;

import java.text.ParseException;
import java.util.List;

public class ListCourseActivity extends AppCompatActivity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {

    public static  final String TAG = "ListCourseActivity";

    private ListView lv;
    private TextView txtV;
    private ImageButton imageButton;

    private ListCourseAdapter listCAD;
    private List<Course> courseList;
    private CourseDAO courseDAO;
    private HojasDAO hojasDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_course);

        // initialize views
        initViews();
        courseDAO = new CourseDAO(this);
        hojasDAO = new HojasDAO(this);
        courseList = courseDAO.getAllCourses();
        if(courseList != null && !courseList.isEmpty()) {
            listCAD = new ListCourseAdapter(this, courseList);
            lv.setAdapter(listCAD);
        }
    }

    @Override
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
                intent =  getIntent();
                finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                Intent intent = new Intent(this, InsertCourse.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Course clickedCourse = listCAD.getItem(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Course clickedCourse = listCAD.getItem(position);
        Log.d(TAG, "longClickedItem : "+clickedCourse.getName());
        showDeleteDialogConfirmation(clickedCourse);
        return true;
    }
    private void showDeleteDialogConfirmation(final Course clickedCourse) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("BORRAR");
        alertDialogBuilder.setMessage("Esta seguro que desea eliminar el curso: \""+clickedCourse.getName()+"\" " +
                "?\n se eliminaran tambien las tareas, horarios y notas asociadas. ");

        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete the company and refresh the list
                if(courseDAO != null) {
                    try {
                        courseDAO.deleteCourse(clickedCourse);
                        hojasDAO.getAllNotesByCourse(clickedCourse.getId());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    courseList.remove(clickedCourse);
                    listCAD.setItems(courseList);
                    listCAD.notifyDataSetChanged();
                }

                dialog.dismiss();
                Toast.makeText(ListCourseActivity.this,"Curso Eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        // set neutral button OK
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }
    private void initViews() {
        this.lv = (ListView) findViewById(R.id.course_list_view);
        this.imageButton = (ImageButton) findViewById(R.id.imageButton);
        this.imageButton.setOnClickListener(this);
        this.lv.setOnItemClickListener(this);
        this.lv.setOnItemLongClickListener(this);
    }
}
