package com.example.procare.main.newTask;


import android.view.View;

public interface NewTaskView {
    void showDatePickerDialog(View view);
    void confirmNewTask(View view);
    void confirmUpdateTask(View view);
    void fillFields();
    void returnFromNewTask(int result);
    void newTaskFail();
}
