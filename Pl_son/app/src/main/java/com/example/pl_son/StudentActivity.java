package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pl_son.Adapter.Announce_ListAdapter;
import com.example.pl_son.Adapter.Assignment_ListAdapter;
import com.example.pl_son.Adapter.Course_ListAdapterr;
import com.example.pl_son.Classes.Announce;
import com.example.pl_son.Classes.Assignment;
import com.example.pl_son.Classes.Course;
import com.example.pl_son.Classes.Message;
import com.example.pl_son.Classes.Post;
import com.example.pl_son.Classes.Student;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    public Button logOut;
    public Button announce;
    public Button homework;
    public Button course;
    public Button messages;
    public ListView listView;
    public TextView message_number,userName;
    public TextView name;
    public TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        logOut=findViewById(R.id.logOut);
        announce=findViewById(R.id.announce);
        homework=findViewById(R.id.homeworks);
        messages=findViewById(R.id.messages);
        course=findViewById(R.id.course);
        listView=(ListView)findViewById(R.id.list_view);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);
        name=findViewById(R.id.name);
        date=findViewById(R.id.date);

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        int  studentid = intent.getIntExtra("student", -1);
        String []user=databaseAccess.userLogin(studentid);
        final Student studentt = new Student(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));

        ArrayList<Course> courses = databaseAccess.userCourse(studentt.getId());
        ArrayList<Message> message= databaseAccess.userMessages(studentt.getId());
        ArrayList<Assignment> homeworks= databaseAccess.userHomework(studentt.getId());
        ArrayList<Announce> announcement= databaseAccess.userAnnounce(studentt.getId());

        int message_num= databaseAccess.userButtons(studentid);
        userName.setText(studentt.getName()+ " " + studentt.getSurname());
        message_number.setText(" (" +message_num+")");
        name.setVisibility(View.GONE);
        date.setVisibility(View.GONE);

        final Course_ListAdapterr course_adapter = new Course_ListAdapterr(this, courses,getApplicationContext(),studentt);
        final Assignment_ListAdapter homework_adapter = new Assignment_ListAdapter(this,homeworks,getApplicationContext(), databaseAccess,false,studentid);
        final Announce_ListAdapter announce_adapter = new Announce_ListAdapter(this,announcement,getApplicationContext(), databaseAccess,true,studentt.getId());


        listView.setAdapter(course_adapter);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentt.setId(0);
                studentt.setEmail("");
                studentt.setName("");
                studentt.setPassword("");
                studentt.setSurname("");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userId",studentt.getId());
                startActivity(intent);
            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                listView.setAdapter(course_adapter);
            }
        });

        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(homework_adapter);
                name.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                name.setText("Homework");
                date.setText("Due Date");
            }
        });

        announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                name.setText("Announce");
                date.setText("Date");
                listView.setAdapter(announce_adapter);
            }
        });
    }
}
