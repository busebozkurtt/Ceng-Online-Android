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

import com.example.pl_son.Classes.Message;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;

public class ReadMessageActivity extends AppCompatActivity {

    public TextView fromMail;
    public EditText message;
    public EditText answer;
    public Button buttonAnswer;
    public Button logOut;
    public Button back;
    public Button message_button;
    public Button course;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();


        logOut=findViewById(R.id.logOut);
        course=findViewById(R.id.course);
        fromMail=findViewById(R.id.fromMail);
        message=findViewById(R.id.message);
        message_button= findViewById(R.id.message_button);
        buttonAnswer=findViewById(R.id.buttonAnswer);
        answer=(EditText)findViewById(R.id.answer);
        back=findViewById(R.id.back);
        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);

        final User user;
        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        final int userId= (int) intent.getSerializableExtra("userId");
        final int state= (int) intent.getSerializableExtra("state");

        String []user_info=databaseAccess.userLogin(userId);
        if(Integer.parseInt(user_info[5]) == 1){
            user= new Teacher(Integer.parseInt(user_info[0]),user_info[1],user_info[2],user_info[3],user_info[4],Integer.parseInt(user_info[5]));
        }
        else {
            user = new Student(Integer.parseInt(user_info[0]),user_info[1],user_info[2],user_info[3],user_info[4],Integer.parseInt(user_info[5]));
        }



        switch (state){
            case 0: //send
                fromMail.setHint("To: ");
                message.setHint("Message: ");
                answer.setVisibility(View.GONE);
                buttonAnswer.setText("Send");
                break;
            case 1: //answer
                int messageId= (int) intent.getSerializableExtra("messageId");
                Message messagee=user.viewMessage(messageId,getApplicationContext(),1);
                fromMail.setText(user.findUserMail(messagee.getSender_id(),getApplicationContext()));
                message.setText(messagee.getMessage());
                message.setEnabled(false);
                fromMail.setEnabled(false);
                break;
            case 2:// see your message
                int messageId1= (int) intent.getSerializableExtra("messageId");
                messagee=user.viewMessage(messageId1,getApplicationContext(),2);
                fromMail.setText("To: "+ user.findUserMail(messagee.getReceiver_id(),getApplicationContext()));
                message.setText(messagee.getMessage());
                fromMail.setEnabled(false);
                message.setEnabled(false);
                buttonAnswer.setVisibility(View.GONE);
                answer.setVisibility(View.GONE);
                break;
        }

        userName.setText(user.getName()+ " " + user.getSurname());
        int message_num= databaseAccess.userButtons(userId);
        message_number.setText(" (" +(message_num)+")");

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setId(0);
                user.setEmail("");
                user.setName("");
                user.setPassword("");
                user.setSurname("");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getIs_teacher()==0) {
                    Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                    intent.putExtra("student", user.getId());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
                    intent.putExtra("teacher", user.getId());
                    startActivity(intent);
                }
            }
        });

        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagee;
                if(state==1)
                    messagee = answer.getText().toString();
                else
                    messagee= message.getText().toString();
                if (!messagee.equalsIgnoreCase("")) {
                    final String receiver = fromMail.getText().toString();
                    final int count = user.sendMessage(receiver, userId, messagee, getApplicationContext());
                    if (receiver.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please fill in the receiver email", Toast.LENGTH_LONG).show();
                    } else if (count == 0) {
                        Toast.makeText(getApplicationContext(), "Wrong email address", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Message sent.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter your message.", Toast.LENGTH_LONG).show();
                }
            }
        });

        message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userId",user.getId());
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userId",user.getId());
                startActivity(intent);
            }
        });

    }
}
