package com.example.pl_son.Classes;

public class Announce {
    int id;
    int course_id;
    String announce;
    String announce_date;
    int teacher_id;
    boolean is_read;

    public Announce(int id, int course_id, String announce, String announce_date, int teacher_id,boolean is_read) {
        this.id = id;
        this.course_id = course_id;
        this.announce = announce;
        this.announce_date = announce_date;
        this.teacher_id = teacher_id;
        this.is_read=is_read;
    }

    public Announce(int id, int course_id, String announce, String announce_date, int teacher_id) {
        this.id = id;
        this.course_id = course_id;
        this.announce = announce;
        this.announce_date = announce_date;
        this.teacher_id = teacher_id;

    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getCourse_id() {return course_id;}
    public void setCourse_id(int course_id) { this.course_id = course_id; }

    public String getAnnounce() {return announce;}
    public void setAnnounce(String announce) {this.announce = announce;}

    public String getAnnounce_date() {return announce_date; }
    public void setAnnounce_date(String announce_date) { this.announce_date = announce_date; }

    public int getTeacher_id() {return teacher_id;}
    public void setTeacher_id(int teacher_id) { this.teacher_id = teacher_id;}

    public boolean isIs_read() { return is_read;}
    public void setIs_read(boolean is_read) {this.is_read = is_read;}

}
