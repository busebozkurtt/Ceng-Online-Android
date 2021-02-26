package com.example.pl_son;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pl_son.Classes.Announce;
import com.example.pl_son.Classes.Assignment;
import com.example.pl_son.Classes.Course;
import com.example.pl_son.Classes.Message;
import com.example.pl_son.Classes.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public String[] userLogin(int userid) {
        open();
        String query = "select * from User WHERE id=? ";
        c = db.rawQuery(query, new String[]{String.valueOf(userid)});
        String [] user= new String[6];
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        user[0]= Integer.toString(id);
        user[1]=c.getString(c.getColumnIndex("email"));;
        user[2]=c.getString(c.getColumnIndex("password"));;
        user[3] = c.getString(c.getColumnIndex("name"));
        user[4] = c.getString(c.getColumnIndex("surname"));
        int is_teacher = c.getInt(c.getColumnIndex("is_teacher"));
        user[5]= Integer.toString(is_teacher);
        return user;
    }

    public String[] userLogin(String email, String password) {
        open();
        String query = "select * from User WHERE email=? and password=?";
        c = db.rawQuery(query, new String[]{email, password});
        String [] user= new String[6];
        if (c.getCount() == 0) {
            user[5]="-1";
            return user;
        }
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("id"));
        user[0]= Integer.toString(id);
        user[1]=email;
        user[2]=password;
        user[3] = c.getString(c.getColumnIndex("name"));
        user[4] = c.getString(c.getColumnIndex("surname"));
        int is_teacher = c.getInt(c.getColumnIndex("is_teacher"));
        user[5]= Integer.toString(is_teacher);
        return user;
    }

    public Cursor control_email(String email){
        String query = "select * from User WHERE email=?";
        c=  db.rawQuery(query, new String[]{email});
        return c;
    }

    public ArrayList<Course> userCourse(int id) {
        open();
        String query = "select * from Course WHERE id IN(select course_id from User_Course WHERE user_id=?)";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        ArrayList<Course> Courses = new ArrayList<Course>();
        c.moveToFirst();
        if (c.getCount() == 0) {
            return Courses;
        }
        for (int i = 0; i < c.getCount(); i++) {
            Course course = new Course(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")));
            Courses.add(course);
            c.moveToNext();
        }
        return Courses;
    }

    public String Course_name(int courseid) {
        open();
        String query = "select * from Course WHERE id=?";
        c = db.rawQuery(query, new String[]{String.valueOf(courseid)});
        c.moveToFirst();
        Course course = new Course(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")));
        return course.getCourse_Name();
    }
    public String FindPost(int post_id){
        open();
        String query = "select * from Posts WHERE id=?";
        c = db.rawQuery(query, new String[]{String.valueOf(post_id)});
        c.moveToFirst();
       Post post = new Post(c.getInt(c.getColumnIndex("id")),c.getInt(c.getColumnIndex("course_id")), c.getString(c.getColumnIndex("post")), c.getString(c.getColumnIndex("post_date")),c.getColumnIndex("teacher_id"));
       return post.getPost();
    }
    public String FindHomework(int student_id,int homework_id){
        open();
        String query = "select * from User_Assignment WHERE assignment_id=? and user_id=?";
        c = db.rawQuery(query, new String[]{String.valueOf(homework_id),String.valueOf(student_id)});
        c.moveToFirst();

        return c.getString(c.getColumnIndex("homework"));
    }

    public Course return_course(int courseid) {
        open();
        String query = "select * from Course WHERE id=?";
        c = db.rawQuery(query, new String[]{String.valueOf(courseid)});
        c.moveToFirst();
        Course course = new Course(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")));
        return course;
    }

    public ArrayList<Message> userMessages(int id) {
        open();
        String query = "select * from Message WHERE receiver_id=?";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        ArrayList<Message> messages = new ArrayList<Message>();
        c.moveToFirst();
        if (c.getCount() == 0) {
            return messages;
        }
        for (int i = 0; i < c.getCount(); i++) {
            Message m = new Message(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("is_read")), c.getInt(c.getColumnIndex("receiver_id")), c.getInt(c.getColumnIndex("sender_id")), c.getString(c.getColumnIndex("message")), c.getString(c.getColumnIndex("delivery_date")));
            messages.add(m);
            c.moveToNext();
        }
        return messages;
    }

    public int userButtons(int id){
        open();
        int messages=0;
        c.moveToFirst();

        String query = "select * from Message Where receiver_id=? and is_read=0";
        c=db.rawQuery(query,new String[] {String.valueOf(id)});
        messages= c.getCount();
        return messages;
    }

    public ArrayList<Assignment> userHomework(int id) {
        open();
        String query = "select * from Course WHERE id IN(select course_id from User_Course WHERE user_id=?)";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        ArrayList<Course> Courses = new ArrayList<Course>();
        ArrayList<Assignment> assignment = new ArrayList<Assignment>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Course course = new Course(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")));
            Courses.add(course);
            c.moveToNext();
        }

        for (Course course : Courses) {
            query = "select * from Assignment where course_id=? and id NOT IN (select assignment_id from User_Assignment where user_id=?)";
            c = db.rawQuery(query, new String[]{String.valueOf(course.getId()),String.valueOf(id)});
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                Assignment homework = new Assignment(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("teacher_id")),c.getString(c.getColumnIndex("due_date")),c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("homework")),false);
                assignment.add(homework);
                c.moveToNext();
            }
            query = "select * from Assignment where course_id=? and id IN (select assignment_id from User_Assignment where user_id=?)";
            c = db.rawQuery(query, new String[]{String.valueOf(course.getId()),String.valueOf(id)});
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                Assignment homework = new Assignment(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("teacher_id")),c.getString(c.getColumnIndex("due_date")),c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("homework")),true);
                assignment.add(homework);
                c.moveToNext();
            }
        }

        return  assignment;
    }

    public ArrayList<Assignment> userHomework(int id,int course_id) {
        open();
        ArrayList<Assignment> assignment = new ArrayList<Assignment>();

        String query = "select * from Assignment Where course_id=? and id NOT IN(select assignment_id from User_Assignment where user_id=?)";
        c = db.rawQuery(query, new String[]{String.valueOf(course_id),String.valueOf(id)});
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) { Assignment homework = new Assignment(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("teacher_id")),c.getString(c.getColumnIndex("due_date")),c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("homework")),false);
            assignment.add(homework);
            c.moveToNext();
        }
        query = "select * from Assignment Where course_id=? and id IN(select assignment_id from User_Assignment where user_id=?)";
        c = db.rawQuery(query, new String[]{String.valueOf(course_id),String.valueOf(id)});

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) { Assignment homework = new Assignment(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("teacher_id")),c.getString(c.getColumnIndex("due_date")),c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("homework")),true);
            assignment.add(homework);
            c.moveToNext();
        }
        return  assignment;
    }

    public ArrayList<Announce> userAnnounce(int id) {
        open();
        String query = "select * from Course WHERE id IN(select course_id from User_Course WHERE user_id=?)";
        c = db.rawQuery(query, new String[]{String.valueOf(id)});
        ArrayList<Course> Courses = new ArrayList<Course>();
        ArrayList<Announce> announces = new ArrayList<Announce>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Course course = new Course(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")));
            Courses.add(course);
            c.moveToNext();
        }

        for (Course course : Courses) {
            query = "select * from Announcement where course_id=? and id NOT IN (select announce_id from User_Announcement where user_id=?)";
            c = db.rawQuery(query, new String[]{String.valueOf(course.getId()),String.valueOf(id)});
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                Announce announce = new Announce(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("announce")),c.getString(c.getColumnIndex("announce_date")),c.getInt(c.getColumnIndex("teacher_id")),false);
                announces.add(announce);
                c.moveToNext();
            }
            query = "select * from Announcement where course_id=? and id IN (select announce_id from User_Announcement where user_id=?)";
            c = db.rawQuery(query, new String[]{String.valueOf(course.getId()),String.valueOf(id)});
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                Announce announce = new Announce(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("announce")),c.getString(c.getColumnIndex("announce_date")),c.getInt(c.getColumnIndex("teacher_id")),true);
                announces.add(announce);
                c.moveToNext();
            }
        }
        return  announces;
    }

    public ArrayList<Announce> userAnnounce(int id,int course_id) {
        open();
        ArrayList<Announce> announces = new ArrayList<Announce>();

        String query = "select * from Announcement Where course_id=? and id NOT IN(select announce_id from User_Announcement where user_id=?)";
        c = db.rawQuery(query, new String[]{String.valueOf(course_id),String.valueOf(id)});
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            Announce announce = new Announce(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("announce")),c.getString(c.getColumnIndex("announce_date")),c.getInt(c.getColumnIndex("teacher_id")),false);
            announces.add(announce);
            c.moveToNext();
        }
        query = "select * from Announcement Where course_id=? and id IN(select announce_id from User_Announcement where user_id=?)";
        c = db.rawQuery(query, new String[]{String.valueOf(course_id),String.valueOf(id)});
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Announce announce = new Announce(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("announce")),c.getString(c.getColumnIndex("announce_date")),c.getInt(c.getColumnIndex("teacher_id")),true);
            announces.add(announce);
            c.moveToNext();
        }
        return  announces;
    }

     public ArrayList<Map<String, String>>  UserAssignment (int assignment_id){
         ArrayList<Map<String, String>> list = new ArrayList<>();
         String query= "select * from User_Assignment where assignment_id=?";
         c=db.rawQuery(query,new String[] {String.valueOf(assignment_id)});
         c.moveToFirst();
         for (int i = 0; i < c.getCount(); i++) {
             Map<String, String> userdelivering_map = new HashMap<>();
             userdelivering_map.put("userId",String.valueOf(c.getInt(c.getColumnIndex("user_id"))));
             userdelivering_map.put("deliveryDate",c.getString(c.getColumnIndex("delivery_date")));
             userdelivering_map.put("homework",c.getString(c.getColumnIndex("homework")));
             list.add(userdelivering_map);
             c.moveToNext();
         }
         return list;
     }

    public ArrayList<Post> Course_Posts (Context context, int course_id){
        String query="SELECT * FROM Posts WHERE course_id=?";
        c= db.rawQuery(query, new String[]{String.valueOf(course_id)});
        c.moveToFirst();
        ArrayList<Post> posts=new ArrayList<>();
        for(int i=0; i<c.getCount(); i++){
            Post post=new Post(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("course_id")),c.getString(c.getColumnIndex("post")),c.getString(c.getColumnIndex("post_date")),c.getInt(c.getColumnIndex("teacher_id")));
            posts.add(post);
            c.moveToNext();
        }
        return posts;
    }

    public ArrayList<Map<String, String>>  CommentsList (int post_id){
        ArrayList<Map<String, String>> list = new ArrayList<>();
        String query= "select * from Comments where post_id=?";
        c=db.rawQuery(query,new String[] {String.valueOf(post_id)});
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Map<String, String> commentList_map = new HashMap<>();
            commentList_map.put("userId",String.valueOf(c.getInt(c.getColumnIndex("user_id"))));
            commentList_map.put("comment_date",c.getString(c.getColumnIndex("comment_date")));
            commentList_map.put("comment",c.getString(c.getColumnIndex("comment")));
            list.add(commentList_map);
            c.moveToNext();
        }
        return list;
    }


     public String EditDate(Calendar calendar){
         String monthS,dayS,hourS,minuteS;
         int year=calendar.get(Calendar.YEAR);
         int month=calendar.get(Calendar.MONTH)+1;
         int day=calendar.get(Calendar.DAY_OF_MONTH);
         int minute=calendar.get(Calendar.MINUTE);
         int hour=calendar.get(Calendar.HOUR_OF_DAY);
         if(month<10)
             monthS="0"+String.valueOf(month);
         else
             monthS=String.valueOf(month);
         if(day<10)
             dayS="0"+String.valueOf(day);
         else
             dayS=String.valueOf(day);
         if(hour<10)
             hourS="0"+String.valueOf(hour);
         else
             hourS=String.valueOf(hour);
         if(minute<10)
             minuteS="0"+String.valueOf(minute);
         else
             minuteS=String.valueOf(minute);
         String date=dayS+"."+monthS+"."+String.valueOf(year)+" "+hourS+":"+minuteS;
         return date;
     }
}
