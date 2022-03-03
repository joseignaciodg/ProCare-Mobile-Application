package com.example.procare.main.events;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.procare.data.App;
import com.example.procare.data.Event;
import com.example.procare.database.ProCareDatabase;
import com.example.procare.database.ProCareDatabase.EventsTable;
import com.example.procare.database.ProCareDbHelper;

public class EventsModel {

    ProCareDbHelper dbHelper;
    Context ctx;

    String[] projection = {
            BaseColumns._ID,
            EventsTable.COLUMN_NAME_EVENTNAME,
            EventsTable.COLUMN_NAME_EVENTLAT,
            EventsTable.COLUMN_NAME_EVENTLONG,
            EventsTable.COLUMN_NAME_EVENTDESCRIPTION,
            EventsTable.COLUMN_NAME_EVENT_NAMEUSER
    };

    EventsModel(Context ctx) {
        this.ctx = ctx;
        dbHelper = new ProCareDbHelper(ctx);
        ;
    }

    public Integer saveNewEvent(double geoLat, double geoLong, String nameUser, String name, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insertion
        ContentValues values = new ContentValues();
        values.put(EventsTable.COLUMN_NAME_EVENTNAME, name);
        values.put(EventsTable.COLUMN_NAME_EVENT_NAMEUSER, nameUser);
        values.put(EventsTable.COLUMN_NAME_EVENTLAT, geoLat);
        values.put(EventsTable.COLUMN_NAME_EVENTLONG, geoLong);
        values.put(EventsTable.COLUMN_NAME_EVENTDESCRIPTION, description);
        values.put(EventsTable.COLUMN_NAME_ID_USER, App.getApp().getUser().getmId());
        long newRowId = db.insert(ProCareDatabase.EventsTable.TABLE_NAME, null, values);
        return (int) newRowId;
    }


    @SuppressLint("Range")
    public List<Event> getEvents(String userId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT t." + EventsTable.COLUMN_NAME_EVENTNAME +
                ", t." + EventsTable.COLUMN_NAME_EVENTDESCRIPTION +
                ", t." + EventsTable.COLUMN_NAME_EVENTLAT +
                ", t." + EventsTable.COLUMN_NAME_EVENTLONG +
                ", t." + EventsTable.COLUMN_NAME_EVENT_NAMEUSER +
                ", t." + EventsTable.COLUMN_NAME_ID_USER +
                " FROM " + EventsTable.TABLE_NAME + " t JOIN " + ProCareDatabase.UserTable.TABLE_NAME +
                " p ON t." + EventsTable.COLUMN_NAME_ID_USER + " = p." + BaseColumns._ID + " WHERE p." + ProCareDatabase.UserTable._ID +
                "= " + userId;
        Cursor cursor = db.rawQuery(query, null);

        List values = new ArrayList<Event>();
        while (cursor.moveToNext()) {
            Event event = new Event();
            event.setName(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTNAME)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTDESCRIPTION)));
            event.setGeoLat(cursor.getDouble(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTLAT)));
            event.setGeoLong(cursor.getDouble(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTLONG)));
            event.setUserName(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENT_NAMEUSER)));
            event.setUserId(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_ID_USER)));
            values.add(event);

        }
        cursor.close();

        return values;
    }

    public Event getAEvent(String name, String userId) {
        List<Event> l_event = new ArrayList<>();
        Event e = new Event();
        l_event = getEvents(userId);
        int i = 0;
        boolean find = false;
        while (i < l_event.size() && !find) {
            e = l_event.get(i);
            if (e.getName().equals(name)) {
                find = true;

            } else {
                i++;
            }
        }
        return e;
    }

    public boolean deleteEvent(String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(EventsTable.TABLE_NAME, EventsTable.COLUMN_NAME_EVENTNAME + " = " + "'"+key+"'", null) > 0;
    }

    @SuppressLint("Range")
    public List<Event> getAllEvents() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + EventsTable.COLUMN_NAME_EVENTNAME +
                ", " + EventsTable.COLUMN_NAME_EVENTDESCRIPTION +
                ", " + EventsTable.COLUMN_NAME_EVENTLAT +
                ", " + EventsTable.COLUMN_NAME_EVENTLONG +
                ", " + EventsTable.COLUMN_NAME_EVENT_NAMEUSER +
                ", " + EventsTable.COLUMN_NAME_ID_USER +
                " FROM " + EventsTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        List values = new ArrayList<Event>();
        while (cursor.moveToNext()) {
            Event event = new Event();
            event.setName(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTNAME)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTDESCRIPTION)));
            event.setGeoLat(cursor.getDouble(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTLAT)));
            event.setGeoLong(cursor.getDouble(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENTLONG)));
            event.setUserName(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_EVENT_NAMEUSER)));
            event.setUserId(cursor.getString(cursor.getColumnIndex(EventsTable.COLUMN_NAME_ID_USER)));
            values.add(event);

        }
        cursor.close();

        return values;
    }


}