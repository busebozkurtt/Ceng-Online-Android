package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;

public class SeeHomeworkActivity extends AppCompatActivity {
    public TextView name;
    public TextView e_mail;
    public TextView delivery_date;
    public TextView homeworktext;
    public Button course;
    public Button message;
    public Button logOut;
    public Button back_page;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_homework);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        name=findViewById(R.id.name);
        e_mail=findViewById(R.id.email);
        delivery_date=findViewById(R.id.delivery_date);
        homeworktext=findViewById(R.id.homework);
        course=findViewById(R.id.course);
        message=findViewById(R.id.messages);
        logOut=findViewById(R.id.logOut);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);
        back_page=  findViewById(R.id.back);

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        final int  teacherid = intent.getIntExtra("id", -1);
        final int  courseid = intent.getIntExtra("course_id", 5);
        String nameSurname=intent.getStringExtra("name");
        String homework=intent.getStringExtra("homework");
        String delivery=intent.getStringExtra("delivery_date");
        final int studentid=intent.getIntExtra("student_id",-1);
        final int homeworkid=intent.getIntExtra("homework_id",-1);
        String []userStudent=databaseAccess.userLogin(studentid);
        final Student student = new Student(Integer.parseInt(userStudent[0]),userStudent[1],userStudent[2],userStudent[3],userStudent[4],Integer.parseInt(userStudent[5]));
        String []userTeacher=databaseAccess.userLogin(teacherid);
        final Teacher teacher = new Teacher(Integer.parseInt(userTeacher[0]),userTeacher[1],userTeacher[2],userTeacher[3],userTeacher[4],Integer.parseInt(userTeacher[5]));
        name.setText(nameSurname);
        e_mail.setText(userStudent[1]);
        delivery_date.setText(delivery);
        homeworktext.setText(homework);

        int message_num= databaseAccess.userButtons(teacherid);
        userName.setText(teacher.getName()+ " " + teacher.getSurname());
        message_number.setText(" (" +message_num+")");


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacher.setId(0);
                teacher.setEmail("");
                teacher.setName("");
                teacher.setPassword("");
                teacher.setSurname("");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userId",teacherid);
                startActivity(intent);
            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TeacherActivity.class);
                intent.putExtra("teacher", teacherid);
                startActivity(intent);
            }
        });

       back_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeworkDetailsActivity.class);
                intent.putExtra("course_id",courseid);
                intent.putExtra("id", teacherid);
                intent.putExtra("homework_id", homeworkid);
                startActivity(intent);
                finish();
            }
        });



    }
}
