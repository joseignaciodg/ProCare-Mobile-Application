package com.example.procare.main.calendar;

import com.applandeo.materialcalendarview.EventDay;

import java.util.List;

public interface CalendarView {
    void setEvents(List<EventDay> events);
    void bindViews();
}
