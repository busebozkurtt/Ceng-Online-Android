package com.example.pl_son.Classes;

import java.util.Date;

public class Message {
        int id;
        int is_read;
        int receiver_id;
        int sender_id;
        String message;
        String date;

        public Message(int id, int is_read, int receiver_id, int sender_id, String message, String  date) {
            this.id = id;
            this.is_read = is_read;
            this.receiver_id = receiver_id;
            this.sender_id = sender_id;
            this.message = message;
            this.date = date;
        }

        public int getId() { return id;}
        public void setId(int id) { this.id = id; }

        public int getIs_read() {return is_read;}
        public void setIs_read(int is_read) {this.is_read = is_read;}

        public int getReceiver_id() { return receiver_id;}
        public void setReceiver_id(int receiver_id) {this.receiver_id = receiver_id;}

        public int getSender_id() { return sender_id; }
        public void setSender_id(int sender_id) {this.sender_id = sender_id; }

        public String getMessage() {return message;}
        public void setMessage(String message) {this.message = message; }

        public String getDate() {return date;}
        public void setDate(String date) {this.date = date;}

}

