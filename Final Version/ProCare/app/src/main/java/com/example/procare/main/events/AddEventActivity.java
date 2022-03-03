package com.example.procare.main.events;

import androidx.annotation.RequiresApi;

import com.example.procare.main.map.MapsActivity;
import procare.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procare.main.toolbar.ToolBarManagement;
import com.example.procare.data.App;

public class AddEventActivity extends ToolBarManagement implements EventsView {
    private EventsPresenter mEventsPresenter;
    private Button submit;
    private EditText event_name, event_description;
    private TextView event_name_title, event_description_title;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mEventsPresenter = new EventsPresenter(this);
        bindViews();
        updateLanguage();

        submit.setOnClickListener(view -> {
            String eName, eDescription;

            eName = event_name.getText().toString();
            eDescription = event_description.getText().toString();

            if (eName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();
            } else {
                if (mEventsPresenter.existEvent(eName)) {
                    Toast.makeText(getApplicationContext(), "This event already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    double latitude, longitude;
                    SharedPreferences settings = getSharedPreferences("ubication", Context.MODE_PRIVATE);
                    latitude = Double.parseDouble(settings.getString("latitude", ""));
                    longitude = Double.parseDouble(settings.getString("longitude", ""));
                    mEventsPresenter.addNewEvent(latitude, longitude, App.getApp().getUser().getmUsername(), eName, eDescription);
                    Intent intent = new Intent(AddEventActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void bindViews() {
        event_name = findViewById(R.id.add_event_name_value);
        event_description = findViewById(R.id.add_event_description_value);
        submit = findViewById(R.id.add_event_submit);
        event_description_title = findViewById(R.id.add_event_description_title);
        event_name_title = findViewById(R.id.add_event_name_title);
    }

    private void updateLanguage() {
        event_name_title.setText(App.getApp().getResources().getString(R.string.add_event_name));
        event_name.setHint(App.getApp().getResources().getString(R.string.add_event_name));
        event_description_title.setText(App.getApp().getResources().getString(R.string.add_event_description));
        event_description.setHint(App.getApp().getResources().getString(R.string.add_event_description));
        submit.setText(App.getApp().getResources().getString(R.string.add_event_button));
    }

    @Override
    public void addNewEvent() {

    }

    @Override
    public void viewEvent(String name, Integer id) {

    }

    @Override
    public void noRegister() {

    }

    @Override
    public void deleteEvent() {

    }
}