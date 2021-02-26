package com.example.pl_son.Classes;

import android.content.Context;
import android.database.Cursor;

import com.example.pl_son.DatabaseAccess;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Student extends User implements Serializable {

    public Student(int id, String email, String password, String name, String surname, int is_teacher) {
        super(id, email, password, name, surname, is_teacher);
    }

    @Override
    public Map<String,String> See_Announce(Context context,int announce_id) {
        databaseAccess=DatabaseAccess.getInstance(context);
        Map<String,String> announce_map = new HashMap<String, String>();
        String query = "select * from Announcement Where id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query,new String[] {String.valueOf(announce_id)});
        c.moveToFirst();
        Announce announcement = new Announce(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("announce")),c.getString(c.getColumnIndex("announce_date")),c.getInt(c.getColumnIndex("teacher_id")),true);
        int teacher_id= c.getInt(c.getColumnIndex("teacher_id"));
        String []user=databaseAccess.userLogin(teacher_id);
        Teacher teacher = new Teacher(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));
        announce_map.put("announce", c.getString(c.getColumnIndex("announce")));
        announce_map.put("date",c.getString(c.getColumnIndex("announce_date")));
        announce_map.put("teacher_name",teacher.getName()+ " " + teacher.getSurname());
        announce_map.put("course_id",String.valueOf(c.getInt(c.getColumnIndex("course_id"))));
        String table_name="User_Announcement";
        query = "INSERT INTO " +table_name+" (user_id,announce_id) VALUES(" + getId() + ", " + announcement.getId() +")";
        databaseAccess.getDb().execSQL(query);
        return  announce_map;
    }

    @Override
    public Map<String,String> See_Homework(Context context,int homework_id) {
        databaseAccess=DatabaseAccess.getInstance(context);
        Map<String,String> homework_map = new HashMap<String, String>();
        String query = "select * from Assignment Where id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query,new String[] {String.valueOf(homework_id)});
        c.moveToFirst();
        Assignment assignment = new Assignment(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("teacher_id")),c.getString(c.getColumnIndex("due_date")),c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("homework")),false);
        int teacher_id= c.getInt(c.getColumnIndex("teacher_id"));
        String []user=databaseAccess.userLogin(teacher_id);
        Teacher teacher = new Teacher(Integer.parseInt(user[0]),user[1],user[2],user[3],user[4],Integer.parseInt(user[5]));
        homework_map.put("homework", c.getString(c.getColumnIndex("homework")));
        homework_map.put("date",c.getString(c.getColumnIndex("due_date")));
        homework_map.put("teacher_name",teacher.getName()+ " " + teacher.getSurname());
        homework_map.put("course_id",String.valueOf(c.getInt(c.getColumnIndex("course_id"))));
        return  homework_map;
    }

    public void Submit_Homework(Context context, int homework_id, String homework, int user_id) {
        databaseAccess=DatabaseAccess.getInstance(context);
        Calendar calendar=Calendar.getInstance();
        String date=databaseAccess.EditDate(calendar);
        String query= String.format("INSERT INTO User_Assignment(assignment_id, homework,delivery_date,user_id) VALUES(%d, \"%s\", \"%s\", %d);",
                homework_id,
                homework,
                date,
                user_id);
        databaseAccess.getDb().execSQL(query);
    }
}
