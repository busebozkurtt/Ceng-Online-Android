package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pl_son.Adapter.Announce_ListAdapter;
import com.example.pl_son.Adapter.Assignment_ListAdapter;
import com.example.pl_son.Adapter.Post_ListAdapter;
import com.example.pl_son.Classes.Announce;
import com.example.pl_son.Classes.Assignment;
import com.example.pl_son.Classes.Post;
import com.example.pl_son.Classes.Teacher;

import java.util.ArrayList;

public class CourseInfoTeacherActivity extends AppCompatActivity {

    public Button logOut;
    public Button message;
    public Button course;

    public ImageButton update;
    public Button asssignment_button,stream;
    public Button announce;
    public Button add;
    public TextView name;
    public TextView date;
    public ImageButton add_student;
    public int state=2;
    public ListView course_content;
    public EditText course_name;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info_teacher);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        update=findViewById(R.id.update);
        add_student=findViewById(R.id.add_student);
        course=findViewById(R.id.course);
        message=findViewById(R.id.message);
        logOut=findViewById(R.id.logOut);
        name=findViewById(R.id.name);
        date=findViewById(R.id.date);
        asssignment_button=findViewById(R.id.assignment_button);
        announce=findViewById(R.id.announce);
        stream=findViewById(R.id.stream);
        add=findViewById(R.id.add);
        course_name=findViewById(R.id.editText);
        course_content=(ListView)findViewById(R.id.course_content);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();

        int  teacherid = intent.getIntExtra("id", -1);
        final int  courseid = intent.getIntExtra("course_id", 5);
        String []user=databaseAccess.userLogin(teacherid);

        final Teacher teacher = new Teacher(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));
        userName.setText(teacher.getName()+ " " + teacher.getSurname());
        int message_num= databaseAccess.userButtons(teacherid);
        message_number.setText(" (" +message_num+")");


        ArrayList<Assignment> homeworks= databaseAccess.userHomework(teacherid,courseid);
        ArrayList<Announce> announcement= databaseAccess.userAnnounce(teacherid,courseid);
        ArrayList<Post> post= databaseAccess.Course_Posts(this,courseid);
        final Assignment_ListAdapter homework_adapter = new Assignment_ListAdapter(this,homeworks,getApplicationContext(),databaseAccess,false,teacherid);
        final Announce_ListAdapter announce_adapter = new Announce_ListAdapter(this,announcement,getApplicationContext(), databaseAccess,false,teacherid);
        final Post_ListAdapter post_adapter = new Post_ListAdapter(this,post,getApplicationContext(), databaseAccess,teacherid,courseid);


        course_content.setAdapter(post_adapter);

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
                intent.putExtra("userId",teacher.getId());
                startActivity(intent);

            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TeacherActivity.class);
                intent.putExtra("teacher", teacher.getId());
                startActivity(intent);
            }
        });

        stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Post");
                date.setText("Date");
                course_content.setAdapter(post_adapter);
                state=2;
                add.setText("Add Post");
            }
        });

        asssignment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Homework");
                date.setText("Due Date");
                course_content.setAdapter(homework_adapter);
                state=0;
                add.setText("Add Assignment");
            }
        });

        announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("Announce");
                date.setText("Date");
                course_content.setAdapter(announce_adapter);
                state=1;
                add.setText("Add Announcement");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course_namee = course_name.getText().toString();
                if(course_namee.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please fill in the fields",Toast.LENGTH_LONG).show();
                }
                else {
                    teacher.Update_Course(getApplicationContext(),courseid,course_namee);
                    Toast.makeText(getApplicationContext(),"Course name is updated",Toast.LENGTH_LONG).show();
                }
            }
        });

        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = course_name.getText().toString();
                if(email.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please fill in the fields",Toast.LENGTH_LONG).show();
                }
                else {
                    int state = teacher.Add_Student_to_Course(getApplicationContext(), courseid, email);
                    switch (state) {
                        case 0:
                            Toast.makeText(getApplicationContext(), "Mail is wrong.", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(getApplicationContext(), "User is already added.", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(getApplicationContext(), "User is added.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==0){
                    Intent intent = new Intent(getApplicationContext(), AddUpdateHomeworkActivity.class);
                    intent.putExtra("course_id", courseid);
                    intent.putExtra("id",teacher.getId());
                    intent.putExtra("state", 0); //add
                    startActivity(intent);
                }
                else if(state==1){
                    Intent intent = new Intent(getApplicationContext(), AddUpdateTeacherAnnounceActivity.class);
                    intent.putExtra("course_id", courseid);
                    intent.putExtra("id",teacher.getId());
                    intent.putExtra("state", 0); //add
                    startActivity(intent);
                }
                else if(state==2){
                    Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                    intent.putExtra("post_id", 0);
                    intent.putExtra("course_id", courseid);
                    intent.putExtra("id", teacher.getId());
                    intent.putExtra("state", 0); //add
                    startActivity(intent);
                }
            }
        });

    }
}
