package com.galileo.cc6.helpmate.database;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import com.galileo.cc6.helpmate.dataModels.Assignment;
import com.galileo.cc6.helpmate.dataModels.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by EEVGG on 03/11/2016.
 */

public class AssignmentDAO {
    public static final String TAG = "AssignmentDAO";

    private SQLiteDatabase db;
    private DBHandler dbh;
    private Context ctx;
    private String[] asAllColumns = {
            DBHandler.ASSIGNMENT_ID,
            DBHandler.ASSIGNMENT_TITLE,
            DBHandler.ASSIGNMENT_DESCRIPTION,
            DBHandler.ASSIGNMENT_COURSE_ID,
            DBHandler.ASSIGNMENT_DATE,
            DBHandler.ASSIGNMENT_PRIORITY,
            DBHandler.ASSIGNMENT_REPEAT
    };
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "dd/MM/yyyy", Locale.ENGLISH);
    public AssignmentDAO(Context ctx){
        this.ctx = ctx;
        dbh = new DBHandler(ctx);
        try{
            open();
        }catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void open() throws SQLException{
        db= dbh.getWritableDatabase();
    }
    public void close() {
        dbh.close();
    }
    public Assignment createAssignment(String title, String desc, long courseId, Date date,int priority, String repeat) throws ParseException {
        ContentValues cv = new ContentValues();
        cv.put(DBHandler.ASSIGNMENT_TITLE,title);
        cv.put(DBHandler.ASSIGNMENT_DESCRIPTION,desc);
        String newDate = formatter.format(date);
        cv.put(DBHandler.ASSIGNMENT_DATE,newDate);
        cv.put(DBHandler.ASSIGNMENT_PRIORITY,priority);
        cv.put(DBHandler.ASSIGNMENT_REPEAT,repeat);
        cv.put(DBHandler.ASSIGNMENT_COURSE_ID,courseId);
        long insertID = db.insert(DBHandler.ASSIGNMENT_TABLE,null,cv);
        Cursor c = db.query (DBHandler.ASSIGNMENT_TABLE, asAllColumns,DBHandler.ASSIGNMENT_ID+" = "+insertID,null,null,null,null);
        c.moveToFirst();
        Assignment newAssignment = cursorToAssignment(c);
        c.close();
        return newAssignment;
    }
    public void deleteAssignment(Assignment assignment){
        long id = assignment.getId();
        System.out.println("the deleted assignment has the id: " + id);
        db.delete(DBHandler.ASSIGNMENT_TABLE, DBHandler.ASSIGNMENT_ID
                + " = " + id, null);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void updateOrDeleteAssignment(Assignment assignment){
        long id = assignment.getId();
        String rep = assignment.getRepeat();
        if(rep.equals("Una Vez")){
            deleteAssignment(assignment);
            return;
        }
        Calendar c = Calendar.getInstance();
        Calendar insertDate = Calendar.getInstance();
        insertDate.setTime(assignment.getDue_date());
        insertDate.set(Calendar.HOUR_OF_DAY, 0);
        insertDate.set(Calendar.MINUTE, 0);
        insertDate.set(Calendar.SECOND, 0);
        insertDate.set(Calendar.MILLISECOND, 0);

        if(rep.equals("Diario")) {
            insertDate.add(Calendar.DATE, 1);
            Log.d(TAG, "added course : " + rep);
            Log.d(TAG, "added course : " + insertDate.getTime());
        }

        if(rep.equals("Semanal")) {
            insertDate.add(Calendar.DATE, 7);
        }
        if(rep.equals("Mensual")) {
            insertDate.add(Calendar.MONTH, 1);
        }
        if(rep.equals("Anual")) {
            insertDate.add(Calendar.YEAR, 1);
        }

        assignment.setDue_date(insertDate.getTime());ContentValues cv = new ContentValues();
        cv.put(DBHandler.ASSIGNMENT_TITLE,assignment.getTitle());
        cv.put(DBHandler.ASSIGNMENT_DESCRIPTION,assignment.getDesc());
        String newDate = formatter.format(insertDate.getTime());
        cv.put(DBHandler.ASSIGNMENT_DATE,newDate);
        cv.put(DBHandler.ASSIGNMENT_PRIORITY,assignment.getPriority());
        cv.put(DBHandler.ASSIGNMENT_REPEAT,assignment.getRepeat());
        cv.put(DBHandler.ASSIGNMENT_COURSE_ID,assignment.getCourse().getId());
        db.update(DBHandler.ASSIGNMENT_TABLE,cv,DBHandler.ASSIGNMENT_ID+" = "+assignment.getId(),null);
        Log.d(TAG, "added course : " + assignment.getDue_date());
    }
    public List<Assignment> getAllAssignments() throws ParseException {
        List<Assignment> listAs = new ArrayList<Assignment>();
        Cursor c = db.query(DBHandler.ASSIGNMENT_TABLE,asAllColumns,null,null,null,null,null);
        if(c!=null){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Assignment as = cursorToAssignment(c);
                listAs.add(as);
                c.moveToNext();
            }
            c.close();
        }
        return listAs;
    }

    public List<Assignment> getAssignmentByDate(Date date) throws ParseException {
        List<Assignment> assignmentList = new ArrayList<Assignment>();
        Cursor cursor = db.query(DBHandler.ASSIGNMENT_TABLE,asAllColumns ,
                DBHandler.ASSIGNMENT_DATE + " = ?",
                new String[] { formatter.format(date) }, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Assignment assignment = cursorToAssignment(cursor);
            assignmentList.add(assignment);
            cursor.moveToNext();
        }
        cursor.close();
        return assignmentList;
    }

    private Assignment cursorToAssignment(Cursor cursor) throws ParseException {
        Assignment assignment = new Assignment();
        assignment.setId(cursor.getLong(0));
        assignment.setTitle(cursor.getString(1));
        assignment.setDesc(cursor.getString(2));
        long courseId = cursor.getLong(3);
        CourseDAO cDAO =  new CourseDAO(ctx);
        Course course = cDAO.getCourseByID(courseId);
        if(course!=null) {
            assignment.setCourse(course);
        }
        assignment.setDue_date(formatter.parse(cursor.getString(4)));
        assignment.setPriority(cursor.getInt(5));
        assignment.setRepeat(cursor.getString(6));
        return assignment;
    }
}
