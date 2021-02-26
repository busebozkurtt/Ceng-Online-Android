package com.example.pl_son.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.pl_son.DatabaseAccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public abstract class User implements Serializable {

    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private int is_teacher;
    DatabaseAccess databaseAccess;
    public User(int id, String email, String password, String name, String surname, int is_teacher) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.is_teacher = is_teacher;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}
    public void setSurname(String surname) {this.surname = surname;}

    public int getIs_teacher() {return is_teacher;}
    public void setIs_teacher(int is_teacher) {this.is_teacher = is_teacher;}

    public abstract Map<String,String> See_Announce(Context context, int announce_id);
    public abstract Map<String,String> See_Homework(Context context,int homework_id);

    public Stack receiverMessage(int id,Context context){
        databaseAccess=DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String query = "Select Message.id,name,surname,delivery_date,is_read from User INNER JOIN Message ON User.id=sender_id WHERE receiver_id=?";
        Cursor c = databaseAccess.getDb().rawQuery(query, new String[]{String.valueOf(id)});
        c.moveToFirst();
        Stack senderPersons = new Stack(c.getCount());
        for(int i=0;i<c.getCount(); i++){
            MessageInfo sender=new MessageInfo(c.getString(c.getColumnIndex("name")),c.getString(c.getColumnIndex("surname")),c.getString(c.getColumnIndex("delivery_date")),c.getInt(c.getColumnIndex("id")),c.getInt(c.getColumnIndex("is_read")),1);
            senderPersons.Push(sender);
            c.moveToNext();
        }
        return senderPersons;
    }

    public Stack senderMessage(int id,Context context){
        databaseAccess=DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String query = "Select Message.id,name,surname,delivery_date,is_read from User INNER JOIN Message ON User.id=receiver_id WHERE sender_id=?";
        Cursor c = databaseAccess.getDb().rawQuery(query, new String[]{String.valueOf(id)});
        c.moveToFirst();
        Stack receiverPersons = new Stack(c.getCount());
        for(int i=0;i<c.getCount(); i++){
            MessageInfo sender=new MessageInfo(c.getString(c.getColumnIndex("name")),c.getString(c.getColumnIndex("surname")),c.getString(c.getColumnIndex("delivery_date")),c.getInt(c.getColumnIndex("id")),c.getInt(c.getColumnIndex("is_read")),2);
            receiverPersons.Push(sender);
            c.moveToNext();
        }
        return receiverPersons;
    }

    public String findUserMail(int id,Context context){
        databaseAccess=DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String query = "select email from User WHERE id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query, new String[]{String.valueOf(id)});
        c.moveToFirst();
        String email=c.getString(c.getColumnIndex("email"));
        return  email;
    }

    public int sendMessage(String email,int sender_id,String message,Context context){
        databaseAccess=DatabaseAccess.getInstance(context);
        databaseAccess.open();
        Cursor c=  databaseAccess.control_email(email);
        c.moveToFirst();
        if(c.getCount()==0){ return 0;}
        else{
            Calendar calendar= Calendar.getInstance();
            String date=databaseAccess.EditDate(calendar);
            ContentValues values = new ContentValues();
            values.put("is_read",0);
            values.put("receiver_id",c.getInt(c.getColumnIndex("id")));
            values.put("sender_id", sender_id);
            values.put("message",message);
            values.put("delivery_date",date);
            databaseAccess.getDb().insert("Message", null, values);
            return c.getCount();
        }
    }

    public Message viewMessage (int id,Context context,int state){
        databaseAccess=DatabaseAccess.getInstance(context);
        databaseAccess.open();
        String query = "select * from Message WHERE id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query, new String[]{String.valueOf(id)});
        c.moveToFirst();
        Message message = new Message(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("is_read")), c.getInt(c.getColumnIndex("receiver_id")), c.getInt(c.getColumnIndex("sender_id")), c.getString(c.getColumnIndex("message")), c.getString(c.getColumnIndex("delivery_date")));        c.moveToFirst();
        if (state == 1) {
            String query1 = "Update Message Set is_read=1 Where id="+id;
            databaseAccess.getDb().execSQL(query1);
        }

        return message;
    }

    public void Add_Comment (Context context,int post_id,String comment,int user_id){
        databaseAccess= DatabaseAccess.getInstance(context);
        Calendar calendar= Calendar.getInstance();
        String query= String.format("INSERT INTO Comments(post_id,comment,comment_date,user_id) VALUES(%d,\"%s\",\"%s\",%d);",
                post_id,comment,databaseAccess.EditDate(calendar),user_id);
        databaseAccess.getDb().execSQL(query);
    }

    public Map<String, String> See_Post(Context context, int post_id) {
        databaseAccess=DatabaseAccess.getInstance(context);
        Map<String,String> post_map = new HashMap<String, String>();
        String query = "select * from Posts Where id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query,new String[] {String.valueOf(post_id)});
        c.moveToFirst();
        post_map.put("post", c.getString(c.getColumnIndex("post")));
        post_map.put("date",c.getString(c.getColumnIndex("post_date")));
        post_map.put("post_id",c.getString(c.getColumnIndex("id")));
        post_map.put("teacher_id",c.getString(c.getColumnIndex("teacher_id")));
        return post_map;
    }

    public ArrayList<Map<String, String>>  post_comment (int post_id){
        ArrayList<Map<String, String>> list = new ArrayList<>();
        Map<String, String> comment_map = new HashMap<>();

        String query= "select * from Comments where post_id=?";
        Cursor c=databaseAccess.getDb().rawQuery(query,new String[] {String.valueOf(post_id)});
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            comment_map.put("userId",String.valueOf(c.getInt(c.getColumnIndex("user_id"))));
            comment_map.put("commentDate",c.getString(c.getColumnIndex("comment_date")));
            comment_map.put("comment",c.getString(c.getColumnIndex("comment")));
            list.add(comment_map);
            c.moveToNext();
        }
        return list;
    }

}
