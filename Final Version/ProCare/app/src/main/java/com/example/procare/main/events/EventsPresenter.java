package com.example.procare.main.events;

import android.content.Context;
import android.util.Log;

import java.util.List;

import com.example.procare.data.App;
import com.example.procare.data.Event;

public class EventsPresenter {
    private EventsView mEventsView;
    private EventsModel mEventsModel;
    private Event event;

    public EventsPresenter(EventsView eventsView) {
        this.mEventsView = eventsView;
        mEventsModel = new EventsModel((Context) eventsView);
    }


    public void addNewEvent(double geoLat, double geoLong, String nameUser, String name, String description) {
        Integer integer = mEventsModel.saveNewEvent(geoLat, geoLong, nameUser, name, description);
        mEventsView.addNewEvent();
    }

    public boolean existEvent(String nameEvent) {
        Event event = mEventsModel.getAEvent(nameEvent, App.getApp().getUser().getmId().toString());
        if (event.getName() != null)  {
            if(event.getName().equals(nameEvent)){
                return true;
            }else
                return false;
        } else
            return false;
    }

    public List<Event> validateUserEvents(Integer userId) {
        if (userId == null) {
            mEventsView.noRegister();
        }
        return mEventsModel.getEvents(userId.toString());
    }

    public List<Event> getAllEvetns() {
        return mEventsModel.getAllEvents();
    }

    public void deleteEvent(String key) {
        boolean fin = mEventsModel.deleteEvent(key);
        mEventsView.deleteEvent();
    }
}
