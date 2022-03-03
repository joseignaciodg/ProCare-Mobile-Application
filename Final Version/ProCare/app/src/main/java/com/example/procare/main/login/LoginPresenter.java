package com.example.procare.main.login;

import android.content.Context;

public class LoginPresenter {
    private LoginModel mLoginModel;
    private LoginView mLoginView;

    LoginPresenter(LoginView loginView){
        this.mLoginView = loginView;
        mLoginModel = new LoginModel((Context) loginView);
    }

    public void validateLogin(String username, String password){
        if(mLoginModel.validateLogin(username, password)) mLoginView.loginSuccessfull();
        else mLoginView.loginFailure();
    }

    public void launchRegister() {
        mLoginView.launchRegister();
    }
}
