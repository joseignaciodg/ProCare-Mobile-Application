package com.example.procare.main.calendar;

import android.content.Context;
import com.applandeo.materialcalendarview.EventDay;
import java.util.List;

public class CalendarPresenter {
    private CalendarView mCalendarView;
    private CalendarModel mCalendarModel;

    CalendarPresenter(CalendarView view){
        mCalendarModel = new CalendarModel((Context) view);
        mCalendarView = view;
    }

    public void prepareEvents(Integer userId){
        List<EventDay> events = mCalendarModel.getEvents(userId);
        mCalendarView.setEvents(events);
    }
}
