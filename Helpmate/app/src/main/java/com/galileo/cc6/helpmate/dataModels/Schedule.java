package com.galileo.cc6.helpmate.dataModels;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by EEVGG on 20/10/2016.
 */

public class Schedule implements Serializable, Comparable<Schedule> {


    /*
     *Schedule day is saved by an integer, assigned the following way
     * Sunday ->0
     * Monday ->1
     * Tuesday ->2
     * Wednesday ->3
     * Thursday ->4
     * Friday ->5
     * Saturday ->6
    */

    private long id;
    private int day;
    private String start_time,end_time;
    private Course course;

    public Schedule(){}

    public Schedule(int day, String start,String end, Course course){
        this.day=day;
        this.start_time=start;
        this.end_time=end;
        this.course=course;
    }
    public static final Comparator<Schedule> schComparator = new Comparator<Schedule>() {
        @Override
        public int compare(Schedule s1, Schedule s2) {
            if(s1.getDay()==s2.getDay()){
                if (s1.getStartHour() == s2.getStartHour()) {
                    return s1.getStartMinute() - s2.getStartMinute();
                } else {
                    return s1.getStartHour() - s2.getStartHour();
                }
            }else{
                return s1.getDay()-s2.getDay();
            }
        }
    };
    public long getId(){
        return  id;
    }
    public void setId(long id){ this.id=id; }
    public int getDay(){
        return day;
    }
    public void setDay(int day){
        this.day=day;
    }
    public String getStartTime(){return this.start_time;}
    public void setStartTime(String start){this.start_time=start;}
    public void setEndTime(String end){this.end_time=end;}
    public String getEndTime(){return this.end_time;}
    public int getStartHour(){return Integer.parseInt(this.start_time.substring(0,2));}
    public int getStartMinute(){return Integer.parseInt(this.start_time.substring(3,5));}
    public int getEndHour(){return Integer.parseInt(this.end_time.substring(0,2));}
    public int getEndMinute(){return Integer.parseInt(this.end_time.substring(3,5));}
    public Course getCourse(){
        return course;
    }
    public void setCourse(Course course){
        this.course=course;
    }

    @Override
    public int compareTo(@NonNull Schedule o) {
        return 0;
    }
}
