package com.example.procare.main.upcoming;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import procare.R;
import com.example.procare.data.Task;
import com.example.procare.database.ProCareDatabase;
import com.example.procare.database.ProCareDatabase.PetTable;
import com.example.procare.database.ProCareDbHelper;

public class UpcomingModel {
    ProCareDbHelper dbHelper;
    Context ctx;

    UpcomingModel(Context ctx) {
        dbHelper = new ProCareDbHelper(ctx);
        this.ctx = ctx;
    }

    public List<Task> getAllTasks (Integer userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String query = "SELECT t." + BaseColumns._ID +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_ID_PET +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_TASKNAME +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_SCHEDULE_DATETIME +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_TASKDONE_DATETIME +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_DESCRIPTION +
                ", t." + ProCareDatabase.TaskTable.COLUMN_NAME_FREQUENCY +
                " FROM " + ProCareDatabase.TaskTable.TABLE_NAME + " t JOIN " + ProCareDatabase.PetTable.TABLE_NAME +
                " p ON t." + ProCareDatabase.TaskTable.COLUMN_NAME_ID_PET + " = p." + BaseColumns._ID + " WHERE p." + ProCareDatabase.PetTable.COLUMN_NAME_ID_OWNER +
                "= " + String.valueOf(userId) + " ORDER BY t." + ProCareDatabase.TaskTable.COLUMN_NAME_SCHEDULE_DATETIME;
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

            int interval=1, loopLimit=1; // Interval: number of days/weeks/months/years to add to original date

            // Check frequency and insert necessary number of copies of task within a year into database
            switch(freq){
                case Task.FREQUENCY_DAILY:
                    loopLimit = 365;
                    break;
                case Task.FREQUENCY_WEEKLY:
                    loopLimit = 52;
                    break;
                case Task.FREQUENCY_MONTHLY:
                    loopLimit = 12;
                    break;
                case Task.FREQUENCY_YEARLY:
                    loopLimit = 2;
                    break;
                default: // No frequency
                    interval = 0;
            }

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(dateFormat.parse(scheduleDatetime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 1; i < loopLimit; i++){
                switch(freq){
                    case Task.FREQUENCY_DAILY:
                        calendar.add(Calendar.DATE, interval);
                        break;
                    case Task.FREQUENCY_WEEKLY:
                        calendar.add(Calendar.WEEK_OF_YEAR, interval);
                        break;
                    case Task.FREQUENCY_MONTHLY:
                        calendar.add(Calendar.MONTH, interval);
                        break;
                    case Task.FREQUENCY_YEARLY:
                        calendar.add(Calendar.YEAR, interval);
                        break;
                    default: // No frequency: for loop not reachable for this case
                }

                String dateTimeString = dateFormat.format(calendar.getTime());

                task = new Task(taskId, petIdAux, taskName, dateTimeString, taskDoneDatetime, description, freq, ctx.getResources().getStringArray(R.array.task_frequency_array));
                values.add(task);
            }
        }
        cursor.close();
        Collections.sort(values);
        return values;
    }

    @SuppressLint("Range")
    public String getPetName(Integer petId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String petName = null;
        String[] projection = {
                BaseColumns._ID,
                PetTable.COLUMN_NAME_PETNAME
        };

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {String.valueOf(petId)};

        Cursor cursor = db.query(
                PetTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                null            // The sort order
        );

        if(cursor.moveToFirst())
            petName = cursor.getString(cursor.getColumnIndex(PetTable.COLUMN_NAME_PETNAME));
        cursor.close();

        return petName;
    }

    public boolean hasPets() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT count(*) FROM " + PetTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
