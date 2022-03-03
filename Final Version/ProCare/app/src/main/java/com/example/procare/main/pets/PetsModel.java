package com.example.procare.main.pets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import com.example.procare.data.Pets;
import com.example.procare.database.ProCareDatabase.PetTable;
import com.example.procare.database.ProCareDbHelper;

public class PetsModel  {

    ProCareDbHelper dbHelper;

    String[] projection = {
            BaseColumns._ID,
            PetTable.COLUMN_NAME_PETNAME,
            PetTable.COLUMN_NAME_TYPE,
            PetTable.COLUMN_NAME_ID_OWNER
    };

    PetsModel(Context ctx) {
        dbHelper = new ProCareDbHelper(ctx);
    }


    @SuppressLint("Range")
    public List<Pets> getPets (Integer userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = PetTable.COLUMN_NAME_ID_OWNER + " = ?";
        String[] selectionArgs = {userId + ""};

        Cursor cursor = db.query(
                PetTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List values = new ArrayList<Pets>();
        while(cursor.moveToNext()){
            Pets pet = new Pets();
            pet.setId(cursor.getInt(cursor.getColumnIndex(PetTable._ID)));
            pet.setName(cursor.getString(cursor.getColumnIndex(PetTable.COLUMN_NAME_PETNAME)));
            pet.setType(cursor.getString(cursor.getColumnIndex(PetTable.COLUMN_NAME_TYPE)));
            pet.setIdOwner(cursor.getInt(cursor.getColumnIndex(PetTable.COLUMN_NAME_ID_OWNER)));
            values.add(pet);
        }
        cursor.close();
        return values;
    }

}