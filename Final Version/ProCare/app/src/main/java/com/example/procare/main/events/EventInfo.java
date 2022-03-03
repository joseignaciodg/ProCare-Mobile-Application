package com.example.procare.main.events;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.procare.main.login.LoginActivity;
import com.example.procare.main.toolbar.ToolBarManagement;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import com.example.procare.main.map.MapsActivity;
import com.example.procare.data.App;
import com.example.procare.data.Event;

import procare.R;

public class EventInfo extends ToolBarManagement implements OnMapReadyCallback, EventsView {
    private TextView vEvent_name, event_name_title, event_description, event_description_title, event_added_by_value, event_added_by_value_title;
    private GoogleMap mMap;
    private List<Event> listEvets;
    public double latitude;
    public double longitude;
    private LatLng eventUbication;
    private Button event_deleteEvent;
    private EventsPresenter mEventsPresenter;
    private EventsModel mEventsModel;
    private ConstraintLayout toolbar;
    private List<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocalizacion();
        setUpToolbar();
        bindViews();
        updateLanguage();
        toolbar = findViewById(R.id.toolbar_map);
        mEventsPresenter = new EventsPresenter(this);
        mEventsModel = new EventsModel(getApplicationContext());
        SharedPreferences settings = getSharedPreferences("event", Context.MODE_PRIVATE);
        String event_name = settings.getString("name", "");
        String user_id = settings.getString("user", "");
        Event event = mEventsModel.getAEvent(event_name, user_id);

        eventUbication = new LatLng(event.getGeoLat(), event.getGeoLong());

        vEvent_name.setText(event.getName());
        event_added_by_value.setText(event.getUserName());
        if (event.getDescription().isEmpty()) {
            event_description.setText(App.getApp().getResources().getString(R.string.no_description));
        } else {
            event_description.setText(event.getDescription());
        }

        if (event.getUserId().equals(App.getApp().getUser().getmId().toString())) {

            event_deleteEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventsPresenter.deleteEvent(event.getName());
                    Intent intent = new Intent(EventInfo.this, MapsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            event_deleteEvent.setVisibility(View.INVISIBLE);
        }
    }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void bindViews() {
        vEvent_name = findViewById(R.id.event_name_value);
        event_description = findViewById(R.id.event_description_value);
        event_deleteEvent = findViewById(R.id.delete_event);
        event_description_title = findViewById(R.id.event_description_title);
        event_added_by_value = findViewById(R.id.event_added_by_value);
        event_name_title = findViewById(R.id.event_name_title);
        event_added_by_value_title = findViewById(R.id.event_added_by_title);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mEventsPresenter = new EventsPresenter(this);

        listEvets = new ArrayList<>();
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        Boolean ownEvents = settings.getBoolean("ownEvents", false);
        if (ownEvents) {
            listEvets = mEventsPresenter.validateUserEvents(App.getApp().getUser().getmId());
        }else {
            listEvets = mEventsPresenter.getAllEvetns();
        }
        SharedPreferences settings2 = getSharedPreferences("event", Context.MODE_PRIVATE);
        String event_name = settings2.getString("name", "");
        String user_id = settings2.getString("user", "");
        Event selected_event = mEventsModel.getAEvent(event_name, user_id);

        for (int i = 0; i < listEvets.size(); i++) {
            Event e = listEvets.get(i);
            LatLng ubication = new LatLng(e.getGeoLat(), e.getGeoLong());
            Marker m = mMap.addMarker(new MarkerOptions().title(e.getName()).position(new LatLng(e.getGeoLat(), e.getGeoLong())).snippet(e.getDescription()));
            m.setTag(e);
            if (e.getUserId().equals(App.getApp().getUserId()) && e.getName().equals(event_name)) {
                m.showInfoWindow();
            }

        }

        mMap.setMyLocationEnabled(true);

        focusCam(mMap, eventUbication);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                SharedPreferences settings = getSharedPreferences("ubication", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = settings.edit();

                editor.remove("latitude");
                editor.remove("longitude");
                editor.putString("latitude", Double.toString(latitude));
                editor.putString("longitude", Double.toString(longitude));
                editor.apply();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 40, 0, locationListener);

        mMap.setOnMarkerClickListener(marker -> {

            Object tag = marker.getTag();
            if (tag instanceof Event) {
                Event event = (Event) tag;

                SharedPreferences.Editor editor;
                editor = settings.edit();

                editor.remove("name");
                editor.remove("user");
                editor.putString("name", event.getName());
                editor.putString("user", event.getUserName());
                editor.apply();

                Intent intent = new Intent(this, EventInfo.class);
                startActivity(intent);

            }
            return false;
        });
    }

    public void focusCam(GoogleMap mMap, LatLng ubication) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubication));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ubication)
                .zoom(5)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        for (Marker marker : markers) {
            if (marker.getPosition() == ubication) {
                marker.showInfoWindow();
            }
        }
    }

    private void updateLanguage() {
        event_description_title.setText(App.getApp().getResources().getString(R.string.add_event_description));
        event_name_title.setText(App.getApp().getResources().getString(R.string.add_event_name));
        event_added_by_value_title.setText(App.getApp().getResources().getString(R.string.add_event_added_by));
    }

    public void setUpToolbar() {
        super.setUpToolbar(R.id.button_toolbar_user);
    }
}