package com.example.pl_son.Classes;

public class Assignment {
    int id;
    int teacher_id;
    String due_date;
    int course_id;
    String homework;
    boolean is_done;

    public Assignment(int id, int teacher_id, String due_date, int course_id, String homework,boolean is_done) {
        this.id = id;
        this.teacher_id = teacher_id;
        this.due_date = due_date;
        this.course_id = course_id;
        this.homework = homework;
        this.is_done=is_done;
    }

    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public int getTeacher_id() {return teacher_id;}
    public void setTeacher_id(int teacher_id) {this.teacher_id = teacher_id;}

    public String getDue_date() {return due_date;}
    public void setDue_date(String due_date) {this.due_date = due_date;}

    public int getCourse_id() {return course_id;}
    public void setCourse_id(int course_id) {this.course_id = course_id;}

    public String getHomework() {return homework;}
    public void setHomework(String homework) {this.homework = homework;}

    public boolean isIs_done() {return is_done;}
    public void setIs_done(boolean is_done) {this.is_done = is_done;}

}
