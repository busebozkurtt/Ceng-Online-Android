package com.example.pl_son.Classes;

import android.content.Context;
import android.database.Cursor;

import com.example.pl_son.DatabaseAccess;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Teacher extends User {

    public Teacher(int id, String email, String password, String name, String surname, int is_teacher) {
        super(id, email, password, name, surname, is_teacher);
    }

    @Override
    public Map<String, String> See_Announce(Context context, int announce_id) {
        databaseAccess=DatabaseAccess.getInstance(context);
        Map<String,String> announce_map = new HashMap<String, String>();
        String query = "select * from Announcement Where id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query,new String[] {String.valueOf(announce_id)});
        c.moveToFirst();
        announce_map.put("announce", c.getString(c.getColumnIndex("announce")));
        announce_map.put("date",c.getString(c.getColumnIndex("announce_date")));
        return announce_map;
    }


    @Override
    public Map<String, String> See_Homework(Context context, int homework_id) {
        databaseAccess=DatabaseAccess.getInstance(context);
        Map<String,String> homework_map = new HashMap<String, String>();
        String query = "select * from Assignment Where id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query,new String[] {String.valueOf(homework_id)});
        c.moveToFirst();
        homework_map.put("homework", c.getString(c.getColumnIndex("homework")));
        homework_map.put("date",c.getString(c.getColumnIndex("due_date")));
        return homework_map;
    }

    public ArrayList<Announce> CourseAnnounces (Context context, int course_id){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query="SELECT * FROM Announcement WHERE course_id=?";
        Cursor c=  databaseAccess.getDb().rawQuery(query, new String[]{String.valueOf(course_id)});
        c.moveToFirst();
        ArrayList<Announce> announces=new ArrayList<>();
        for(int i=0; i<c.getCount(); i++){
            Announce announce=new Announce(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("announce")),c.getString(c.getColumnIndex("announce_date")),c.getInt(c.getColumnIndex("teacher_id")));
            announces.add(announce);
            c.moveToNext();
        }
        return announces;
    }

    public Course Add_Course(Context context, String course) {
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("INSERT INTO Course(name) VALUES(\"%s\");",
                course);
        databaseAccess.getDb().execSQL(query);
        query = "select id from Course WHERE name=?";
        Cursor c = databaseAccess.getDb().rawQuery(query, new String[]{course});
        c.moveToFirst();
        int course_id= c.getInt(c.getColumnIndex("id"));
        query= String.format("INSERT INTO User_Course(user_id,course_id) VALUES(%d,%d);",
                getId(),course_id);
        databaseAccess.getDb().execSQL(query);
        Course course_info = new Course(course_id, course);
        return course_info;
    }

    public void Delete_Course(Context context, int course_id){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("DELETE FROM Course WHERE id=%d;",
                course_id);
        databaseAccess.getDb().execSQL(query);
    }

    public void Update_Course(Context context, int course_id,String new_name){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("UPDATE Course SET name=\"%s\" WHERE id=%d;",
                new_name,course_id);
        databaseAccess.getDb().execSQL(query);
    }

    public int Add_Student_to_Course(Context context,int course_id,String email){
        databaseAccess= DatabaseAccess.getInstance(context);
        Cursor c =databaseAccess.control_email(email);
        if (c.getCount()==0)
            return 0;
        else{
            c.moveToFirst();
            int user_id= c.getInt(c.getColumnIndex("id"));
            String query = "select * from User_Course WHERE user_id=? and course_id=?";
            c=  databaseAccess.getDb().rawQuery(query, new String[]{String.valueOf(c.getInt(c.getColumnIndex("id"))),String.valueOf(course_id)});
            if(c.getCount()!=0)
                return 1;
            query= String.format("INSERT INTO User_Course(user_id,course_id) VALUES(%d,%d);",
                    user_id,course_id);
            databaseAccess.getDb().execSQL(query);
            return 2;
        }
    }


    public void Update_Announce(Context context, int announce_id,String new_announce){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("UPDATE Announcement SET announce=\"%s\" WHERE id=%d;",
                new_announce,announce_id);
        databaseAccess.getDb().execSQL(query);
        String query1=String.format("DELETE FROM User_Announcement WHERE announce_id=%d;",
                announce_id);
        databaseAccess.getDb().execSQL(query1);
    }

    public void Delete_Announce(Context context, int announce_id){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("DELETE FROM Announcement WHERE id=%d;",
                announce_id);
        databaseAccess.getDb().execSQL(query);
    }

    public void Add_Announce (Context context,int course_id, int teacher_id, String announce){
        databaseAccess= DatabaseAccess.getInstance(context);
        Calendar calendar= Calendar.getInstance();
        String query= String.format("INSERT INTO Announcement(course_id,announce,announce_date,teacher_id) VALUES(%d,\"%s\",\"%s\",%d);",
                course_id,announce,databaseAccess.EditDate(calendar),teacher_id);
        databaseAccess.getDb().execSQL(query);
    }

    public void Add_Homework (Context context,int course_id, int teacher_id, String homework,String Due_date){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("INSERT INTO Assignment(course_id,homework,due_date,teacher_id) VALUES(%d,\"%s\",\"%s\",%d);",
                course_id,homework,Due_date,teacher_id);
        databaseAccess.getDb().execSQL(query);
    }

    public void Update_Homework(Context context, int homework_id,String homework,String Due_date){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("UPDATE Assignment SET homework=\"%s\"  WHERE id=%d;", homework,homework_id);
        databaseAccess.getDb().execSQL(query);
        query= String.format("UPDATE Assignment SET due_date=\"%s\"  WHERE id=%d;", Due_date,homework_id);
        databaseAccess.getDb().execSQL(query);
    }

    public void Delete_Homework(Context context, int homework_id){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("DELETE FROM Assignment WHERE id=%d;",
                homework_id);
        databaseAccess.getDb().execSQL(query);
    }

    public void Add_Post (Context context,int course_id, int teacher_id, String post,String date){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("INSERT INTO Posts(course_id,post,post_date,teacher_id) VALUES(%d,\"%s\",\"%s\",%d);",
                course_id,post,date,teacher_id);
        databaseAccess.getDb().execSQL(query);
    }

    public void Update_Post(Context context, int post_id,String post){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("UPDATE Posts SET post=\"%s\" WHERE id=%d;",
                post,post_id);
        databaseAccess.getDb().execSQL(query);
    }
    public void Delete_Post(Context context, int post_id){
        databaseAccess= DatabaseAccess.getInstance(context);
        String query= String.format("DELETE FROM Posts WHERE id=%d;",
                post_id);
        databaseAccess.getDb().execSQL(query);
    }
}


