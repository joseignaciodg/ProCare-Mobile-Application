package com.example.procare.main.register;

public interface RegisterView {
    void registerSuccessful();
    void registerFailureUsername();
    void registerFailurePasswords();
    void fillFields();
    void bindViews();
    //Future work add method to improve security passwords longer, etc
}
