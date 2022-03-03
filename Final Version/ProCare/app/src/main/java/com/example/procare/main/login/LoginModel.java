package com.example.procare.main.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.procare.data.App;
import com.example.procare.data.User;
import com.example.procare.database.ProCareDatabase.UserTable;
import com.example.procare.database.ProCareDbHelper;

public class LoginModel  {
    private String mName;
    private String mUsername;
    private String mPassword;
    private Integer mId;

    ProCareDbHelper dbHelper;

    String[] projection = {
            BaseColumns._ID,
            UserTable.COLUMN_NAME_NAME,
            UserTable.COLUMN_NAME_USERNAME,
            UserTable.COLUMN_NAME_PASSWORD
    };

    LoginModel(Context ctx){
        mName = "name";
        mUsername = "username";
        mPassword = "password";
        dbHelper = new ProCareDbHelper(ctx);
    }

    @SuppressLint("Range")
    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = UserTable.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                UserTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if(cursor.moveToFirst()) {
            mName = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_NAME));
            mUsername = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_USERNAME));
            mPassword = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME_PASSWORD));
            mId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            cursor.close();
        }
        else
            return false;

        if(mUsername.equals(username) && mPassword.equals(password)){
            App.getApp().setUser(User.getInstance(mName, mUsername, mId));
            return true;
        }
        else
            return false;
    }
}
