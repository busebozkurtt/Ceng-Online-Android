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
import com.example.pl_son.Adapter.Post_ListAdapter;
import com.example.pl_son.Classes.Announce;
import com.example.pl_son.Classes.Assignment;
import com.example.pl_son.Classes.Course;
import com.example.pl_son.Classes.Message;
import com.example.pl_son.Classes.Post;
import com.example.pl_son.Classes.Student;

import java.util.ArrayList;

public class CourseStudentActivity extends AppCompatActivity {
    public Button logOut;
    public Button announce;
    public Button homework;
    public Button course;
    public Button messages;
    public Button assignment;
    public Button stream;
    public TextView name;
    public TextView date;
    public ListView course_contents;
    public TextView course_name;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_student);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        logOut=findViewById(R.id.logOut);
        announce=findViewById(R.id.announce);
        assignment=findViewById(R.id.assignment_button);
        homework=findViewById(R.id.homeworks);
        messages=findViewById(R.id.messages);
        course=findViewById(R.id.course);
        name=findViewById(R.id.name);
        date=findViewById(R.id.date);
        course_contents=(ListView)findViewById(R.id.course_contents);
        course_name=findViewById(R.id.Coursenamee);
        stream=findViewById(R.id.stream);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        int  studentid = intent.getIntExtra("id", -1);
        int  courseid = intent.getIntExtra("course_id", 5);
        String []user=databaseAccess.userLogin(studentid);

        final Student student = new Student(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));
        final Course coursee= databaseAccess.return_course(courseid);

        course_name.setText(coursee.getCourse_Name());

        userName.setText(student.getName()+ " " + student.getSurname());
        int message_num= databaseAccess.userButtons(studentid);
        message_number.setText(" (" +message_num+")");

        ArrayList<Message> message= databaseAccess.userMessages(studentid);
        ArrayList<Assignment> homeworks= databaseAccess.userHomework(studentid,coursee.getId());
        ArrayList<Announce> announcement= databaseAccess.userAnnounce(studentid,coursee.getId());
        ArrayList<Post> post= databaseAccess.Course_Posts(this,courseid);

        final Assignment_ListAdapter homework_adapter = new Assignment_ListAdapter(this,homeworks,getApplicationContext(),databaseAccess,false,studentid);
        final Announce_ListAdapter announce_adapter = new Announce_ListAdapter(this,announcement,getApplicationContext(), databaseAccess,true,student.getId());
        final Post_ListAdapter post_adapter = new Post_ListAdapter(this,post,getApplicationContext(), databaseAccess,studentid,courseid);


        course_contents.setAdapter(post_adapter);

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

        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Homework");
                date.setText("Due Date");
                course_contents.setAdapter(homework_adapter);
            }
        });

        announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Announce");
                date.setText("Date");
                course_contents.setAdapter(announce_adapter);
            }
        });
        stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Post");
                date.setText("Date");
                course_contents.setAdapter(post_adapter);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userId",student.getId());
                startActivity(intent);
            }
        });
    }
}
