package com.galileo.cc6.helpmate.dataModels;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by EEVGG on 03/11/2016.
 */

public class Assignment implements Serializable, Comparable<Assignment> {
    private long id;
    private String title;
    private String desc;
    private Course course;
    private Date due_date;
    private int priority;
    private String repeat;

    public Assignment(){}

    public Assignment(String title, String desc, Date date, int priority, String repeat, Course c){
        this.title=title;
        this.desc=desc;
        this.due_date=date;
        this.priority=priority;
        this.repeat=repeat;
        this.course=c;
    }

    public static final Comparator<Assignment> assignmentComparator = new Comparator<Assignment>(){
        @Override
        public int compare(Assignment a1, Assignment a2){
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(a1.getDue_date());
            c2.setTime(a2.getDue_date());
            int data =c1.compareTo(c2);
            if(data==0){
                return a2.getPriority()-a1.getPriority();
            }else{
                return data;
            }
        }
    };

    public long getId(){return this.id;}
    public void setId(long id){this.id=id;}
    public String getTitle(){ return this.title;}
    public void setTitle(String t){this.title=t;}
    public String getDesc(){return  this.desc;}
    public void setDesc(String d){this.desc=d;}
    public Date getDue_date(){return  this.due_date;}
    public  void setDue_date(Date d){this.due_date=d;}
    public int getPriority(){return this.priority;}
    public void setPriority(int p){this.priority=p;}
    public String getRepeat(){return this.repeat;}
    public void setRepeat(String r){this.repeat=r;}
    public Course getCourse(){return this.course;}
    public void setCourse(Course c){this.course=c;}

    @Override
    public int compareTo(@NonNull Assignment o) {
        return 0;
    }
}
