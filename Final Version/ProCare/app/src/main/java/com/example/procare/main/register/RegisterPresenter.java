package com.example.procare.main.register;

import android.content.Context;


import com.example.procare.data.User;

public class RegisterPresenter {
    private RegisterView mRegisterView;
    private RegisterModel mRegisterModel;

    RegisterPresenter(RegisterView registerView){
        mRegisterModel = new RegisterModel((Context) registerView);
        mRegisterView = registerView;
    }

    public void validateRegister(String name, String username, String password, String passwordRepeat){
        User u;

        if(username.length() == 0 || password.length() == 0 || passwordRepeat.length() == 0){
            mRegisterView.fillFields();
        }else if(mRegisterModel.getUsername(username)) mRegisterView.registerFailureUsername();
        else if(!password.equals(passwordRepeat)) mRegisterView.registerFailurePasswords();
        else {
            if(mRegisterModel.registerUser(name, username, password)) mRegisterView.registerSuccessful();
        }
    }
}
