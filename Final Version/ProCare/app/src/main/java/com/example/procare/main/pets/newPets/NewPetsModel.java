package com.example.procare.main.pets.newPets;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.example.procare.database.ProCareDatabase;
import com.example.procare.database.ProCareDbHelper;

public class NewPetsModel  {

    ProCareDbHelper dbHelper;

    NewPetsModel(Context ctx) {
        dbHelper = new ProCareDbHelper(ctx);
    }

    public boolean saveNewPet(String name, String type, Integer userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProCareDatabase.PetTable.COLUMN_NAME_PETNAME, name);
        values.put(ProCareDatabase.PetTable.COLUMN_NAME_TYPE, type);
        values.put(ProCareDatabase.PetTable.COLUMN_NAME_ID_OWNER, userId);

        long newRowId = db.insert(ProCareDatabase.PetTable.TABLE_NAME, null, values);

        if (newRowId == -1) return false;
        else return true;
    }
}
