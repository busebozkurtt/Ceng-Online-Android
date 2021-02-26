package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;

public class MainActivity extends AppCompatActivity {
    public EditText mail;
    public EditText password;
    public Button buttonSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        mail=findViewById(R.id.mail);
        password=findViewById(R.id.password);
        buttonSignin=findViewById(R.id.buttonSignin);
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
                String maill=mail.getText().toString();
                String passwordd=password.getText().toString();
                if(maill.equalsIgnoreCase("") || passwordd.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please fill in the fields",Toast.LENGTH_LONG).show();
                }
                else {
                    String []user=databaseAccess.userLogin(maill,passwordd);

                    switch (user[5]) {
                        case "0" :
                            Student student = new Student(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));
                            Intent intent = new Intent(getApplicationContext(),StudentActivity.class);
                            intent.putExtra("student", student.getId());
                            startActivity(intent);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            finish();
                            break;
                        case "1" :
                            Teacher teacher = new Teacher(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));
                            intent = new Intent(getApplicationContext(),TeacherActivity.class);
                            intent.putExtra("teacher", teacher.getId());
                            startActivity(intent);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            break;

                        default :
                            Toast.makeText(getApplicationContext(), "Wrong email or password.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });

    }
}
