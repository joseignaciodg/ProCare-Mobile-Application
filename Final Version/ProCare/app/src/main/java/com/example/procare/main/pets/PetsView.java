package com.example.procare.main.pets;

public interface PetsView {
    void addNewPet();
    void viewPet(String name,String type, Integer id);
    void noRegister();
    void bindViews();
}
