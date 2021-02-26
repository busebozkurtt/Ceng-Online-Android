package com.example.pl_son.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pl_son.Classes.Announce;
import com.example.pl_son.Classes.Student;
import com.example.pl_son.Classes.Teacher;
import com.example.pl_son.Classes.User;
import com.example.pl_son.DatabaseAccess;
import com.example.pl_son.R;
import com.example.pl_son.SeeHomeworkActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentDeliverers_ListAdapter extends BaseAdapter {

    ArrayList<Map<String, String>> list = new ArrayList<>();
    LayoutInflater announceInflater;
    Context context;
    DatabaseAccess databaseAccess;
    boolean activity_state;
    User user;
    int course_id;
    int homework_id;
    public StudentDeliverers_ListAdapter(Activity activity, ArrayList<Map<String, String>> list, Context context, DatabaseAccess databaseAccess, int user_id,int course_id,int homework_id) {
        this.list = list;
        announceInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.databaseAccess=databaseAccess;
        this.course_id=course_id;
        this.homework_id=homework_id;
        String []userr=databaseAccess.userLogin(user_id);
        user = new Teacher(Integer.parseInt(userr[0]), userr[1], userr[2], userr[3], userr[4], Integer.parseInt(userr[5]));
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row_view;
        row_view = announceInflater.inflate(R.layout.homework_deliverers,null);
        TextView student_name =(TextView) row_view.findViewById(R.id.studentName);
        TextView delivery_date =(TextView) row_view.findViewById(R.id.deliveryDate);
        ImageButton see_homework=(ImageButton)row_view.findViewById(R.id.see_homework);
        String[] student=databaseAccess.userLogin(Integer.parseInt( list.get(position).get("userId")));
        final String nameSurname=student[3]+" "+student[4];
        student_name.setText(nameSurname);
        delivery_date.setText(list.get(position).get("deliveryDate"));

        see_homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeeHomeworkActivity.class);
                intent.putExtra("course_id", course_id);
                intent.putExtra("homework", list.get(position).get("homework"));
                intent.putExtra("id", user.getId());
                intent.putExtra("name",nameSurname);
                intent.putExtra("delivery_date",list.get(position).get("deliveryDate"));
                intent.putExtra("student_id",Integer.parseInt( list.get(position).get("userId")));
                intent.putExtra("homework_id",homework_id);
                context.startActivity(intent);
            }
        });
        return row_view;
    }
}
