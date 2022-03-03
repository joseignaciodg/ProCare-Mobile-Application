package com.example.procare.main.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

import procare.R;
import com.example.procare.data.App;
import com.example.procare.data.LocaleHelper;
import com.example.procare.data.User;
import com.example.procare.main.pets.PetsActivity;
import com.example.procare.main.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements LoginView{
    public static final String PREFS_NAME = "Settings";
    private LoginPresenter mLoginPresenter;
    private EditText mUsername, mPassword;
    private Button mIniciasesion;
    private TextView mRegister;
    private Resources resources;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginPresenter = new LoginPresenter(this);
        App.getApp().setResources(getResources());
        bindViews();

        recover();
        resources = App.getApp().getResources();
        mIniciasesion.setText(App.getApp().getResources().getString(R.string.iniciar_sesion_button));
        TextView registerInfo = findViewById(R.id.login_register_info);
        registerInfo.setText(App.getApp().getResources().getString(R.string.aun_no_tienes_cuenta));
        mRegister.setText(App.getApp().getResources().getString(R.string.reg_strate));

        mIniciasesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.validateLogin(String.valueOf(mUsername.getText()), String.valueOf(mPassword.getText()));
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.launchRegister();
            }
        });

        sp = getSharedPreferences("preferences", MODE_PRIVATE);
    }

    @Override
    public void loginSuccessfull() {
        App.getApp().setPass(String.valueOf(mPassword.getText()));

        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editoor = settings.edit();
        editoor.putBoolean("darkMode", App.getApp().getDarkMode());
        editoor.putString("language", App.getApp().getLanguage());
        editoor.putString("username" , App.getApp().getUser().getmUsername());
        editoor.putString("pass", App.getApp().getPass());

        editoor.commit();

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", User.getInstance(null, App.getApp().getUserName(), 0).getmUsername());
        editor.apply();

        Intent intent = new Intent(this, PetsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailure() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.username_password_invalid), Toast.LENGTH_LONG);
        toast.show();
        mPassword.setText("");
    }

    @Override
    public void launchRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void recover(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        App.getApp().setLanguage(settings.getString("language", "en"));
        App.getApp().setUserName(settings.getString("nombre", ""));
        App.getApp().setDarkMode(settings.getBoolean("darkMode", false));
        App.getApp().setPass(settings.getString("pass", ""));

        Context context;
        Locale locale;
        if(App.getApp().getLanguage().equals("es")){
            context = LocaleHelper.setLocale(LoginActivity.this, "es");
            locale = new Locale("es");
            App.getApp().setLanguage("es");
        }else if (App.getApp().getLanguage().equals("it")){
            context = LocaleHelper.setLocale(LoginActivity.this, "it");
            locale = new Locale("it");
            App.getApp().setLanguage("it");
        }
        else{
            context = LocaleHelper.setLocale(LoginActivity.this, "en");
            locale = new Locale("en");
            App.getApp().setLanguage("en");
        }
        App.getApp().setResources(context.getResources());
        if(!App.getApp().getUserName().equals("")){
            mUsername.setText(App.getApp().getUserName());
            if(!App.getApp().getPass().equals("")){
                mPassword.setText(App.getApp().getPass());
                mLoginPresenter.validateLogin(String.valueOf(mUsername.getText()), String.valueOf(mPassword.getText()));
            }
        }
        if(App.getApp().getDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    public void bindViews() {
        mUsername = findViewById(R.id.editText_username);
        mPassword = findViewById(R.id.editText_password);
        mRegister = findViewById(R.id.login_register_option);
        mIniciasesion = findViewById(R.id.button_inicia_sesion);
    }
}