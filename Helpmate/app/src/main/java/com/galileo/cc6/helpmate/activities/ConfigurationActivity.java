package com.galileo.cc6.helpmate.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.ListCourseAdapter;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.database.CourseDAO;
import com.galileo.cc6.helpmate.database.HojasDAO;

import java.text.ParseException;
import java.util.List;

/**
 * Created by EEVGG on 21/10/2016.
 */

public class ConfigurationActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "ConfigActivity";
    //this activity is used to show copiright information and also delete all data
    Button aboutButton, delButton;
    CourseDAO courseDAO;
    HojasDAO hojasDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);

        initViews();
    }
    private void initViews() {
        this.aboutButton = (Button) findViewById(R.id.about_button);
        this.aboutButton.setOnClickListener(this);
        this.delButton = (Button) findViewById(R.id.delete_button);
        this.delButton.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_button:
                showAboutDialog().show();
                break;
            case R.id.delete_button:
                showDeleteConfirmDialog();
                break;
            default:
                break;
        }
    }
    private AlertDialog showAboutDialog() {
        final TextView message  = new TextView(ConfigurationActivity.this);
        message.setPadding(40,0,40,0);
        final SpannableString str = new SpannableString("Aplicacion Helpmate, creada 2016 para el curso de Ingenieria de Sistemas\n" +
				"por Santiago Wever y Erwin Gonzalez\n"+
                "Utilizacion de Librerias Adicionales\niTextPdf v.5.5.8\n" +
                "uso de Drawable Notepad bajo la licencia GPLv3+\n para mas informcacion visite:\n"
                +"https://github.com/tmarzeion/drawable-notepad/blob/HEAD/README.md");
        Linkify.addLinks(str, Linkify.ALL);
        message.setText(str);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        return new AlertDialog.Builder(ConfigurationActivity.this)
                .setTitle("Acerca De")
                .setCancelable(true)
                .setPositiveButton(R.string.ok, null)
                .setView(message)
                .create();

    }
    private void showDeleteConfirmDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("BORRAR");
        alertDialogBuilder.setMessage("Esta seguro que desea borrar todos los datos?");
        courseDAO = new CourseDAO(ConfigurationActivity.this);
        hojasDAO = new HojasDAO(ConfigurationActivity.this);
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(courseDAO != null) {
                    List<Course> courseList = courseDAO.getAllCourses();
                    for(Course c :courseList){
                        try {
                            courseDAO.deleteCourse(c);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                hojasDAO.clearAllNotes();
                dialog.dismiss();
                courseDAO.close();
                hojasDAO.close();
                Toast.makeText(ConfigurationActivity.this,"Todos los datos han sido borrados", Toast.LENGTH_SHORT).show();
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
