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

import com.example.pl_son.AddUpdateTeacherAnnounceActivity;
import com.example.pl_son.Classes.Announce;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;
import com.example.pl_son.DatabaseAccess;
import com.example.pl_son.Announce_Homework_SubmitActivity;
import com.example.pl_son.R;

import java.util.ArrayList;
import java.util.List;

public class Announce_ListAdapter extends BaseAdapter {

    List<Announce> announce= new ArrayList<Announce>();
    LayoutInflater announceInflater;
    Context context;
    DatabaseAccess databaseAccess;
    boolean activity_state;
    User user;
    int course_id;
    public Announce_ListAdapter(Activity activity, ArrayList<Announce> announce,Context context, DatabaseAccess databaseAccess,boolean activity_state,int user_id) {
        this.announce = announce;
        announceInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public Announce_ListAdapter(Activity activity, ArrayList<Announce> announce,Context context, DatabaseAccess databaseAccess,boolean activity_state,int user_id,int course_id) {
        this.announce = announce;
        announceInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.databaseAccess=databaseAccess;
        this.activity_state=activity_state;
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
        return announce.size();
    }

    @Override
    public Object getItem(int position) {
        return announce.get(position);
    }

    @Override
    public long getItemId(int position) {

        return announce.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row_view = null;

        final int course_id= announce.get(position).getCourse_id();
        row_view = announceInflater.inflate(R.layout.list,null);
        Button delete =(Button) row_view.findViewById(R.id.delete);
        TextView announces=(TextView )row_view.findViewById(R.id.studentName);
        TextView date=(TextView )row_view.findViewById(R.id.deliveryDate);
        CheckBox read=(CheckBox)row_view.findViewById(R.id.checkBox);
        ImageButton view=(ImageButton) row_view.findViewById(R.id.viewButton);
        read.setClickable(false);
        announces.setText(announce.get(position).getAnnounce());
        date.setText(announce.get(position).getAnnounce_date());
        if( user.getIs_teacher()==1) {
            read.setVisibility(View.GONE);
        }
        else {
            delete.setVisibility(View.GONE);
            if(announce.get(position).isIs_read()){
                read.setChecked(true);
            }
            else
                read.setChecked(false);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getIs_teacher()==0) {
                    Intent intent = new Intent(context, Announce_Homework_SubmitActivity.class);
                    intent.putExtra("announce_id", announce.get(position).getId());
                    intent.putExtra("id", user.getId());
                    intent.putExtra("state", 1);
                    context.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, AddUpdateTeacherAnnounceActivity.class);
                    intent.putExtra("course_id", course_id);
                    intent.putExtra("id",user.getId());
                    intent.putExtra("state", 1); //update
                    intent.putExtra("announce_id", announce.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Announce> temp= (ArrayList<Announce>) ((ArrayList<Announce>) announce).clone();
                for(Announce c: temp){
                    if(c.getId()==announce.get(position).getId()) {
                        announce.remove(c);
                        break;
                    }
                }
                Teacher teacher = new Teacher(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getIs_teacher());
                teacher.Delete_Announce(context, temp.get(position).getId());
                Announce_ListAdapter.this.notifyDataSetChanged();
            }
        });
        return row_view;
    }
}
