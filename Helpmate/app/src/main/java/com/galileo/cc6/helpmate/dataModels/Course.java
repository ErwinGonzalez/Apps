package com.galileo.cc6.helpmate.dataModels;

import java.io.Serializable;

/**
 * Created by EEVGG on 19/10/2016.
 */

public class Course implements Serializable {
    private long id;
    private String name, color;

    public Course(){}

    public Course(String name, String color){
        this.name=name;
        this.color=color;
    }

    public long getId(){
        return  id;
    }
    public void setId(long id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color=color;
    }
}

