package com.example.procare.main.password;

import android.content.Context;

public class PasswordPresenter {
    private PasswordView mPasswordView;
    private PasswordModel mPasswordModel;

    public PasswordPresenter(PasswordView view) {
        mPasswordView = view;
        mPasswordModel = new PasswordModel((Context) mPasswordView);
    }

    public boolean validatePasswords(String username, String opw, String npw1, String npw2) {
        if (opw.isEmpty() || npw1.isEmpty() || npw2.isEmpty()) {
            mPasswordView.fillField();
            return false;
        }
        if (!mPasswordModel.validatePW(username, opw)) {
            mPasswordView.incorrectPassword();
            return false;
        }
        if (!npw1.equals(npw2)) {
            mPasswordView.noPasswordMatch();
            return false;
        }
        mPasswordModel.changePassword(username, npw1);
        mPasswordView.changeSuccessful();
        return true;

    }
}
