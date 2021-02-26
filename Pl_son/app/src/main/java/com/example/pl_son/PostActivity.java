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

import com.example.pl_son.Adapter.Comment_ListAdapter;
import com.example.pl_son.Classes.Post;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    public EditText post,make_comment;
    public TextView date;
    public Button add_update,back;
    public ImageButton send;
    public Button logOut;
    public Button message;
    public Button course;
    public ListView comments;
    public TextView message_number,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        final Calendar datetime = Calendar.getInstance();
        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        Intent intent = getIntent();
        Calendar calendar= Calendar.getInstance();
        final int  userId = intent.getIntExtra("id", -1);
        final int  post_id = intent.getIntExtra("post_id", -1);
        final int  courseid = intent.getIntExtra("course_id", -1);
        final int  state = intent.getIntExtra("state", -1);
        final User user;
        final String []user_info=databaseAccess.userLogin(userId);
        if(Integer.parseInt(user_info[5]) == 1){
            user= new Teacher(Integer.parseInt(user_info[0]),user_info[1],user_info[2],user_info[3],user_info[4],Integer.parseInt(user_info[5]));
        }
        else {
            user = new Student(Integer.parseInt(user_info[0]),user_info[1],user_info[2],user_info[3],user_info[4],Integer.parseInt(user_info[5]));
        }


        message_number=findViewById(R.id.message_number);
        userName=findViewById(R.id.userName);
        post=findViewById(R.id.post);
        date=findViewById(R.id.date);
        add_update=findViewById(R.id.add_update);
        back=findViewById(R.id.back);
        send=findViewById(R.id.send);
        logOut=findViewById(R.id.logOut);
        message=findViewById(R.id.messages);
        course=findViewById(R.id.course);
        make_comment=findViewById(R.id.make_comment);
        comments=(ListView)findViewById(R.id.comments);

        final ArrayList<Map<String, String>> list=databaseAccess.CommentsList(post_id);
        final Comment_ListAdapter commentlist_adapter = new Comment_ListAdapter(this,list,getApplicationContext(),databaseAccess);
        comments.setAdapter(commentlist_adapter);

        if(list.size()!=0)
            comments.setSelection(list.size()-1);

        userName.setText(user.getName()+ " " + user.getSurname());
        int message_num= databaseAccess.userButtons(userId);
        message_number.setText(" (" +message_num+")");

        if(state==0){
            add_update.setText("Add");
            comments.setVisibility(View.GONE);
            make_comment.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            date.setText(user.getName()+" "+user.getSurname() + " Date: " + databaseAccess.EditDate(calendar));
        }
        else{
            Map<String,String> post_map= user.See_Post(getApplicationContext(), post_id);
            String []teacherInf=databaseAccess.userLogin(Integer.parseInt(post_map.get("teacher_id")));
            date.setText(teacherInf[3]+" "+teacherInf[4] + " Date: " + post_map.get("date"));
            post.setText(post_map.get("post"));
            comments.setVisibility(View.VISIBLE);
            make_comment.setVisibility(View.VISIBLE);
            send.setVisibility(View.VISIBLE);
        }

        if(Integer.parseInt(user_info[5]) == 0){
            add_update.setVisibility(View.GONE);
            post.setEnabled(false);
            post.setText(databaseAccess.FindPost(post_id));
        }


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

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userId",user.getId());
                startActivity(intent);

            }
        });

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(user_info[5]) == 1) {
                    Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
                    intent.putExtra("teacher", user.getId());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                    intent.putExtra("student", user.getId());
                    startActivity(intent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(user_info[5]) == 1) {
                    Intent intent = new Intent(getApplicationContext(), CourseInfoTeacherActivity.class);
                    intent.putExtra("id", userId);
                    intent.putExtra("course_id", courseid);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), CourseStudentActivity.class);
                    intent.putExtra("id", userId);
                    intent.putExtra("course_id", courseid);
                    startActivity(intent);
                }
            }
        });

        add_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher teacher= new Teacher(Integer.parseInt(user_info[0]),user_info[1],user_info[2],user_info[3],user_info[4],Integer.parseInt(user_info[5]));
                if(state==0 && add_update.getText().toString().equalsIgnoreCase("Add")){
                    teacher.Add_Post(getApplicationContext(), courseid,  userId, post.getText().toString(), databaseAccess.EditDate(datetime));
                    Toast.makeText(getApplicationContext(),"The post is currently being added...",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),CourseInfoTeacherActivity.class);
                    intent.putExtra("id",  userId);
                    intent.putExtra("course_id", courseid);
                    startActivity(intent);
                }
                else {
                    teacher.Update_Post(getApplicationContext(), post_id, post.getText().toString());
                    Toast.makeText(getApplicationContext(),"The post is currently being updated...",Toast.LENGTH_LONG).show();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=make_comment.getText().toString();
                if(comment.equalsIgnoreCase(" "))
                    Toast.makeText(getApplicationContext(), "Please enter a comment", Toast.LENGTH_LONG).show();
                else {
                    Calendar c= Calendar.getInstance();
                    user.Add_Comment(getApplicationContext(),post_id,comment,userId);
                    Toast.makeText(getApplicationContext(), "Your comment has been sent", Toast.LENGTH_LONG).show();
                    Map<String, String> commentList_map = new HashMap<>();
                    commentList_map.put("userId",String.valueOf(user.getId()));
                    commentList_map.put("comment_date",databaseAccess.EditDate(c));
                    commentList_map.put("comment",make_comment.getText().toString());
                    list.add(commentList_map);
                    commentlist_adapter.notifyDataSetChanged();
                }
            }
        });

    }
}
