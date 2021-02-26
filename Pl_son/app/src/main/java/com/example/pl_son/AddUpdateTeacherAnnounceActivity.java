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

import com.example.pl_son.Classes.Teacher;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AddUpdateTeacherAnnounceActivity extends AppCompatActivity {
    public EditText announce;
    public TextView teacher_name;
    public Button add,back_page;
    public int state,announceid;
    public Button logOut;
    public Button message;
    public Button course;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_teacher_announce);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        announce=(EditText)findViewById(R.id.announce);
        teacher_name=(TextView)findViewById(R.id.teacher_name);
        add=findViewById(R.id.add);
        back_page=findViewById(R.id.back_page);
        course=findViewById(R.id.course);
        message=findViewById(R.id.messages);
        logOut=findViewById(R.id.logOut);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);



        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        final int  teachertid = intent.getIntExtra("id", -1);
        final int  courseid = intent.getIntExtra("course_id", -1);
        state = intent.getIntExtra("state", -1);
        String []user=databaseAccess.userLogin(teachertid);
        final Teacher teacher = new Teacher(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));

        userName.setText(teacher.getName()+ " " + teacher.getSurname());
        int message_num= databaseAccess.userButtons(teachertid);
        message_number.setText(" (" +message_num+")");

        if(state==1){
            announceid = intent.getIntExtra("announce_id", -1);
            Map<String,String> announce_map= teacher.See_Announce(this, announceid);
            String s = teacher.getName()+" "+teacher.getSurname() + " Date: " + announce_map.get("date");
            teacher_name.setText(s);
            announce.setText(announce_map.get("announce"));
            add.setText("Update");
        }
        else {
            Calendar calendar = Calendar.getInstance();
            final String s = teacher.getName() + " " + teacher.getSurname() + " Date: " + databaseAccess.EditDate(calendar);
            teacher_name.setText(s);
            add.setText("Add");
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==0) {
                    String new_anounce = announce.getText().toString();
                    if (new_anounce.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please fill in the fields", Toast.LENGTH_LONG).show();
                    } else {
                        teacher.Add_Announce(getApplicationContext(), courseid, teachertid, new_anounce);
                        Toast.makeText(getApplicationContext(), "Announce is added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CourseInfoTeacherActivity.class);
                        intent.putExtra("id", teachertid);
                        intent.putExtra("course_id", courseid);
                        startActivity(intent);
                    }
                }

                else{
                    String new_anounce = announce.getText().toString();
                    if(new_anounce.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Please fill in the fields",Toast.LENGTH_LONG).show();
                    }
                    else {
                        teacher.Update_Announce(getApplicationContext(),announceid,new_anounce);
                        Toast.makeText(getApplicationContext(),"Announce is updated",Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(getApplicationContext(),CourseInfoTeacherActivity.class);
                    intent.putExtra("id", teachertid);
                    intent.putExtra("course_id", courseid);
                    startActivity(intent);
                }
            }
        });

        back_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CourseInfoTeacherActivity.class);
                intent.putExtra("id", teachertid);
                intent.putExtra("course_id", courseid);
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

    }
}
