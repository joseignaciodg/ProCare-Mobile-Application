package com.example.procare.main.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import procare.R;
import com.example.procare.data.App;
import com.example.procare.data.User;
import com.example.procare.main.login.LoginActivity;
import com.example.procare.main.map.MapsActivity;
import com.example.procare.main.toolbar.ToolBarManagement;
import com.example.procare.main.user.books.BooksActivity;

public class UserActivity extends ToolBarManagement implements UserView {
    private UserPresenter mUserPresenter;
    private EditText mNameView;
    private TextView greetings;
    private Button mLogoutButton, mapButton;
    private ImageView mIconEdit;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        sp = getSharedPreferences("preferences", MODE_PRIVATE);

        setUpToolbar();
        bindViews();

        mNameView.setText(App.getApp().getUser().getmUsername());

        mUserPresenter = new UserPresenter(this);

        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v){
                Intent intent = new Intent(UserActivity.this, MapsActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public void logout(View view){
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editoor = settings.edit();
        editoor.putBoolean("darkMode", App.getApp().getDarkMode());
        editoor.putString("language", App.getApp().getLanguage());
        editoor.putString("nombre" , App.getApp().getUser().getmUsername());
        editoor.putString("pass", "");

        editoor.commit();

        App.getApp().setUser(null);
        User.eraseUser();

        // Logout
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void changeName(View view){
        mNameView.setEnabled(true);

        mIconEdit.setImageDrawable(getDrawable(R.drawable.ic_confirm));

        mIconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmName(view);
            }
        });
    }

    @Override
    public void confirmName(View view){
        String mUsername = sp.getString("username", null);
        String mName = mNameView.getText().toString();

        if(mUserPresenter.validateName(mUsername, mName)) {
            // Change username in session
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", mName);
            editor.apply();
        }

        mNameView.setEnabled(false);

        mIconEdit.setImageDrawable(getDrawable(R.drawable.ic_edit));

        mIconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeName(view);
            }
        });
    }

    @Override
    public void fillField() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.fill_field), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void changeSuccessful(){
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.changed_name), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void bindViews() {
        mNameView = findViewById(R.id.name);
        mLogoutButton = findViewById(R.id.button_logout);
        mIconEdit = findViewById(R.id.icon_edit);
        greetings = findViewById(R.id.greeting);
        mapButton = findViewById(R.id.button_map);
        Button urgencies = findViewById(R.id.call);
        Button gps = findViewById(R.id.gps);
        Button find = findViewById(R.id.buscar);

        Resources resources = App.getApp().getResources();

        greetings.setText(resources.getString(R.string.greetings));
        mLogoutButton.setText(resources.getString(R.string.cerrar_sesi_n));
        mapButton.setText(resources.getString(R.string.mapa));
        urgencies.setText(resources.getString(R.string.llamar_a_nurgencias));
        gps.setText(resources.getString(R.string.localizar_ncl_nicas));
        find.setText(resources.getString(R.string.search_books_button));
        TextView bookInfo = findViewById(R.id.books_info);
        bookInfo.setText(resources.getString(R.string.dale_el_mejor_cuidado_a_tu_mascota_con_nuestros_libros));
    }

    public void setUpToolbar() {
        super.setUpToolbar(R.id.button_toolbar_user);
    }

    public void llamarUrgencias(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:+390249764505"));
        startActivity(i);
    }

    public void locateClinics(View view) {
        String loc = App.getApp().getResources().getString(R.string.seach_clinics);

        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void findBooks(View view){
        Intent intent = new Intent(this, BooksActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}