package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pl_son.Classes.Student;

import java.util.Map;

public class Announce_Homework_SubmitActivity extends AppCompatActivity {
    public Button logOut;
    public Button course;
    public Button messages;
    public Button back_page;
    public Button submit;
    public TextView homework_submit;
    public EditText homework;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_homework_submit);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        final int course_id;
        logOut=findViewById(R.id.logOut);
        messages=findViewById(R.id.messages);
        course=findViewById(R.id.course);
        back_page=findViewById(R.id.back_page);
        submit=findViewById(R.id.submit);
        homework=findViewById(R.id.homework);
        homework_submit=(TextView)findViewById(R.id.homework_submit);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);


        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        final int  studentid = intent.getIntExtra("id", -1);
        int state = intent.getIntExtra("state", -1);
        int homework_done=intent.getIntExtra("homework_done", -1);
        String []user=databaseAccess.userLogin(studentid);
        final Student student = new Student(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));

        userName.setText(student.getName()+ " " + student.getSurname());
        int message_num= databaseAccess.userButtons(studentid);
        message_number.setText(" (" +message_num+")");

        int temp_homework=0;
        if(state==0) {
            temp_homework = intent.getIntExtra("homeworkid", -1);
            Map<String, String> homework_map = student.See_Homework(this, temp_homework);
            final String s = homework_map.get("teacher_name") + " Due Date: " + homework_map.get("date") + ": " + homework_map.get("homework");
            course_id = Integer.parseInt(homework_map.get("course_id"));
            homework_submit.setText(s);
            if(homework_done==1){
                Toast.makeText(getApplicationContext(), "Homework already submitted", Toast.LENGTH_LONG).show();
                String homeworkStudent=databaseAccess.FindHomework(studentid,intent.getIntExtra("homeworkid", -1));
                homework.setText(homeworkStudent);
                submit.setVisibility(View.GONE);
                homework.setEnabled(false);
            }
        }
        else{
            int  announceid = intent.getIntExtra("announce_id", -1);
            Map<String,String> announce_map= student.See_Announce(this, announceid);
            final String s = announce_map.get("teacher_name") + " Date: " + announce_map.get("date");
            homework_submit.setText(s);
            homework.setText(announce_map.get("announce"));
            course_id=Integer.parseInt(announce_map.get("course_id"));
            submit.setVisibility(View.GONE);
            homework.setEnabled(false);
        }
        final int homeworkid=temp_homework;

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student.setId(0);
                student.setEmail("");
                student.setName("");
                student.setPassword("");
                student.setSurname("");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
            }
        });
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StudentActivity.class);
                intent.putExtra("student", student.getId());
                startActivity(intent);
            }
        });

        back_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseStudentActivity.class);
                intent.putExtra("course_id",course_id);
                intent.putExtra("id", studentid);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String assignment = homework.getText().toString();
                if(!assignment.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), CourseStudentActivity.class);
                    intent.putExtra("course_id", course_id);
                    intent.putExtra("id", studentid);
                    student.Submit_Homework(getApplicationContext(), homeworkid, assignment, studentid);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill in the fields",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
