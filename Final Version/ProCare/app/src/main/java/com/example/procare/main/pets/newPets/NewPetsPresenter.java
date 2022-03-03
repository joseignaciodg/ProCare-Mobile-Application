package com.example.procare.main.pets.newPets;

import android.content.Context;

public class NewPetsPresenter  {
    private NewPetsView mNewPetsView;
    private NewPetsModel mNewPetsModel;

    NewPetsPresenter(NewPetsView newPetsView){
        this.mNewPetsView = newPetsView;
        mNewPetsModel = new NewPetsModel((Context) newPetsView);
    }

    public void validateNewPet(String name, String type, Integer userId) {
        if(name.length() == 0 || type.length() == 0) mNewPetsView.fillField();
        else {
            if(mNewPetsModel.saveNewPet(name, type, userId)){
                mNewPetsView.NewPetSuccessful();
            }
            else{
                mNewPetsView.NewPetError();
            }

        }
    }
}
