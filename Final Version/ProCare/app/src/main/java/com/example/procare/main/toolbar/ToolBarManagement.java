package com.example.procare.main.toolbar;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import procare.R;
import com.example.procare.data.App;
import com.example.procare.main.calendar.CalendarActivity;
import com.example.procare.main.pets.PetsActivity;
import com.example.procare.main.settings.SettingsActivity;
import com.example.procare.main.upcoming.UpcomingActivity;
import com.example.procare.main.user.UserActivity;

public class ToolBarManagement extends AppCompatActivity {
     public void launchFromToolbar(View view){
               Intent intent;
               switch (view.getId()){
                    case R.id.button_toolbar_upcoming:
                         intent = new Intent(view.getContext(), UpcomingActivity.class);
                         break;
                    case R.id.button_toolbar_user:
                         intent = new Intent(view.getContext(), UserActivity.class);
                         break;
                    case R.id.button_toolbar_calendar:
                         intent = new Intent(view.getContext(), CalendarActivity.class);
                         break;
                    case R.id.button_toolbar_pets:
                         intent = new Intent(view.getContext(), PetsActivity.class);
                         break;
                    default:
                         intent = new Intent(view.getContext(), SettingsActivity.class);
                         break;
               }
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
     }

     public void setUpToolbar(int id){
         switch (id){
             case R.id.button_toolbar_upcoming:
                 findViewById(R.id.button_toolbar_upcoming).getBackground().setTint(App.getApp().getResources().getColor(R.color.white));
                 break;
             case R.id.button_toolbar_user:
                 findViewById(R.id.button_toolbar_user).getBackground().setTint(App.getApp().getResources().getColor(R.color.white));
                 break;
             case R.id.button_toolbar_calendar:
                 findViewById(R.id.button_toolbar_calendar).getBackground().setTint(App.getApp().getResources().getColor(R.color.white));
                 break;
             case R.id.button_toolbar_pets:
                 findViewById(R.id.button_toolbar_pets).getBackground().setTint(App.getApp().getResources().getColor(R.color.white));
                 break;
             default:
                 findViewById(R.id.button_toolbar_settings).getBackground().setTint(App.getApp().getResources().getColor(R.color.white));
                 break;
         }
         if(id!=R.id.button_toolbar_pets){
             findViewById(R.id.button_toolbar_pets).getBackground().setTint(App.getApp().getResources().getColor(R.color.iconColor));
         }
         if(id!=R.id.button_toolbar_settings){
             findViewById(R.id.button_toolbar_settings).getBackground().setTint(App.getApp().getResources().getColor(R.color.iconColor));
         }
         if(id!=R.id.button_toolbar_calendar){
             findViewById(R.id.button_toolbar_calendar).getBackground().setTint(App.getApp().getResources().getColor(R.color.iconColor));
         }
         if(id!=R.id.button_toolbar_upcoming){
             findViewById(R.id.button_toolbar_upcoming).getBackground().setTint(App.getApp().getResources().getColor(R.color.iconColor));
         }
         if(id!=R.id.button_toolbar_user){
             findViewById(R.id.button_toolbar_user).getBackground().setTint(App.getApp().getResources().getColor(R.color.iconColor));
         }
     }

}
