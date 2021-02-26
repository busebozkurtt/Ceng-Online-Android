package com.example.pl_son;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pl_son.Adapter.MessageListAdapter;
import com.example.pl_son.Classes.MessageInfo;
import com.example.pl_son.Classes.Stack;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;

import java.util.ArrayList;
import java.util.Collections;

public class MessageActivity extends AppCompatActivity {

    public Button logOut;
    public TextView course;
    public Button send_message;
    public ListView messages;
    public Button incoming_message;
    public Button outgoing_message;
    public TextView name;
    public TextView date;
    public TextView userName;
    public TextView message_info;
    public User userr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        logOut=findViewById(R.id.logOut);
        send_message=findViewById(R.id.send_message);
        course=findViewById(R.id.course);
        name=findViewById(R.id.name);
        date=findViewById(R.id.date);
        incoming_message=findViewById(R.id.incoming_button);
        outgoing_message=findViewById(R.id.outgoing_message);
        userName=findViewById(R.id.userName);
        message_info=(TextView)findViewById(R.id.message_info);
        messages=(ListView)findViewById(R.id.messages);

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();

        final int userId = (int) intent.getSerializableExtra("userId");
        String []user=databaseAccess.userLogin(userId);

        if(Integer.parseInt(user[5])==0) {
            userr = new Student(Integer.parseInt(user[0]), user[1], user[2], user[3], user[4], Integer.parseInt(user[5]));
        }
        if(Integer.parseInt(user[5])==1) {
            userr = new Teacher(Integer.parseInt(user[0]), user[1], user[2], user[3], user[4], Integer.parseInt(user[5]));
        }

        Stack messageReceiver= userr.receiverMessage(userId,getApplicationContext());
        Stack messageSender= userr.senderMessage(userId,getApplicationContext());
        int j = messageReceiver.Size();
        ArrayList<MessageInfo> messageReceiverInf= new ArrayList<MessageInfo>();
        ArrayList<MessageInfo> messageSendInf= new ArrayList<MessageInfo>();
        for(int i=0;i<j;i++){
            messageReceiverInf.add((MessageInfo) messageReceiver.pop());
        }
        j= messageSender.Size();
        for(int i=0;i<j;i++){
            messageSendInf.add((MessageInfo) messageSender.pop());
        }

        final MessageListAdapter message_adapter_receive = new MessageListAdapter(this,messageReceiverInf,getApplicationContext(),databaseAccess,userId);
        final MessageListAdapter message_adapter_send= new MessageListAdapter(this,messageSendInf,getApplicationContext(),databaseAccess,userId);
        userName.setText(user[3]+" "+user[4]);
        messages.setAdapter(message_adapter_receive);


        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReadMessageActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("state",0);
                startActivity(intent);
            }
        });
        
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userr.setId(0);
                userr.setEmail("");
                userr.setName("");
                userr.setPassword("");
                userr.setSurname("");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userr.getIs_teacher()==0) {
                    Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                    intent.putExtra("student", userr.getId());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
                    intent.putExtra("teacher", userr.getId());
                    startActivity(intent);
                }
            }
        });

        incoming_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_info.setText("Incoming Messages");
                messages.setAdapter(message_adapter_receive);
            }
        });

        outgoing_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_info.setText("Outgoing Messages");
                name.setText(" Receiver Name");
                messages.setAdapter(message_adapter_send);
            }
        });
    }

}
