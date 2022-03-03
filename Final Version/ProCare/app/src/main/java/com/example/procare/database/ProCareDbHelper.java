package com.example.procare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.procare.database.ProCareDatabase.UserTable;
import com.example.procare.database.ProCareDatabase.PetTable;
import com.example.procare.database.ProCareDatabase.TaskTable;
import com.example.procare.database.ProCareDatabase.EventsTable;

public class ProCareDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 14;
    public static final String DATABASE_NAME = "ProCareDB.db";
    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                    UserTable._ID + " INTEGER PRIMARY KEY," +
                    UserTable.COLUMN_NAME_NAME + " TEXT NOT NULL," +
                    UserTable.COLUMN_NAME_USERNAME + " TEXT UNIQUE NOT NULL," +
                    UserTable.COLUMN_NAME_PASSWORD + " TEXT NOT NULL);";
    private static final String SQL_CREATE_PET_TABLE =
            "CREATE TABLE " + PetTable.TABLE_NAME + " (" +
                    PetTable._ID + " INTEGER PRIMARY KEY," +
                    PetTable.COLUMN_NAME_PETNAME + " TEXT NOT NULL," +
                    PetTable.COLUMN_NAME_TYPE + " TEXT NOT NULL," +
                    PetTable.COLUMN_NAME_ID_OWNER + " INTEGER, FOREIGN KEY (" + PetTable.COLUMN_NAME_ID_OWNER + ") REFERENCES " +
                    UserTable.TABLE_NAME + "(" + UserTable._ID + ") ON DELETE CASCADE );";
    private static final String SQL_CREATE_TASK_TABLE =
            "CREATE TABLE " + TaskTable.TABLE_NAME + " (" +
                    TaskTable._ID + " INTEGER PRIMARY KEY," +
                    TaskTable.COLUMN_NAME_TASKNAME + " TEXT NOT NULL," +
                    TaskTable.COLUMN_NAME_SCHEDULE_DATETIME + " DATETIME NOT NULL," +
                    TaskTable.COLUMN_NAME_TASKDONE_DATETIME + " DATETIME," +
                    TaskTable.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    TaskTable.COLUMN_NAME_FREQUENCY + " INTEGER NOT NULL," +
                    TaskTable.COLUMN_NAME_ID_PET + " INTEGER, FOREIGN KEY (" + TaskTable.COLUMN_NAME_ID_PET + ") REFERENCES " +
                    PetTable.TABLE_NAME + "(" + PetTable._ID + ") ON DELETE CASCADE );";
    private static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE " + EventsTable.TABLE_NAME + " (" +
                    EventsTable.COLUMN_NAME_EVENTNAME + " TEXT PRIMARY KEY," +
                    EventsTable.COLUMN_NAME_EVENTLAT + " NUM NOT NULL," +
                    EventsTable.COLUMN_NAME_EVENTLONG + " NUM NOT NULL," +
                    EventsTable.COLUMN_NAME_EVENTDESCRIPTION + " TEXT," +
                    EventsTable.COLUMN_NAME_EVENT_NAMEUSER + " TEXT NOT NULL," +
                    EventsTable.COLUMN_NAME_ID_USER + " INTEGER, FOREIGN KEY (" + EventsTable.COLUMN_NAME_ID_USER + ") REFERENCES " +
                    UserTable.TABLE_NAME + "(" + UserTable._ID + ") ON DELETE CASCADE );";

    private static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + UserTable.TABLE_NAME;
    private static final String SQL_DELETE_PET_TABLE =
            "DROP TABLE IF EXISTS " + PetTable.TABLE_NAME;
    private static final String SQL_DELETE_TASK_TABLE =
            "DROP TABLE IF EXISTS " + TaskTable.TABLE_NAME;
    private static final String SQL_DELETE_EVENTS_TABLE =
            "DROP TABLE IF EXISTS " + EventsTable.TABLE_NAME;

    public ProCareDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_PET_TABLE);
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_TABLE);
        db.execSQL(SQL_DELETE_PET_TABLE);
        db.execSQL(SQL_DELETE_TASK_TABLE);
        db.execSQL(SQL_DELETE_EVENTS_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}