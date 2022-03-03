package com.example.procare.main.pets.profilePet;

public interface ProfilePetView {
    void editPet(Integer id);
    void EditPetSuccessful();
    void DeletePetSuccessful();
    void DeletePetError();
    void EditPetError();
    void fillField();
    void deletePet(Integer id);
}
