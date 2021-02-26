package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pl_son.Adapter.Assignment_ListAdapter;
import com.example.pl_son.Adapter.StudentDeliverers_ListAdapter;
import com.example.pl_son.Classes.Teacher;

import java.util.ArrayList;
import java.util.Map;

public class HomeworkDetailsActivity extends AppCompatActivity {

    Button update,course,message,logOut;
    public ListView delivered_homework;
    public TextView message_number,userName;
    public Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_details);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        update=findViewById(R.id.update);
        delivered_homework=(ListView)findViewById(R.id.listStudent);
        course=findViewById(R.id.course);
        message=findViewById(R.id.message);
        logOut=findViewById(R.id.logOut);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);
        back=findViewById(R.id.back);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        Intent intent = getIntent();
        final int teacherid = intent.getIntExtra("id", -1);
        final int courseid = intent.getIntExtra("course_id", -1);
        final int  homeworkid = intent.getIntExtra("homework_id", -1);
        String[] user = databaseAccess.userLogin(teacherid);
        final Teacher teacher = new Teacher(Integer.parseInt(user[0]), user[1], user[2], user[3], user[4], Integer.parseInt(user[5]));
        ArrayList<Map<String, String>> list=databaseAccess.UserAssignment(homeworkid);
        final StudentDeliverers_ListAdapter deliveredList_adapter = new StudentDeliverers_ListAdapter(this,list,getApplicationContext(),databaseAccess,teacherid,courseid,homeworkid);
        delivered_homework.setAdapter(deliveredList_adapter);
        userName.setText(teacher.getName()+ " " + teacher.getSurname());
        int message_num= databaseAccess.userButtons(teacherid);
        message_number.setText(" (" +message_num+")");


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddUpdateHomeworkActivity.class);
                intent.putExtra("id", teacherid);
                intent.putExtra("course_id", courseid);
                intent.putExtra("homework_id", homeworkid);
                intent.putExtra("state", 1); //update
                startActivity(intent);
            }
        });

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseInfoTeacherActivity.class);
                intent.putExtra("course_id", courseid);
                intent.putExtra("id", teacher.getId());
                startActivity(intent);
            }
        });
    }
}
