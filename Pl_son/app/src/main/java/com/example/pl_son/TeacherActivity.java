package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pl_son.Adapter.Course_ListAdapterr;
import com.example.pl_son.Classes.Course;
import com.example.pl_son.Classes.Teacher;

import java.util.ArrayList;

public class TeacherActivity extends AppCompatActivity {

    public Button add;
    public Button logOut;
    public Button course_button;
    public Button message;
    public ListView courses;
    public EditText course_name;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        add=findViewById(R.id.add);
        course_name=findViewById(R.id.course_name);
        course_button=findViewById(R.id.course);
        message=findViewById(R.id.message);
        logOut=findViewById(R.id.logOut);
        courses=(ListView)findViewById(R.id.courses);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);

        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        int  teacherid = intent.getIntExtra("teacher", -1);
        String []user=databaseAccess.userLogin(teacherid);
        final Teacher teacher= new Teacher(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));

        userName.setText(teacher.getName()+ " " + teacher.getSurname());
        int message_num= databaseAccess.userButtons(teacherid);
        message_number.setText(" (" +message_num+")");

        final ArrayList<Course> course = databaseAccess.userCourse(teacher.getId());
        final Course_ListAdapterr course_adapter = new Course_ListAdapterr(this, course,getApplicationContext(),teacher);
        courses.setAdapter(course_adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course_namee = course_name.getText().toString();
                if (!course_namee.equalsIgnoreCase("")) {
                    teacher.Add_Course(getApplicationContext(),course_namee);
                    course.add(teacher.Add_Course(getApplicationContext(),course_namee));
                    course_adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill in the receiver email", Toast.LENGTH_LONG).show();
                }
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
                intent.putExtra("userId",teacher.getId());
                startActivity(intent);

            }
        });

    }
}
