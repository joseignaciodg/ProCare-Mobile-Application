package com.example.procare.data;

import android.content.res.Resources;

public class App {
    private Resources resources = null;
    private boolean darkMode = false;
    private String language = "es";
    private boolean ownEvents = false;
    private static App app = null;
    private String userName;
    private String userId;
    private String pass;
    private User user;

    public static App getApp(){
        if(app != null){
            return app;
        }
        else {
            app = new App();
            return app;
        }
    }

    public boolean getOwnEvents(){
        return this.ownEvents;
    }

    public void setOwnEvents(boolean ownEvents){
        this.ownEvents = ownEvents;
    }

    public void setResources(Resources resources) { this.resources = resources; }

    public Resources getResources() {
        return resources;
    }

    public boolean getDarkMode(){ return darkMode; }

    public void setDarkMode(boolean bool){
        darkMode = bool;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
