package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pl_son.Classes.Teacher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class AddUpdateHomeworkActivity extends AppCompatActivity implements View.OnClickListener {

    public Button logOut;
    public Button message;
    public Button course;

    public TextView date;
    public TextView time,textView2;
    Button timebtn,datebtn,add_hw,back_page;
    public int hour,minute,day,month,year;
    private boolean date_selected,hw_completed,date_change;
    public Calendar calendar,datetime;
    public EditText explain_hw;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_homework);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());

        course=findViewById(R.id.course);
        message=findViewById(R.id.message);
        logOut=findViewById(R.id.logOut);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        textView2=findViewById(R.id.textView2);
        timebtn=findViewById(R.id.timebtn);
        datebtn=findViewById(R.id.date_btn);
        explain_hw=findViewById(R.id.explain_hw);
        add_hw=findViewById(R.id.add_hw);
        back_page=findViewById(R.id.back_page);
        calendar=Calendar.getInstance();
        datetime = Calendar.getInstance();
        date_selected=false;
        hw_completed=false;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        day = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH);
        year= calendar.get(Calendar.YEAR);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);

        Intent intent = getIntent();
        final int teachertid = intent.getIntExtra("id", -1);
        final int courseid = intent.getIntExtra("course_id", -1);
        final int state = intent.getIntExtra("state", -1);
        int homeworkid;

        String[] user = databaseAccess.userLogin(teachertid);
        final Teacher teacher = new Teacher(Integer.parseInt(user[0]), user[1], user[2], user[3], user[4], Integer.parseInt(user[5]));
        homeworkid=0;

        userName.setText(teacher.getName()+ " " + teacher.getSurname());
        int message_num= databaseAccess.userButtons(teachertid);
        message_number.setText(" (" +message_num+")");

        if(state==0){
            date_change=true;
            add_hw.setText("Add homework");
        }
        else{
            date_change=false;
            add_hw.setText("Update homework");
            homeworkid = intent.getIntExtra("homework_id", -1);
            final Map<String,String> homework_map= teacher.See_Homework(this, homeworkid);
            explain_hw.setText(homework_map.get("homework"));
            textView2.setText(homework_map.get("date"));
        }

        final int finalHomeworkid = homeworkid;
        datebtn.setOnClickListener(this);
        timebtn.setOnClickListener(this);

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


        add_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    if (!explain_hw.getText().toString().equalsIgnoreCase("") && hw_completed) {
                        teacher.Add_Homework(getApplicationContext(), courseid, teachertid, explain_hw.getText().toString(), databaseAccess.EditDate(datetime));
                        Intent intent = new Intent(getApplicationContext(), CourseInfoTeacherActivity.class);
                        intent.putExtra("id", teachertid);
                        intent.putExtra("course_id", courseid);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select valid date and fill the homework space.", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if(!explain_hw.getText().toString().equalsIgnoreCase("") && (hw_completed || !date_change)){
                        if(hw_completed)
                            teacher.Update_Homework(getApplicationContext(), finalHomeworkid, explain_hw.getText().toString(), databaseAccess.EditDate(datetime));
                        else if(!date_change)
                            teacher.Update_Homework(getApplicationContext(), finalHomeworkid, explain_hw.getText().toString(), textView2.getText().toString());
                        Intent intent = new Intent(getApplicationContext(),HomeworkDetailsActivity.class);
                        intent.putExtra("id", teachertid);
                        intent.putExtra("course_id", courseid);
                        intent.putExtra("homework_id", finalHomeworkid);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please select valid date and fill the homework space.",Toast.LENGTH_LONG).show();
                    }
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
    }

    @Override
    public void onClick(View view){
        if(view == timebtn && date_selected) {
            TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);
                    if (datetime.getTimeInMillis() >= calendar.getTimeInMillis()) {
                        time.setText(datetime.get(Calendar.HOUR_OF_DAY) + ":" + datetime.get(Calendar.MINUTE));
                        hw_completed=true;
                    }
                    else {
                        Toast.makeText(AddUpdateHomeworkActivity.this, "Invalid Time", Toast.LENGTH_LONG).show();
                    }

                }
            }, hour, minute, true);
            tpd.show();
        }
        else if(view == timebtn && !date_selected){
            Toast.makeText(AddUpdateHomeworkActivity.this, "First select date.", Toast.LENGTH_LONG).show();
        }

        else if(view== datebtn){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int myear, int monthOfYear, int dayOfMonth) {
                            datePicker.setMinDate(System.currentTimeMillis() - 1000);
                            datetime.set(Calendar.YEAR,myear);
                            datetime.set(Calendar.MONTH,monthOfYear);
                            datetime.set(Calendar.DATE,dayOfMonth);
                            if (datetime.getTimeInMillis() >= calendar.getTimeInMillis()) {
                                date.setText(dayOfMonth+"."+(monthOfYear+1)+"."+myear);
                                date_selected=true;
                                date_change=true;
                            }
                            else {
                                Toast.makeText(AddUpdateHomeworkActivity.this, "Invalid date", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, year, month, day);
            datePickerDialog.show();
        }
    }
}
