package com.example.procare.main.settings;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.procare.data.App;

public class SettingsPresenter {
    private SettingsView mSettingsView;
    private SettingsModel mSettingsModel;

    SettingsPresenter(SettingsView view){
        mSettingsView = view;
        mSettingsModel = new SettingsModel();
    }

    public void screenModeChanged() {
        if(!App.getApp().getDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    //Future work disable notifications//
    public void notificationsEnabled() {
        //need to create methods in AlarmBroadcastReceiver.java
    }

    public void notificationsDisable() {
        //need to create methods in AlarmBroadcastReceiver.java
    }
}
