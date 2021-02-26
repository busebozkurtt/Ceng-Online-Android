package com.example.pl_son.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pl_son.Classes.Course;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;
import com.example.pl_son.CourseInfoTeacherActivity;
import com.example.pl_son.CourseStudentActivity;
import com.example.pl_son.R;

import java.util.ArrayList;
import java.util.List;

public class Course_ListAdapterr extends BaseAdapter {

    List<Course> course= new ArrayList<Course>();
    LayoutInflater courseInflater;
    User user;
    Context c;
    Activity activity;
    public Course_ListAdapterr(Activity activity, ArrayList<Course> course, Context c, User user) {
        this.activity=activity;
        this.course = course;
        courseInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.c=c;
        if(user.getIs_teacher()==1) {
            this.user = new Teacher(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getIs_teacher());
        }
        else {
            this.user = new Student(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getIs_teacher());
        }
    }

    @Override
    public int getCount() {
        return course.size();
    }

    @Override
    public Object getItem(int position) {
        return course.get(position);
    }

    @Override
    public long getItemId(int position) {
        return course.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row_view ;

        if(user.getIs_teacher()==0){
            row_view = courseInflater.inflate(R.layout.row,null);
        }
        else{
            row_view = courseInflater.inflate(R.layout.row_teacher,null);
            Button delete=(Button) row_view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Course> temp=(ArrayList<Course>) ((ArrayList<Course>) course).clone();
                    for(Course c: temp){
                        if(c.getId()==course.get(position).getId()) {
                            course.remove(c);
                            break;
                        }
                    }
                    Teacher teacher = new Teacher(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getIs_teacher());
                    teacher.Delete_Course(c, temp.get(position).getId());
                    Course_ListAdapterr.this.notifyDataSetChanged();
                }
            });
        }

        Button course_name =(Button) row_view.findViewById(R.id.course_name);
        course_name.setText(course.get(position).getCourse_Name());
        course_name.setText(course.get(position).getCourse_Name());
        course_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getIs_teacher()==0) {
                    Intent intent = new Intent(c, CourseStudentActivity.class);
                    intent.putExtra("course_id", course.get(position).getId());
                    intent.putExtra("id", user.getId());
                    c.startActivity(intent);
                    activity.finish();
                }
                else {
                    Intent intent = new Intent(c, CourseInfoTeacherActivity.class);
                    intent.putExtra("course_id", course.get(position).getId());
                    intent.putExtra("id", user.getId());
                    c.startActivity(intent);
                    activity.finish();
                }
            }
        });
       
        return row_view;
    }
}
