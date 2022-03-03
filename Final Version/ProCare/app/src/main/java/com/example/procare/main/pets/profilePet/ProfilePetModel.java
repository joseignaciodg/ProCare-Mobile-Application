package com.example.procare.main.pets.profilePet;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import procare.R;

import com.example.procare.data.Task;
import com.example.procare.database.ProCareDatabase;
import com.example.procare.database.ProCareDbHelper;

public class ProfilePetModel  {
    ProCareDbHelper dbHelper;
    Context ctx;

    ProfilePetModel(Context ctx) {
        dbHelper = new ProCareDbHelper(ctx);
        this.ctx = ctx;
    }

    public boolean deletePet(Integer petId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        long newRowId = db.delete(ProCareDatabase.PetTable.TABLE_NAME, BaseColumns._ID + "=?", new String[]{String.valueOf(petId)});

        if (newRowId == -1) return false;
        else return true;
    }

    public boolean editPet(Integer petId, String name, String type, Integer userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProCareDatabase.PetTable.COLUMN_NAME_PETNAME, name);
        values.put(ProCareDatabase.PetTable.COLUMN_NAME_TYPE, type);
        values.put(ProCareDatabase.PetTable.COLUMN_NAME_ID_OWNER, userId);

        long newRowId = db.update(ProCareDatabase.PetTable.TABLE_NAME, values, BaseColumns._ID + "=?", new String[]{String.valueOf(petId)});

        if (newRowId == -1)
            return false;
        else
            return true;
    }

    public List<Task> getAllPetTasks(Integer petId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT t." + BaseColumns._ID +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_ID_PET +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_TASKNAME +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_SCHEDULE_DATETIME +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_TASKDONE_DATETIME +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_DESCRIPTION +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_FREQUENCY +
                " FROM " + ProCareDatabase.TaskTable.TABLE_NAME + " t JOIN " + ProCareDatabase.PetTable.TABLE_NAME +
                " p ON t." + ProCareDatabase.TaskTable.COLUMN_NAME_ID_PET + " = p." + BaseColumns._ID + " WHERE p." + ProCareDatabase.PetTable._ID +
                "= " + String.valueOf(petId) + " ORDER BY t." + ProCareDatabase.TaskTable.COLUMN_NAME_SCHEDULE_DATETIME;
        Cursor cursor = db.rawQuery(query, null);
        List values = new ArrayList<Task>();

        while(cursor.moveToNext()){
            @SuppressLint("Range") Integer taskId = cursor.getInt(cursor.getColumnIndex(ProCareDatabase.TaskTable._ID));
            @SuppressLint("Range") Integer petIdAux = cursor.getInt(cursor.getColumnIndex(ProCareDatabase.TaskTable.COLUMN_NAME_ID_PET));
            @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex(ProCareDatabase.TaskTable.COLUMN_NAME_TASKNAME));
            @SuppressLint("Range") String scheduleDatetime = cursor.getString(cursor.getColumnIndex(ProCareDatabase.TaskTable.COLUMN_NAME_SCHEDULE_DATETIME));
            @SuppressLint("Range") String taskDoneDatetime = cursor.getString(cursor.getColumnIndex(ProCareDatabase.TaskTable.COLUMN_NAME_TASKDONE_DATETIME));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(ProCareDatabase.TaskTable.COLUMN_NAME_DESCRIPTION));
            @SuppressLint("Range") Integer freq = cursor.getInt(cursor.getColumnIndex(ProCareDatabase.TaskTable.COLUMN_NAME_FREQUENCY));
            Task task = new Task(taskId, petIdAux, taskName, scheduleDatetime, taskDoneDatetime, description, freq, ctx.getResources().getStringArray(R.array.task_frequency_array));
            values.add(task);
        }
        cursor.close();
        return values;
    }
}