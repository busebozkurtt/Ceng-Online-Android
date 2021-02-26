package com.example.pl_son.Classes;

public class MessageInfo {
    private String name;
    private String surname;
    private String date;
    int state;
    private int id;
    int is_read;

    public MessageInfo(String name, String surname, String date, int id,int is_read,int state) {
        this.name = name;
        this.state=state; // 2 sender, 1 receiver
        this.surname = surname;
        this.date = date;
        this.id = id;
        this.is_read=is_read;
    }

    public String getName() {return name;}
    public void setName(String name) { this.name = name; }

    public int getState() { return state;}
    public void setState(int state) {this.state = state;}

    public String getSurname() {return surname; }
    public void setSurname(String surname) {this.surname = surname;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getIs_read() {return is_read; }
    public void setIs_read(int is_read) {this.is_read = is_read;}

}
