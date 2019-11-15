package com.galileo.cc6.helpmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.galileo.cc6.helpmate.dataModels.Assignment;
import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.dataModels.Schedule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EEVGG on 20/10/2016.
 */

public class CourseDAO {
    public static final String TAG = "CourseDAO";

    private SQLiteDatabase db;
    private DBHandler dbh;
    private Context ctx;
    private String[] cAllColumns = {
            DBHandler.COURSE_ID,
            DBHandler.COURSE_NAME,
            DBHandler.COURSE_COLOR
    };
    public CourseDAO(Context ctx){
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

    public Course createCourse(String name, String color){
        ContentValues cv = new ContentValues();
        cv.put(DBHandler.COURSE_NAME,name);
        cv.put(DBHandler.COURSE_COLOR,color);
        long insertID = db.insert(DBHandler.COURSE_TABLE,null,cv);
        Cursor c = db.query(DBHandler.COURSE_TABLE,cAllColumns,DBHandler.COURSE_ID+" = "+insertID,null,null,null,null);
        c.moveToFirst();
        Course newCourse = cursorToCourse(c);
        c.close();
        return newCourse;
    }

    public void deleteCourse(Course course) throws ParseException {
        long id = course.getId();
        ScheduleDAO scheduleDAO = new ScheduleDAO(ctx);
        List<Schedule> listSchedule = scheduleDAO.getScheduleOfCourse(id);
        if (listSchedule != null && !listSchedule.isEmpty()) {
            for (Schedule e : listSchedule) {
                scheduleDAO.deleteSchedule(e);
            }
        }
        AssignmentDAO assignmentDAO = new AssignmentDAO(ctx);
        List<Assignment> assignmentList = assignmentDAO.getAllAssignments();
        if(assignmentList!= null && !assignmentList.isEmpty()){
            for (Assignment a: assignmentList){
                if(a.getCourse().getId()==id){
                    assignmentDAO.deleteAssignment(a);
                }
            }
        }

        //System.out.println("the deleted course has the id: " + id);
        db.delete(DBHandler.COURSE_TABLE, DBHandler.COURSE_ID
                + " = " + id, null);

    }

    public List<Course> getAllCourses(){
        List<Course> listC = new ArrayList<Course>();
        Cursor c = db.query(DBHandler.COURSE_TABLE,cAllColumns,null,null,null,null,null);
        if(c!=null){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Course course = cursorToCourse(c);
                listC.add(course);
                c.moveToNext();
            }
            c.close();
        }
        return listC;
    }
    public Course getCourseByID(long id){
        Cursor c = db.query(DBHandler.COURSE_TABLE,cAllColumns,
                DBHandler.COURSE_ID+" = ?",new String[] { String.valueOf(id) }, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }

        return cursorToCourse(c);
    }
    protected Course cursorToCourse(Cursor cursor){
        Course course = new Course();
        course.setId(cursor.getLong(0));
        course.setName(cursor.getString(1));
        course.setColor(cursor.getString(2));
        return course;
    }
}
