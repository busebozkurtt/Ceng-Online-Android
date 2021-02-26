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

import com.example.pl_son.Classes.Announce;
import com.example.pl_son.Classes.Assignment;
import com.example.pl_son.Classes.Message;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;
import com.example.pl_son.DatabaseAccess;
import com.example.pl_son.HomeworkDetailsActivity;
import com.example.pl_son.Announce_Homework_SubmitActivity;
import com.example.pl_son.R;

import java.util.ArrayList;
import java.util.List;

public class Assignment_ListAdapter extends BaseAdapter {

    List<Assignment> assignment= new ArrayList<Assignment>();
    LayoutInflater assignmentInflater;
    Context context;
    DatabaseAccess databaseAccess;
    boolean activity_state;
    User user;
    int homeworkDone;
    public Assignment_ListAdapter(Activity activity, ArrayList<Assignment> assignment,Context context,DatabaseAccess databaseAccess,boolean activity_state,int user_id) {
        this.assignment = assignment;
        assignmentInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.databaseAccess=databaseAccess;
        this.activity_state=activity_state;
        String []userr=databaseAccess.userLogin(user_id);
        if(Integer.parseInt(userr[5])==0) {
            user = new Student(Integer.parseInt(userr[0]), userr[1], userr[2], userr[3], userr[4], Integer.parseInt(userr[5]));
        }
        else
            user = new Teacher(Integer.parseInt(userr[0]), userr[1], userr[2], userr[3], userr[4], Integer.parseInt(userr[5]));
    }

    @Override
    public int getCount() {
        return assignment.size();
    }

    public Object getItem(int position) {
        return assignment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return assignment.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row_view;
        final int course_id= assignment.get(position).getCourse_id();
        row_view = assignmentInflater.inflate(R.layout.list,null);
        Button delete =(Button) row_view.findViewById(R.id.delete);
        TextView announces=(TextView )row_view.findViewById(R.id.studentName);
        TextView date=(TextView )row_view.findViewById(R.id.deliveryDate);
        CheckBox read=(CheckBox)row_view.findViewById(R.id.checkBox);
        ImageButton view=(ImageButton) row_view.findViewById(R.id.viewButton);
        read.setClickable(false);
        announces.setText(assignment.get(position).getHomework());
        date.setText(assignment.get(position).getDue_date());
        if( user.getIs_teacher()==1) {
            read.setVisibility(View.GONE);
        }
        else {
            delete.setVisibility(View.GONE);
            if(assignment.get(position).isIs_done()){
                read.setChecked(true);
            }
            else
                read.setChecked(false);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!assignment.get(position).isIs_done() && !activity_state && user.getIs_teacher()==0){
                    Intent intent = new Intent(context, Announce_Homework_SubmitActivity.class);
                    intent.putExtra("homeworkid",assignment.get(position).getId());
                    intent.putExtra("id", user.getId());
                    intent.putExtra("state", 0);
                    intent.putExtra("homework_done", 0);
                    context.startActivity(intent);
                }
                else if(assignment.get(position).isIs_done() && !activity_state && user.getIs_teacher()==0){
                    Intent intent = new Intent(context, Announce_Homework_SubmitActivity.class);
                    intent.putExtra("homeworkid",assignment.get(position).getId());
                    intent.putExtra("id", user.getId());
                    intent.putExtra("state", 0);
                    intent.putExtra("homework_done", 1);
                    context.startActivity(intent);
                }
                else if(user.getIs_teacher()==1){
                    Intent intent = new Intent(context, HomeworkDetailsActivity.class);
                    intent.putExtra("course_id", course_id);
                    intent.putExtra("homework_id",assignment.get(position).getId());
                    intent.putExtra("id", user.getId());
                    context.startActivity(intent);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Assignment> temp= (ArrayList<Assignment>) ((ArrayList<Assignment>) assignment).clone();
                for(Assignment c: temp){
                    if(c.getId()==assignment.get(position).getId()) {
                        assignment.remove(c);
                        break;
                    }
                }
                Teacher teacher = new Teacher(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getIs_teacher());
                teacher.Delete_Homework(context, temp.get(position).getId());
                Assignment_ListAdapter.this.notifyDataSetChanged();
            }
        });
        return row_view;
    }
}
