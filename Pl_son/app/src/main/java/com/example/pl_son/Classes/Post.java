package com.example.pl_son.Classes;

public class Post {
    int id;
    int course_id;
    String post;
    String post_date;
    int teacher_id;

    public Post(int id, int course_id, String post, String post_date, int teacher_id) {
        this.id = id;
        this.course_id = course_id;
        this.post = post;
        this.post_date = post_date;
        this.teacher_id = teacher_id;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getCourse_id() {return course_id;}
    public void setCourse_id(int course_id) {this.course_id=course_id;}

    public String getPost() {return post;}
    public void setPost(String post) {this.post=post;}

    public String getpost_date() {return post_date;}
    public void setpost_date(String post_date) {this.post_date=post_date;}

    public int getTeacher_id() {return teacher_id;}
    public void setTeacher_id(int teacher_id) {this.teacher_id=teacher_id;}

}
