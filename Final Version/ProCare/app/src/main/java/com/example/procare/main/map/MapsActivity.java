package com.example.procare.main.map;

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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.Objects;

import com.example.procare.data.App;
import com.example.procare.main.events.EventInfo;
import com.example.procare.main.events.EventsPresenter;

import com.example.procare.main.events.AddEventActivity;

import com.example.procare.data.Event;
import com.example.procare.main.events.EventsView;

import procare.R;

public class MapsActivity extends ToolBarManagement implements OnMapReadyCallback, EventsView {

    private GoogleMap mMap;
    private Button new_event_button;
    public double latitude, longitude;
    private List<Event> listEvets;
    private LatLng miUbicacion;
    private EventsPresenter mEventsPresenter;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpToolbar();
        getLocalizacion();
        bindViews();

        new_event_button.setText(App.getApp().getResources().getString(R.string.add_event_floating_button));

        locationManager = (LocationManager) Objects.requireNonNull(this.getApplicationContext()).getSystemService(Context.LOCATION_SERVICE);
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
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        latitude = currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();
        miUbicacion = new LatLng(latitude, longitude);

        setUpToolbar(R.id.button_toolbar_user);
        new_event_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, AddEventActivity.class);
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();

                SharedPreferences settings = getSharedPreferences("ubication", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.remove("latitude");
                editor.remove("longitude");
                editor.putString("latitude", Double.toString(latitude));
                editor.putString("longitude", Double.toString(longitude));
                editor.apply();
                startActivity(intent);
            }
        });
    }

    private void getLocalizacion() {

        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


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
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        latitude = currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();
        miUbicacion = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(miUbicacion)
                .zoom(15)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mEventsPresenter = new EventsPresenter(this);
        listEvets = new ArrayList<>();

        SharedPreferences settings2 = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        Boolean ownEvents = settings2.getBoolean("ownEvents", false);

        if (ownEvents) {
            listEvets = mEventsPresenter.validateUserEvents(App.getApp().getUser().getmId());
        } else {
            listEvets = mEventsPresenter.getAllEvetns();
        }

        for (int i = 0; i < listEvets.size(); i++) {
            Event e = listEvets.get(i);
            LatLng ubication = new LatLng(e.getGeoLat(), e.getGeoLong());
            Marker m = mMap.addMarker(new MarkerOptions().title(e.getName()).position(new LatLng(e.getGeoLat(), e.getGeoLong())).snippet(e.getDescription()));
            m.setTag(e);
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
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
        viewLocationManager(mMap);
    }


    public void viewLocationManager(GoogleMap mMap) {
        mMap.setOnMarkerClickListener(marker -> {

            marker.showInfoWindow();
            Object tag = marker.getTag();

            if (tag instanceof Event) {

                Event event = (Event) tag;
                SharedPreferences settings = getSharedPreferences("event", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.remove("name");
                editor.remove("user");
                editor.putString("name", event.getName());
                editor.putString("user", event.getUserId());
                editor.apply();

                Intent intent = new Intent(MapsActivity.this, EventInfo.class);
                startActivity(intent);
            }
            return false;
        });

    }

    public void setUpToolbar() {
        super.setUpToolbar(R.id.button_toolbar_user);
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
    public void bindViews() {
        new_event_button = findViewById(R.id.add_event_button);
    }
}
