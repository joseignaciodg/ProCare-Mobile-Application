package com.example.procare.main.pets;

import android.content.Context;

import java.util.List;
import com.example.procare.data.Pets;

public class PetsPresenter {
    private PetsView mPetsView;
    private PetsModel mPetsModel;

    PetsPresenter(PetsView petsView){
        this.mPetsView = petsView;
        mPetsModel = new PetsModel((Context) petsView);
    }

    public List<Pets> validateUserPets(Integer userId) {
        if(userId == null){
            mPetsView.noRegister();
        }
        return mPetsModel.getPets(userId);
    }

}
