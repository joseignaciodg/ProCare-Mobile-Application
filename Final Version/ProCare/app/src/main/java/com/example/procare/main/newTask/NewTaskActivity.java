package com.example.procare.main.newTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import procare.R;
import com.example.procare.data.App;
import com.example.procare.data.Task;

public class NewTaskActivity extends AppCompatActivity implements NewTaskView {

    private NewTaskPresenter mNewTaskPresenter;
    private Task task;
    private EditText mTaskName, mTaskDesc, mScheduleDate;
    private Spinner mPetSpinner, mFreqSpinner;
    private TimePicker mScheduleTime;
    private Button buttonConfirmTask;
    private String date;
    private Integer hour;
    private Integer minutes;
    private TextView mTaskTitle;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mNewTaskPresenter = new NewTaskPresenter(this);
        task = (Task) getIntent().getSerializableExtra("task");

        bindViews();

        buttonConfirmTask.setText(App.getApp().getResources().getString(R.string.new_task_button));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(task == null){
            Calendar calendar = Calendar.getInstance();
            String date = dateFormat.format(calendar.getTime());

            mTaskTitle.setText(App.getApp().getResources().getString(R.string.toolbar_label_show_task));

            mScheduleDate.setText(date);
            mScheduleDate.setOnClickListener(view -> {showDatePickerDialog(view); });
            mScheduleTime.setIs24HourView(true);
            mScheduleTime.setHour(calendar.getTime().getHours());
            mScheduleTime.setMinute(calendar.getTime().getMinutes());

            String[] options = mNewTaskPresenter.getPetNames(App.getApp().getUser());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
            mPetSpinner.setAdapter(adapter);

            options = App.getApp().getResources().getStringArray(R.array.task_frequency_array);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
            mFreqSpinner.setAdapter(adapter);

            buttonConfirmTask.setText(App.getApp().getResources().getString(R.string.new_task_button));
            buttonConfirmTask.setOnClickListener(v -> confirmNewTask(v) );
        } else {
            mTaskName.setText(task.getmTaskName());
            mTaskDesc.setText(task.getmDescription());
            mScheduleTime.setIs24HourView(true);
            mScheduleTime.setHour(task.getmScheduleDatetime().getHours());
            mScheduleTime.setMinute(task.getmScheduleDatetime().getMinutes());
            mScheduleDate.setText(dateFormat.format(task.getmScheduleDatetime()));

            buttonConfirmTask.setText(App.getApp().getResources().getString(R.string.modificar_nueva_tarea));

            buttonConfirmTask.setOnClickListener(v -> confirmUpdateTask(v));

            mPetSpinner = findViewById(R.id.petSpinner);
            String[] options = mNewTaskPresenter.getPetNames(App.getApp().getUser());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
            mPetSpinner.setAdapter(adapter);
            mPetSpinner.setSelection(mNewTaskPresenter.getPetPosition(App.getApp().getUser(), task.getmPetId()));
            mPetSpinner.setEnabled(false);

            String[] options2 = App.getApp().getResources().getStringArray(R.array.task_frequency_array);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options2);
            mFreqSpinner.setAdapter(adapter2);
            mFreqSpinner.setSelection(task.getmFreq());
        }
    }

    @Override
    public void showDatePickerDialog(View view) {
        NewTaskModel.DatePickerFragment newFragment = NewTaskModel.DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            String selectedDate = "";

            if(day < 10)
                selectedDate = "0";
            selectedDate += day + "/";
            if(month < 9)
                selectedDate += "0";
            selectedDate += (month + 1) + "/" + year;

            mScheduleDate.setText(selectedDate);
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void confirmNewTask(View view){
        String name = mTaskName.getText().toString();
        String desc = mTaskDesc.getText().toString();
        String petName = mPetSpinner.getSelectedItem().toString();
        date = mScheduleDate.getText().toString();
        hour = mScheduleTime.getHour();
        minutes = mScheduleTime.getMinute();
        Integer freq = mFreqSpinner.getSelectedItemPosition();
        mNewTaskPresenter.validateNewTask(name, desc, petName, date, hour, minutes, App.getApp().getUser(), freq);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void confirmUpdateTask(View view) {
        String name = mTaskName.getText().toString();
        String desc = mTaskDesc.getText().toString();
        String petName = mPetSpinner.getSelectedItem().toString();
        Integer freq = mFreqSpinner.getSelectedItemPosition();
        date = mScheduleDate.getText().toString();
        hour = mScheduleTime.getHour();
        minutes = mScheduleTime.getMinute();
        mNewTaskPresenter.validateUpdateTask(task.getmId(), name, desc, petName, date, hour, minutes, App.getApp().getUser(), freq);
    }

    @Override
    public void fillFields() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.toast_fill_fields), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void returnFromNewTask(int result) {
        App.getApp();
        Intent intent = new Intent(NewTaskActivity.this, AlarmBroadcastReceiver.class);
        intent.putExtra("taskId", result);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NewTaskActivity.this, result, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, getDateFromStr().getTime(), pendingIntent);

        setResult(result, intent);
        finish();
    }

    @Override
    public void newTaskFail() {
        Toast toast = Toast.makeText(this, App.getApp().getResources().getString(R.string.toast_error), Toast.LENGTH_LONG);
        toast.show();
    }

    private Date getDateFromStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateStr = date + " ";
        if(hour < 10)
            dateStr += "0";
        dateStr += hour + ":";
        if(minutes < 10)
            dateStr += "0";
        dateStr += minutes;
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void goBack (View view){
        finish();
    }

    private void bindViews() {
        mTaskTitle = findViewById(R.id.new_task_toolbar_title);
        mTaskName = findViewById(R.id.editTextTaskName);
        mTaskDesc = findViewById(R.id.editTextTaskDesc);
        mPetSpinner = findViewById(R.id.petSpinner);
        mFreqSpinner = findViewById(R.id.freqSpinner);
        mScheduleDate = findViewById(R.id.scheduleDate);
        mScheduleTime = findViewById(R.id.scheduleTime);
        buttonConfirmTask = findViewById(R.id.buttonConfirmTask);
        EditText editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskName.setHint(App.getApp().getResources().getString(R.string.new_task_name_hint));

        EditText editTextTaskDesc = findViewById(R.id.editTextTaskDesc);
        editTextTaskDesc.setHint(App.getApp().getResources().getString(R.string.new_task_desc_hint));

    }
}
