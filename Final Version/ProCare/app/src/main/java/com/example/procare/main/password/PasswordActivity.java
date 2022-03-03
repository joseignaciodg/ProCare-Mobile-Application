package com.example.procare.main.password;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import procare.R;
import com.example.procare.data.App;

public class PasswordActivity extends AppCompatActivity implements PasswordView {
    private PasswordPresenter mPasswordPresenter;
    private EditText mOldPW, mNewPW1, mNewPW2;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mOldPW = findViewById(R.id.old_pw);
        mNewPW1 = findViewById(R.id.new_pw1);
        mNewPW2 = findViewById(R.id.new_pw2);

        mOldPW.setHint(App.getApp().getResources().getString(R.string.old_password_hint));
        mNewPW1.setHint(App.getApp().getResources().getString(R.string.new_password_hint));
        mNewPW2.setHint(App.getApp().getResources().getString(R.string.repeat_new_password_hint));
        Button button = findViewById(R.id.button_pw);
        button.setText(App.getApp().getResources().getString(R.string.password_confirm_button));
        TextView title = findViewById(R.id.change_password_toolbar_title);
        title.setText(App.getApp().getResources().getString(R.string.password_activity_title));

        mPasswordPresenter = new PasswordPresenter(this);

        sp = getSharedPreferences("preferences", MODE_PRIVATE);
    }

    @Override
    public void confirmPassword(View view){
        String mUsername = sp.getString("username", null);

        String mopw = mOldPW.getText().toString();
        String mnpw1 = mNewPW1.getText().toString();
        String mnpw2 = mNewPW2.getText().toString();

        if (mPasswordPresenter.validatePasswords(mUsername, mopw, mnpw1, mnpw2))
            finish();
    }

    @Override
    public void fillField() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.password_fill_field), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void incorrectPassword(){
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.password_incorrect_password), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void noPasswordMatch(){
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.password_no_match), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void changeSuccessful(){
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.password_change_successful), Toast.LENGTH_LONG);
        toast.show();
    }

    public void goBack (View view){
        finish();
    }
}