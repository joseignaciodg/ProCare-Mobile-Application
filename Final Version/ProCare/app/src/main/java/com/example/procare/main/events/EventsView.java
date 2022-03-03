package com.example.procare.main.events;

public interface EventsView {
    void addNewEvent();
    void viewEvent(String name, Integer id);
    void noRegister();
    void deleteEvent();
    void bindViews();
}
