package com.example.procare.data;

public class Event {
    private double geoLat;
    private double geoLong;
    private String name;
    private String description;
    private String userName;
    private String userId;

    public Event() {

    }

    public Event(double geoLat, double geoLong, String userName, String name, String description, String userId) {
        this.geoLat = geoLat;
        this.geoLong = geoLong;
        this.userName = userName;
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeoLat(double geoLat) {
        this.geoLat = geoLat;
    }

    public void setGeoLong(double geoLong) {
        this.geoLong = geoLong;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public double getGeoLong() {
        return geoLong;
    }

    public String getDescription() {
        return description;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserId(){return this.userId;}

    public void setUserId(String userId){this.userId = userId;}

}
