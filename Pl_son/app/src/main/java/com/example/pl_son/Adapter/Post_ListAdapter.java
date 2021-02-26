package com.example.pl_son.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pl_son.Classes.Post;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;
import com.example.pl_son.DatabaseAccess;
import com.example.pl_son.R;
import com.example.pl_son.PostActivity;

import java.util.ArrayList;
import java.util.List;

public class Post_ListAdapter extends BaseAdapter {

    List<Post> posts= new ArrayList<Post>();
    LayoutInflater postInflater;
    Context context;
    DatabaseAccess databaseAccess;
    User user;
    int course_id;

    public Post_ListAdapter(Activity activity, ArrayList<Post> posts,Context context, DatabaseAccess databaseAccess,int user_id,int course_id) {
        this.posts = posts;
        postInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.databaseAccess=databaseAccess;
        this.course_id=course_id;
        String []userr=databaseAccess.userLogin(user_id);
        if(Integer.parseInt(userr[5])==0) {
            user = new Student(Integer.parseInt(userr[0]), userr[1], userr[2], userr[3], userr[4], Integer.parseInt(userr[5]));
        }
        else
            user = new Teacher(Integer.parseInt(userr[0]), userr[1], userr[2], userr[3], userr[4], Integer.parseInt(userr[5]));
    }
    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {

        return posts.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row_view;
        final int course_id= posts.get(position).getCourse_id();
        row_view = postInflater.inflate(R.layout.list,null);
        Button delete =(Button) row_view.findViewById(R.id.delete);
        TextView announces=(TextView )row_view.findViewById(R.id.studentName);
        TextView date=(TextView )row_view.findViewById(R.id.deliveryDate);
        CheckBox read=(CheckBox)row_view.findViewById(R.id.checkBox);
        ImageButton view=(ImageButton) row_view.findViewById(R.id.viewButton);
        read.setClickable(false);
        announces.setText(posts.get(position).getPost());
        date.setText(posts.get(position).getpost_date());
        if( user.getIs_teacher()==1) {
            read.setVisibility(View.GONE);
        }
        else {
            read.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("post_id", posts.get(position).getId());
                    intent.putExtra("course_id", course_id);
                    intent.putExtra("id", user.getId());
                    context.startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Post> temp= (ArrayList<Post>) ((ArrayList<Post>) posts).clone();
                for(Post c: temp){
                    if(c.getId()==posts.get(position).getId()) {
                        posts.remove(c);
                        break;
                    }
                }
                Teacher teacher = new Teacher(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getIs_teacher());
                teacher.Delete_Post(context, temp.get(position).getId());
                Post_ListAdapter.this.notifyDataSetChanged();
            }
        });
        return row_view;
    }
}
