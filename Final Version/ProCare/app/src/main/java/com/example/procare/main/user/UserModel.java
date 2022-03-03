package com.example.procare.main.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.procare.database.ProCareDatabase;
import com.example.procare.database.ProCareDbHelper;

public class UserModel {

    ProCareDbHelper dbHelper;

    UserModel(Context ctx) {
        dbHelper = new ProCareDbHelper(ctx);
    }

    public void setName(String username, String Name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        String whereClause = ProCareDatabase.UserTable.COLUMN_NAME_USERNAME + " = ?";

        contentValues.put(ProCareDatabase.UserTable.COLUMN_NAME_USERNAME, Name);

        db.update(ProCareDatabase.UserTable.TABLE_NAME, contentValues, whereClause, new String[]{username});
    }
}
