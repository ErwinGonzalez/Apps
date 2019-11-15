package com.galileo.cc6.helpmate.activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.ListScheduleAdapter;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.dataModels.Schedule;
import com.galileo.cc6.helpmate.database.CourseDAO;
import com.galileo.cc6.helpmate.database.DBHandler;
import com.galileo.cc6.helpmate.database.ScheduleDAO;
import com.galileo.cc6.helpmate.customListeners.timeListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;

/**
 * Created by EEVGG on 11/10/2016.
 */

public class InsertCourse extends AppCompatActivity implements View.OnClickListener {

    //class manages course insertions and deletions, as well as schedules insertions and deletions
    public static final String TAG = "AddCourseActivity";
    // variables used to bind layout items
    private Button addButton;
    private Button clrButton;
    private DBHandler dbh;
    private EditText courseName;
    private EditText mondayTime;
    private EditText tuesdayTime;
    private EditText wednesdayTime;
    private EditText thursdayTime;
    private EditText fridayTime;
    private EditText saturdayTime;
    private EditText sundayTime;
    private EditText mondayEndTime;
    private EditText tuesdayEndTime;
    private EditText wednesdayEndTime;
    private EditText thursdayEndTime;
    private EditText fridayEndTime;
    private EditText saturdayEndTime;
    private EditText sundayEndTime;

    private CheckBox mondayCheck;
    private CheckBox tuesdayCheck;
    private CheckBox wednesdayCheck;
    private CheckBox thursdayCheck;
    private CheckBox fridayCheck;
    private CheckBox saturdayCheck;
    private CheckBox sundayCheck;

    private Spinner sp;
    private String[] data;

