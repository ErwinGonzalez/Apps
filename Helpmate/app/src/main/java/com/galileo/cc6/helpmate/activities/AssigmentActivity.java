package com.galileo.cc6.helpmate.activities;

/**
 * Created by EEVGG on 14/10/2016.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.ListAssignmentAdapter;
import com.galileo.cc6.helpmate.dataModels.Assignment;
import com.galileo.cc6.helpmate.database.AssignmentDAO;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AssigmentActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String TAG = "AssingmentActivity";
    /* Defines variables used in the activity execution
     */
    ImageButton imageButton;
    AssignmentDAO aDAO ;
    List<Assignment> assignmentList;
    ListAssignmentAdapter listAAD;
    Button showNotificationB, stopNotificationB;
    Intent alertIntent;
    PendingIntent aIntent;
    AlarmManager alarmManager;
    public static final String alarmActivated = "alarm";
    SharedPreferences sharedPreferences;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assingments_layout);
        //binds variables to layout items
        this.imageButton = (ImageButton) findViewById(R.id.as_imageButton);
        this.imageButton.setOnClickListener(this);
        this.showNotificationB= (Button) findViewById(R.id.showNotButton);
        this.stopNotificationB= (Button) findViewById(R.id.showCanButton);
        this.showNotificationB.setOnClickListener(this);
        this.stopNotificationB.setOnClickListener(this);
        this.alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        this.lv=(ListView) findViewById(R.id.assignment_list_view);
        this.lv.setOnItemClickListener(this);
        this.lv.setOnItemLongClickListener(this);

        //gets assignments to populate list
        this.aDAO=new AssignmentDAO(this);
        //checks alarm status, if activated it'll show deactivate button
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean(alarmActivated,false)){
            this.showNotificationB.setVisibility(View.GONE);
            this.stopNotificationB.setVisibility(View.VISIBLE);
        }else{
            this.showNotificationB.setVisibility(View.VISIBLE);
            this.stopNotificationB.setVisibility(View.GONE);
        }

        try {
            //sorts assingment list by date, then by priority before populating
            this.assignmentList=aDAO.getAllAssignments();
            Collections.sort(assignmentList,Assignment.assignmentComparator);
            if(assignmentList!= null && !assignmentList.isEmpty()){
                listAAD=new ListAssignmentAdapter(this,assignmentList);
                lv.setAdapter(listAAD);
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
                intent =  getIntent();
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.as_imageButton:
                Intent intent = new Intent(this, InsertAssignment.class);
                startActivity(intent);
                break;
            case R.id.showNotButton:
                Calendar mCalendar = Calendar.getInstance();
                stopNotificationB.setVisibility(View.VISIBLE);
                showNotificationB.setVisibility(View.GONE);

                mCalendar.set(Calendar.HOUR_OF_DAY,7);
                mCalendar.set(Calendar.MINUTE,0);
                mCalendar.set(Calendar.SECOND,0);
                mCalendar.set(Calendar.MILLISECOND,0);
                long alertTime = mCalendar.getTimeInMillis();
                // Define our intention of executing AlertReceiver

                alertIntent = new Intent(AssigmentActivity.this, AlertReceiver.class);
                aIntent = PendingIntent.getBroadcast(AssigmentActivity.this,0,alertIntent,0);
                /* Allows you to schedule for your application to do something at a later date
                 even if it is in he background or isn't active
                 setInexactRepeating allows device to bundle alarms in order to save battery,
                 this alarm triggers every day
                 FLAG_UPDATE_CURRENT : Update the Intent if active , also updates saved preferences used to
                 show correct display button*/

                alarmManager.setInexactRepeating(AlarmManager.RTC, alertTime,AlarmManager.INTERVAL_DAY,aIntent);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferences.edit().putBoolean(alarmActivated,true).apply();

                break;
            case R.id.showCanButton:
                /* Cancels alarm intent so it stops triggering, also updates saved preferences used to
                   show correct display button
                 */
                alertIntent = new Intent(AssigmentActivity.this, AlertReceiver.class);
                aIntent = PendingIntent.getBroadcast(AssigmentActivity.this,0,alertIntent,0);
                alarmManager.cancel(aIntent);
                stopNotificationB.setVisibility(View.GONE);
                showNotificationB.setVisibility(View.VISIBLE);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferences.edit().putBoolean(alarmActivated,false).apply();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Assignment clickedAssignment = listAAD.getItem(position);
        showUpdateDialogConfirmation(clickedAssignment);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Assignment clickedAssignment = listAAD.getItem(position);

        showDeleteDialogConfirmation(clickedAssignment);
        return true;
    }
    private void showUpdateDialogConfirmation(final Assignment clickedAssignment){
        //dialog used to confirm selection, it'll update repeating tasks and delete single time ones
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Tarea Completa");
        alertDialogBuilder.setMessage("Actualizar estado de : \""+clickedAssignment.getTitle()+"\"?");
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(aDAO != null) {
                    aDAO.updateOrDeleteAssignment(clickedAssignment);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }

                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void showDeleteDialogConfirmation(final Assignment clickedAssignment) {
        //dialog used to remove tasks regardless of frecuency
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Eliminar Tarea");
        alertDialogBuilder.setMessage("Quitar de la lista : \""+clickedAssignment.getTitle()+"\"?");
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(aDAO != null) {
                    aDAO.deleteAssignment(clickedAssignment);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
