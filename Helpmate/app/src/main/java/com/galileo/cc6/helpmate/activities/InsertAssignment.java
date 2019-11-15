package com.galileo.cc6.helpmate.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.ListAssignmentAdapter;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.database.AssignmentDAO;
import com.galileo.cc6.helpmate.database.CourseDAO;
import com.galileo.cc6.helpmate.database.DBHandler;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by EEVGG on 28/10/2016.
 */

public class InsertAssignment extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "AddAssingmentActivity";
    // creates variables that will be bound to layout items
    private EditText title;
    private Spinner course_spinner;
    private EditText description;
    private EditText due_date;
    // date  format to manage inserts on the database
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "dd-MM-yyyy", Locale.ENGLISH);

    private Spinner repeat_settings;
    private Spinner prioritySpinner;
    private Button addButton;
    private Button clrButton;
    private Calendar dateCalendar;
    private CourseDAO cDAO;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assingment);
        cDAO = new CourseDAO(this);
        List<Course> check = cDAO.getAllCourses();
        this.addButton = (Button) findViewById(R.id.insert_assingment_button);
        this.clrButton = (Button) findViewById(R.id.clr_assignment_button);
        // as assingments are bound to courses, checks if any exist, if not it redirects to insertCourse activity
        if(check.isEmpty()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Para Ingresar una Tarea, Ingrese un curso primero");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent= new Intent(InsertAssignment.this, InsertCourse.class);
                            finish();
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        initViews();
    }

    private void initViews(){
        // method initViews() binds variables to layout items
        this.addButton.setOnClickListener(this);
        this.clrButton.setOnClickListener(this);
        this.title = (EditText) findViewById(R.id.Title_txt);
        this.course_spinner = (Spinner) findViewById(R.id.course_spinner);
        courses= cDAO.getAllCourses();
        ArrayList<String> cList = new ArrayList<String>();
        for(Course e:courses){
            cList.add(e.getName());
        }
        // gets course list to fill spinner
        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cList);
        course_spinner.setAdapter(cAdapter);
        this.description =(EditText) findViewById(R.id.Description_Edit);
        this.due_date = (EditText) findViewById(R.id.Date_edit);
        this.repeat_settings = (Spinner) findViewById(R.id.repeat_spinner);
        // creates adapters to fill spinner repeat and priority valules
        ArrayAdapter<String> repAd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                new String[]{"Una Vez","Diario","Semanal","Mensual","Anual"});
        this.repeat_settings.setAdapter(repAd);
        this.prioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
        ArrayAdapter<String> priAd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                new String[]{"1","2","3"});
        this.prioritySpinner.setAdapter(priAd);
        setListeners();
    }

    private void setListeners(){
        //uses calendar object to start a datepicking dialog, instanciates calendar to start on today's date
        dateCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateCalendar.set(Calendar.YEAR, year);
                dateCalendar.set(Calendar.MONTH, monthOfYear);
                dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        due_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(InsertAssignment.this, datePicker, dateCalendar
                        .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // clear resets fields to default values
            case R.id.clr_assignment_button:
                title.setText("");
                description.setText("");
                course_spinner.setSelection(0);
                due_date.setText("");
                repeat_settings.setSelection(0);
                prioritySpinner.setSelection(0);
                break;

            case R.id.insert_assingment_button:
                Editable titleTxt = title.getText();
                Editable descTxt = description.getText();
                String descStr="";
                Editable newDate = due_date.getText();
                // gets fields values and checks if they have correct values
                if(!TextUtils.isEmpty(descTxt)){
                    descStr=descTxt.toString();
                }
                if(!TextUtils.isEmpty(titleTxt) &&  !TextUtils.isEmpty(newDate)) {
                    Integer newDay = Integer.parseInt(newDate.toString().substring(0, 2));
                    Integer newMonth = Integer.parseInt(newDate.toString().substring(3, 5));
                    Integer newYear = Integer.parseInt(newDate.toString().substring(6, 10));
                    Calendar c = Calendar.getInstance();
                    Calendar insertDate = Calendar.getInstance();
                    insertDate.set(Calendar.YEAR, newYear);
                    insertDate.set(Calendar.MONTH, newMonth-1);
                    insertDate.set(Calendar.DATE, newDay);
                    insertDate.set(Calendar.HOUR_OF_DAY, 0);
                    insertDate.set(Calendar.MINUTE, 0);
                    insertDate.set(Calendar.SECOND, 0);
                    insertDate.set(Calendar.MILLISECOND, 0);


                    AssignmentDAO aDAO = new AssignmentDAO(InsertAssignment.this);
                    // the following if strings validates that the date is not a past date, otherwise an notification would never pop up
                    if (newYear < c.get(Calendar.YEAR)) {
                        Toast.makeText(this, "Invalid YEAR", Toast.LENGTH_LONG).show();
                    }else {
                        if(newYear > c.get(Calendar.YEAR)){
                            try {
                                aDAO.createAssignment(titleTxt.toString(),descStr,courses.get(course_spinner.getSelectedItemPosition()).getId(),
                                        insertDate.getTime(),Integer.parseInt(prioritySpinner.getSelectedItem().toString()),repeat_settings.getSelectedItem().toString());
                                Intent intent = new Intent(this,AssigmentActivity.class);
                                aDAO.close();
                                cDAO.close();
                                finish();
                                startActivity(intent);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else{
                            if(newMonth < (c.get(Calendar.MONTH)+1)){
                                Toast.makeText(this, "Invalid MONTH", Toast.LENGTH_LONG).show();
                            }else{
                                if(newMonth>(c.get(Calendar.MONTH)+1)){
                                    try {
                                        aDAO.createAssignment(titleTxt.toString(),descStr,courses.get(course_spinner.getSelectedItemPosition()).getId(),
                                                insertDate.getTime(),Integer.parseInt(prioritySpinner.getSelectedItem().toString()),repeat_settings.getSelectedItem().toString());
                                        Intent intent = new Intent(this,AssigmentActivity.class);
                                        aDAO.close();
                                        cDAO.close();
                                        finish();
                                        startActivity(intent);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    if(newDay<c.get(Calendar.DAY_OF_MONTH)){
                                        Toast.makeText(this, "Invalid DAY", Toast.LENGTH_LONG).show();
                                    }else{
                                        if(newDay>=c.get(Calendar.DAY_OF_MONTH)){
                                            try {
                                                aDAO.createAssignment(titleTxt.toString(),descStr,courses.get(course_spinner.getSelectedItemPosition()).getId(),
                                                        insertDate.getTime(),Integer.parseInt(prioritySpinner.getSelectedItem().toString()),repeat_settings.getSelectedItem().toString());
                                                Intent intent = new Intent(this,AssigmentActivity.class);
                                                aDAO.close();
                                                cDAO.close();
                                                finish();
                                                startActivity(intent);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    // title and date values are a must, description is optional
                    Toast.makeText(this, R.string.emptyAssignmentFields, Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        due_date.setText(sdf.format(dateCalendar.getTime()));
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
        // TextView mTextView = (TextView) findViewById(R.id.Test);
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