    // variables used to check for validity on inserts
    private CourseDAO courseDAO;
    private ScheduleDAO scheduleDAO;
    private String usedDay ="";
    private String conflictCourse="";
    private String conflictSTime="";
    private String conflictETime="";
    private boolean confirm=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);
        initViews();
        setTimeListeners();
        this.courseDAO = new CourseDAO(this);
        this.scheduleDAO = new ScheduleDAO(this);
    }


    private void initViews(){
        // method bind all vabiables to textViews, checkBoxes and Spinners
        this.courseName = (EditText) findViewById(R.id.CourseName);
        this.mondayTime = (EditText) findViewById(R.id.mon_text_start);
        this.tuesdayTime = (EditText) findViewById(R.id.tue_text_start);
        this.wednesdayTime = (EditText) findViewById(R.id.wed_text_start);
        this.thursdayTime = (EditText) findViewById(R.id.thu_text_start);
        this.fridayTime = (EditText) findViewById(R.id.fri_text_start);
        this.saturdayTime = (EditText) findViewById(R.id.sat_text_start);
        this.sundayTime = (EditText) findViewById(R.id.sun_text_start);

        this.mondayEndTime = (EditText) findViewById(R.id.mon_text_end);
        this.tuesdayEndTime = (EditText) findViewById(R.id.tue_text_end);
        this.wednesdayEndTime = (EditText) findViewById(R.id.wed_text_end);
        this.thursdayEndTime = (EditText) findViewById(R.id.thu_text_end);
        this.fridayEndTime = (EditText) findViewById(R.id.fri_text_end);
        this.saturdayEndTime = (EditText) findViewById(R.id.sat_text_end);
        this.sundayEndTime = (EditText) findViewById(R.id.sun_text_end);

        this.mondayCheck = (CheckBox) findViewById(R.id.mon_checkbox);
        this.tuesdayCheck = (CheckBox) findViewById(R.id.tue_checkbox);
        this.wednesdayCheck = (CheckBox) findViewById(R.id.wed_checkbox);
        this.thursdayCheck = (CheckBox) findViewById(R.id.thu_checkbox);
        this.fridayCheck = (CheckBox) findViewById(R.id.fri_checkbox);
        this.saturdayCheck = (CheckBox) findViewById(R.id.sat_checkbox);
        this.sundayCheck = (CheckBox) findViewById(R.id.sun_checkbox);

        this.sp=(Spinner)findViewById(R.id.color_spinner);
        this.addButton = (Button)findViewById(R.id.insert_course_button);
        this.addButton.setOnClickListener(this);
        this.clrButton = (Button)findViewById(R.id.clear_button);
        this.clrButton.setOnClickListener(this);
        data = new String[]{"Rojo","Azul","Amarillo","Verde","Anaranjado","Negro"};
        ArrayAdapter<String> listAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data );
        sp.setAdapter(listAd);
    }


    public void setTimeListeners(){
        // setTimeListeners manages calls to timeListener class, which shows a timepicker
        mondayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,mondayTime);}});
        tuesdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,tuesdayTime);    }
        });
        wednesdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,wednesdayTime);    }
        });
        thursdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,thursdayTime);    }
        });
        fridayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,fridayTime);}
        });
        saturdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,saturdayTime);}
        });
        sundayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,sundayTime);}
        });
        mondayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,mondayEndTime);}
        });
        tuesdayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,tuesdayEndTime);}
        });
        wednesdayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,wednesdayEndTime);}
        });
        thursdayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,thursdayEndTime);}
        });
        fridayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,fridayEndTime);}
        });
        saturdayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,saturdayEndTime);}
        });
        sundayEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListener tl = new timeListener();
                tl.setListener(InsertCourse.this,sundayEndTime);}
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
        // Handle item selection
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
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.insert_course_button:
                //On insert all selected fields must be valiated, so each one is bind to a text variable
                ArrayList<Editable> editList = new ArrayList<Editable>();
                Editable coName = courseName.getText();
                Editable monS = mondayTime.getText();
                Editable monE = mondayEndTime.getText();
                editList.add(monS);
                editList.add(monE);
                Editable tueS= tuesdayTime.getText();
                Editable tueE= tuesdayEndTime.getText();
                editList.add(tueS);
                editList.add(tueE);
                Editable wedS= wednesdayTime.getText();
                Editable wedE= wednesdayEndTime.getText();
                editList.add(wedS);
                editList.add(wedE);
                Editable thuS= thursdayTime.getText();
                Editable thuE= thursdayEndTime.getText();
                editList.add(thuS);
                editList.add(thuE);
                Editable friS= fridayTime.getText();
                Editable friE= fridayEndTime.getText();
                editList.add(friS);
                editList.add(friE);
                Editable satS= saturdayTime.getText();
                Editable satE= saturdayEndTime.getText();
                editList.add(satS);
                editList.add(satE);
                Editable sunS= sundayTime.getText();
                Editable sunE= sundayEndTime.getText();
                editList.add(sunS);
                editList.add(sunE);
                String coColor = sp.getSelectedItem().toString();
                if (!TextUtils.isEmpty(coName)) {
                    /*name is a must, otherwise the app wont continue,
                    *then the time fields are checked, each must have a start and end time
                    * thet are coherent, start time must be before end time
                    * also a day must be selected to insert a time window
                    */
                    if(checkCorrectFields(editList)&&isOneSelected()){
                        if(scheduleTimesOK(editList)) {
                            scheduleTimeAlreadyUsed(editList);
                            // scheduleTimeAlreadyUsed(ArrayList<Editable>) checks database for tiem windows conflicts
                            //insert is not imposible but user is warned of time conflict
                            if(confirm) {
                                insertNewCourse(coName, coColor, editList);
                            }else{
                                InsertConflictDialog(coName,coColor,editList).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, R.string.emptyScheduleFields, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, R.string.emptyName, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.clear_button:
                // on clear resets all fields to empty and default values, also unchecks any checked checkbox
                    courseName.setText("");
                    mondayTime.setText("");
                    tuesdayTime.setText("");
                    wednesdayTime.setText("");
                    thursdayTime.setText("");
                    fridayTime.setText("");
                    saturdayTime.setText("");
                    sundayTime.setText("");
                    mondayEndTime.setText("");
                    tuesdayEndTime.setText("");
                    wednesdayEndTime.setText("");
                    thursdayEndTime.setText("");
                    fridayEndTime.setText("");
                    saturdayEndTime.setText("");
                    sundayEndTime.setText("");
                    sp.setSelection(0);
                    if(mondayCheck.isChecked()){mondayCheck.toggle();}
                    if(tuesdayCheck.isChecked()){tuesdayCheck.toggle();}
                    if(wednesdayCheck.isChecked()){wednesdayCheck.toggle();}
                    if(thursdayCheck.isChecked()){thursdayCheck.toggle();}
                    if(fridayCheck.isChecked()){fridayCheck.toggle();}
                    if(saturdayCheck.isChecked()){saturdayCheck.toggle();}
                    if(sundayCheck.isChecked()){sundayCheck.toggle();}
                break;
            default:
                break;
        }
    }
    public boolean isOneSelected(){
        // isOneSelected() validates that course has at least one day associated
        return(mondayCheck.isChecked()||tuesdayCheck.isChecked()||wednesdayCheck.isChecked()||thursdayCheck.isChecked()
        ||fridayCheck.isChecked()||saturdayCheck.isChecked()||sundayCheck.isChecked());
    }

    public boolean checkCorrectFields(ArrayList<Editable> el){
        //checkCorrectFields(ArrayList<Editable>) checks that requiered fields to insert a schedule are filled
        if((mondayCheck.isChecked()&&(TextUtils.isEmpty(el.get(0))||TextUtils.isEmpty(el.get(1)))
          ||(!mondayCheck.isChecked()&&(!TextUtils.isEmpty(el.get(0))||!TextUtils.isEmpty(el.get(1))))))
            {Toast.makeText(this,"Error en el dia lunes", Toast.LENGTH_LONG).show();
            return false;}
        if((tuesdayCheck.isChecked()&&(TextUtils.isEmpty(el.get(2))||TextUtils.isEmpty(el.get(3))))
          ||(!tuesdayCheck.isChecked()&&(!TextUtils.isEmpty(el.get(2))||!TextUtils.isEmpty(el.get(3))))){
            Toast.makeText(this,"Error en el dia martes", Toast.LENGTH_LONG).show();
            return false;
        }
        if((wednesdayCheck.isChecked()&&(TextUtils.isEmpty(el.get(4))||TextUtils.isEmpty(el.get(5))))
          ||(!wednesdayCheck.isChecked()&&(!TextUtils.isEmpty(el.get(4))||!TextUtils.isEmpty(el.get(5))))){
            Toast.makeText(this,"Error en el dia miercoles", Toast.LENGTH_LONG).show();
            return false;
        }
        if((thursdayCheck.isChecked()&&(TextUtils.isEmpty(el.get(6))||TextUtils.isEmpty(el.get(7))))
          ||(!thursdayCheck.isChecked()&&(!TextUtils.isEmpty(el.get(6))||!TextUtils.isEmpty(el.get(7))))){
            Toast.makeText(this,"Error en el dia jueves", Toast.LENGTH_LONG).show();
            return false;
        }
        if((fridayCheck.isChecked()&&(TextUtils.isEmpty(el.get(8))||TextUtils.isEmpty(el.get(9))))
          ||(!fridayCheck.isChecked()&&(!TextUtils.isEmpty(el.get(8))||!TextUtils.isEmpty(el.get(9))))){
            Toast.makeText(this,"Error en el dia viernes", Toast.LENGTH_LONG).show();
            return false;
        }
        if((saturdayCheck.isChecked()&&(TextUtils.isEmpty(el.get(10))||TextUtils.isEmpty(el.get(11))))
          ||(!saturdayCheck.isChecked()&&(!TextUtils.isEmpty(el.get(10))||!TextUtils.isEmpty(el.get(11))))){
            Toast.makeText(this,"Error en el dia sabado", Toast.LENGTH_LONG).show();
            return false;
        }
        if((sundayCheck.isChecked()&&(TextUtils.isEmpty(el.get(12))||TextUtils.isEmpty(el.get(13))))
          ||(!sundayCheck.isChecked()&&(!TextUtils.isEmpty(el.get(12))||!TextUtils.isEmpty(el.get(13))))){
            Toast.makeText(this,"Error en el dia domingo", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public boolean compareTime(Editable t1, Editable t2){
        //compareTime(Editable t1, Editable t2) checks for a time inconsistency on start and end times
        if(Integer.parseInt(t2.toString().substring(0,2))<Integer.parseInt(t1.toString().substring(0,2))){
            return false;
        }else{
            if(Integer.parseInt(t2.toString().substring(3,5))<=Integer.parseInt(t1.toString().substring(3,5))
                    &&(Integer.parseInt(t2.toString().substring(0,2))==Integer.parseInt(t1.toString().substring(0,2)))){
                return false;
            }
        }
        return true;
    }

    public boolean scheduleTimesOK(ArrayList<Editable> el) {
        //scheduleTimesOK(ArrayList<Editable> el) recieves all time fields and checkboxes and checks for inconsistencies
        if(mondayCheck.isChecked()&&!compareTime(el.get(0),el.get(1))){
            Toast.makeText(this,"Horario de final del lunes es antes o igual al del inicio", Toast.LENGTH_LONG).show();
            return false;
        }
        if(tuesdayCheck.isChecked()&&!compareTime(el.get(2),el.get(3))){
            Toast.makeText(this,"Horario de final del martes es antes o igual al del inicio", Toast.LENGTH_LONG).show();
            return false;
        }
        if(wednesdayCheck.isChecked()&&!compareTime(el.get(4),el.get(5))){
            Toast.makeText(this,"Horario de final del miercoles es antes o igual al del inicio", Toast.LENGTH_LONG).show();
            return false;
        }
        if(thursdayCheck.isChecked()&&!compareTime(el.get(6),el.get(7))){
            Toast.makeText(this,"Horario de final del jueves es antes o igual al del inicio", Toast.LENGTH_LONG).show();
            return false;
        }
        if(fridayCheck.isChecked()&&!compareTime(el.get(8),el.get(9))){
            Toast.makeText(this,"Horario de final del viernes es antes o igual al del inicio", Toast.LENGTH_LONG).show();
            return false;
        }
        if(saturdayCheck.isChecked()&&!compareTime(el.get(10),el.get(11))){
            Toast.makeText(this,"Horario de final del sabado es antes o igual al del inicio", Toast.LENGTH_LONG).show();
            return false;
        }
        if(sundayCheck.isChecked()&&!compareTime(el.get(12),el.get(13))){
            Toast.makeText(this,"Horario de final del domingo es antes o igual al del inicio", Toast.LENGTH_LONG).show();
            return false;
        }
    return true;
    }
    private void insertNewCourse(Editable coName, String coColor, ArrayList<Editable> el){
        // method is in charge of inserting a course and it's respective schedules after all checks are passed
        Course newCourse = courseDAO.createCourse(coName.toString(), coColor);
        Schedule newSchedule;
        if (mondayCheck.isChecked()) {
            newSchedule = scheduleDAO.createSchedule(1, el.get(0).toString(), el.get(1).toString(), newCourse.getId());
        }
        if (tuesdayCheck.isChecked()) {
            newSchedule = scheduleDAO.createSchedule(2, el.get(2).toString(), el.get(3).toString(), newCourse.getId());
        }
        if (wednesdayCheck.isChecked()) {
            newSchedule = scheduleDAO.createSchedule(3, el.get(4).toString(), el.get(5).toString(), newCourse.getId());
        }
        if (thursdayCheck.isChecked()) {
            newSchedule = scheduleDAO.createSchedule(4, el.get(6).toString(), el.get(7).toString(), newCourse.getId());
        }
        if (fridayCheck.isChecked()) {
            newSchedule = scheduleDAO.createSchedule(5, el.get(8).toString(), el.get(9).toString(), newCourse.getId());
        }
        if (saturdayCheck.isChecked()) {
            newSchedule = scheduleDAO.createSchedule(6, el.get(10).toString(), el.get(11).toString(), newCourse.getId());
        }
        if (sundayCheck.isChecked()) {
            newSchedule = scheduleDAO.createSchedule(0, el.get(12).toString(), el.get(13).toString(), newCourse.getId());
        }
        Log.d(TAG, "added course : " + newCourse.getName());
        Intent intent;
        intent = new Intent(this, ListCourseActivity.class);
        ListScheduleAdapter ls = new ListScheduleAdapter(this,scheduleDAO.getAllSchedules());
        ls.notifyDataSetChanged();
        courseDAO.close();
        scheduleDAO.close();
        finish();
        startActivity(intent);
    }
    public boolean scheduleTimeAlreadyUsed(ArrayList<Editable> el){
        // method checks for time overlaps in database
        List<Schedule> sc = scheduleDAO.getAllSchedules();
        if(sc!=null && !sc.isEmpty()){
            for (Schedule e :sc){

                if(mondayCheck.isChecked()&&(e.getDay()==1)){
                    confirmTime(el.get(0),el.get(1),e);

                }
                if(tuesdayCheck.isChecked()&&(e.getDay()==2)){
                    confirmTime(el.get(2),el.get(3),e);
                }
                if(wednesdayCheck.isChecked()&&(e.getDay()==3)){
                    confirmTime(el.get(4),el.get(5),e);
                }
                if(thursdayCheck.isChecked()&&(e.getDay()==4)){
                    confirmTime(el.get(6),el.get(7),e);
                }
                if(fridayCheck.isChecked()&&(e.getDay()==5)){
                    confirmTime(el.get(8),el.get(9),e);
                }
                if(saturdayCheck.isChecked()&&(e.getDay()==6)){
                    confirmTime(el.get(10),el.get(11),e);
                }
                if(sundayCheck.isChecked()&&(e.getDay()==0)){
                    confirmTime(el.get(12),el.get(13),e);
                }
            }
        }
        return confirm;
    }
    private void confirmTime(Editable currSTime, Editable currETime, Schedule sch){
        // method is used to format dialog output
        int sHour = sch.getStartHour();
        int sMin = sch.getStartMinute();
        int eHour = sch.getEndHour();
        int eMin = sch.getEndMinute();
        switch (sch.getDay()){
            case 0:
                usedDay="Domingo";
                break;
            case 1:
                usedDay="Lunes";
                break;
            case 2:
                usedDay="Martes";
                break;
            case 3:
                usedDay="Miercoles";
                break;
            case 4:
                usedDay="Jueves";
                break;
            case 5:
                usedDay="Viernes";
                break;
            case 6:
                usedDay="Sabado";
                break;
        }
        if((Integer.parseInt(currSTime.toString().substring(0,2))>=sHour)&&(Integer.parseInt(currSTime.toString().substring(0,2))<=eHour)){
            if((Integer.parseInt(currSTime.toString().substring(3,5))<=eMin)||(eMin==0)){
                conflictCourse=sch.getCourse().getName();
                conflictSTime=sch.getStartTime();
                conflictETime=sch.getEndTime();
                confirm=false;
            }
        }
        if((Integer.parseInt(currETime.toString().substring(0,2))>=sHour)&&(Integer.parseInt(currETime.toString().substring(0,2))<=eHour)){
            if((Integer.parseInt(currETime.toString().substring(3,5))>=sMin)){
                conflictCourse=sch.getCourse().getName();
                conflictSTime=sch.getStartTime();
                conflictETime=sch.getEndTime();
                confirm=false;
            }
        }
    }
    private AlertDialog InsertConflictDialog(final Editable coName, final String coColor, final ArrayList<Editable> editList) {
        return new AlertDialog.Builder(this)
            //set message, title, and icon
            .setTitle("Confirmar Ingreso")
            .setMessage("Conflicto en el dia "+usedDay+" ya existe horario para el " +
                    "curso : "+conflictCourse+" que tiene horario "+conflictSTime+"->"+conflictETime)
            .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //your deleting code
                    insertNewCourse(coName,coColor,editList);
                    confirm=true;
                    dialog.dismiss();
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    confirm=true;
                    dialog.dismiss();
                }
            })
            .create();
    }
}
