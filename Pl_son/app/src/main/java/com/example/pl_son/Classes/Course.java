package com.example.pl_son.Classes;

import java.io.Serializable;

public class Course implements Serializable {
    private int id;
    private String Course_Name;

    public Course(int id, String course_Name) {
        this.id = id;
        Course_Name = course_Name;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getCourse_Name() {return Course_Name;}
    public void setCourse_Name(String course_Name) {Course_Name = course_Name;}

}
