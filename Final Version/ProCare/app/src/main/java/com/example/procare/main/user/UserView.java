package com.example.procare.main.user;

import android.view.View;

public interface UserView {
    void logout(View view);
    void changeName(View view);
    void confirmName(View view);
    void fillField();
    void changeSuccessful();
    void bindViews();
}
