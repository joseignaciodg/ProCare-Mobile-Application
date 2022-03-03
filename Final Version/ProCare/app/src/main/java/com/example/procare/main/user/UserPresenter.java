package com.example.procare.main.user;

import android.content.Context;

public class UserPresenter {
    private UserView mUserView;
    private UserModel mUserModel;

    UserPresenter(UserView view){
        mUserView = view;
        mUserModel = new UserModel((Context) mUserView);
    }

    public boolean validateName(String username, String newusername){
        if (username != null && !newusername.isEmpty()){
            // Upload DB
            mUserModel.setName(username, newusername);
            mUserView.changeSuccessful();
            return true;
        }
        mUserView.fillField();
        return false;
    }
}
