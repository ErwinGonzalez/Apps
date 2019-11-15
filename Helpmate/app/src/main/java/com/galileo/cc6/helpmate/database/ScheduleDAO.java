package com.galileo.cc6.helpmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.galileo.cc6.helpmate.dataModels.Course;
import com.galileo.cc6.helpmate.dataModels.Schedule;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by EEVGG on 20/10/2016.
 */

public class ScheduleDAO {
    public static final String TAG = "ScheduleDAO";



    private SQLiteDatabase db;
    private DBHandler dbh;
    private Context ctx;
    private String[] scAllColumns = {
            DBHandler.SCHEDULE_ID,
            DBHandler.SCHEDULE_DAY,
            DBHandler.SCHEDULE_START,
            DBHandler.SCHEDULE_END,
            DBHandler.SCHEDULE_COURSE_ID
    };
    public ScheduleDAO(Context ctx){
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
    public Schedule createSchedule(int day, String start, String end, long courseId){
        ContentValues cv = new ContentValues();
        cv.put(DBHandler.SCHEDULE_DAY,day);
        cv.put(DBHandler.SCHEDULE_START,start);
        cv.put(DBHandler.SCHEDULE_END,end);
        cv.put(DBHandler.SCHEDULE_COURSE_ID,courseId);
        long insertID = db.insert(DBHandler.SCHEDULE_TABLE,null,cv);
        Cursor c = db.query (DBHandler.SCHEDULE_TABLE, scAllColumns,DBHandler.SCHEDULE_ID+" = "+insertID,null,null,null,null);
        c.moveToFirst();
        Schedule newSchedule = cursorToSchedule(c);
        c.close();
        return newSchedule;
    }

    public void deleteSchedule(Schedule schedule){
        long id = schedule.getId();
        System.out.println("the deleted schedule has the id: " + id);
        db.delete(DBHandler.SCHEDULE_TABLE, DBHandler.SCHEDULE_ID
                + " = " + id, null);
    }

    public List<Schedule> getAllSchedules(){
        List<Schedule> listSc = new ArrayList<Schedule>();
        Cursor c = db.query(DBHandler.SCHEDULE_TABLE, scAllColumns,null,null,null,null,null);
        if(c!=null){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Schedule sc = cursorToSchedule(c);
                listSc.add(sc);
                c.moveToNext();
            }
            c.close();
        }
        return listSc;
    }

    public List<Schedule> getScheduleOfCourse(long courseId){
        List<Schedule> scheduleList = new ArrayList<Schedule>();
        Cursor cursor = db.query(DBHandler.SCHEDULE_TABLE,scAllColumns ,
                DBHandler.SCHEDULE_COURSE_ID + " = ?",
                new String[] { String.valueOf(courseId) }, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Schedule schedule = cursorToSchedule(cursor);
            scheduleList.add(schedule);
            cursor.moveToNext();
        }
        cursor.close();
        return scheduleList;
    }
    public List<Schedule> getScheduleByDay(int day){
        List<Schedule> scheduleList = new ArrayList<Schedule>();
        Cursor cursor = db.query(DBHandler.SCHEDULE_TABLE,scAllColumns ,
                DBHandler.SCHEDULE_DAY + " = ?",
                new String[] { String.valueOf(day) }, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Schedule schedule = cursorToSchedule(cursor);
            scheduleList.add(schedule);
            cursor.moveToNext();
        }
        cursor.close();
        return scheduleList;
    }

    private Schedule cursorToSchedule(Cursor cursor){
        Schedule schedule = new Schedule();
        schedule.setId(cursor.getLong(0));
        schedule.setDay(cursor.getInt(1));
        schedule.setStartTime(cursor.getString(2));
        schedule.setEndTime(cursor.getString(3));

        long courseId = cursor.getLong(4);
        CourseDAO cDAO =  new CourseDAO(ctx);
        Course course = cDAO.getCourseByID(courseId);
        if(course!=null) {
            schedule.setCourse(course);
        }
        return schedule;
    }
}
