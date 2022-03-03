package com.example.procare.main.password;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.procare.database.ProCareDatabase;
import com.example.procare.database.ProCareDbHelper;

public class PasswordModel  {
    ProCareDbHelper dbHelper;

    public PasswordModel(Context ctx){dbHelper = new ProCareDbHelper(ctx);}

    @SuppressLint("Range")
    protected boolean validatePW(String username, String password){
        String mPassword;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = ProCareDatabase.UserTable.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

        String[] projection = {ProCareDatabase.UserTable.COLUMN_NAME_PASSWORD};

        Cursor cursor = db.query(
                ProCareDatabase.UserTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if(cursor.moveToFirst()) {
            mPassword = cursor.getString(cursor.getColumnIndex(ProCareDatabase.UserTable.COLUMN_NAME_PASSWORD));
            cursor.close();
        } else
            return false;

        if(mPassword.equals(password)) return true;
        else return false;
    }

    public void changePassword(String username, String password){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        String whereClause = ProCareDatabase.UserTable.COLUMN_NAME_USERNAME + " = ?";

        contentValues.put(ProCareDatabase.UserTable.COLUMN_NAME_PASSWORD, password);

        db.update(ProCareDatabase.UserTable.TABLE_NAME, contentValues, whereClause, new String[]{username});
    }
}
