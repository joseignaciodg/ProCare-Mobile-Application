package com.example.procare.main.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import procare.R;
import com.example.procare.data.App;
import com.example.procare.main.login.LoginActivity;
import com.example.procare.main.pets.PetsActivity;

public class RegisterActivity extends AppCompatActivity implements RegisterView{
    private RegisterPresenter mRegisterPresenter;
    private Button mRegistrarUsuario;
    private EditText mName, mUsername, mPassword, mPasswordRepeat;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterPresenter = new RegisterPresenter(this);

        bindViews();
        mRegistrarUsuario.setText(App.getApp().getResources().getString(R.string.iniciar_sesion_button));
        mRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterPresenter.validateRegister(String.valueOf(mName.getText()), String.valueOf(mUsername.getText()), String.valueOf(mPassword.getText()), String.valueOf(mPasswordRepeat.getText())) ;
            }
        });
        sp = getSharedPreferences("preferences", MODE_PRIVATE);
    }

    @Override
    public void registerSuccessful() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", App.getApp().getUser().getmUsername());
        editor.putString("name", App.getApp().getUser().getmName());
        editor.apply();

        Intent intent = new Intent(this, PetsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void registerFailureUsername() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.user_used), Toast.LENGTH_LONG);
        toast.show();
        mUsername.setText("");
    }

    @Override
    public void registerFailurePasswords() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.not_same_passwords), Toast.LENGTH_LONG);
        toast.show();
        mPassword.setText("");
        mPasswordRepeat.setText("");
    }

    @Override
    public void fillFields() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.fill_all_fields), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void bindViews() {
        mName = findViewById(R.id.editText_register_name);
        mUsername = findViewById(R.id.editText_register_username);
        mPassword = findViewById(R.id.editText_register_password);
        mPasswordRepeat = findViewById(R.id.editText_register_password_repeat);
        mRegistrarUsuario = findViewById(R.id.button_registrar_usuario);
    }
}