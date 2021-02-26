package com.example.pl_son;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME="CENG_Online_Project.db";
    private static final int DATABASE_VERSİON=1;


    public DatabaseOpenHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSİON);
    }
}
