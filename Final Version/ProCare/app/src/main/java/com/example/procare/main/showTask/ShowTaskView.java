package com.example.procare.main.showTask;

import android.view.View;

public interface ShowTaskView {
    void removeTask(View view);
    void removeTaskSuccess();
    void removeTaskFail();
    void editTask(View view);
    void changeTaskState(View view);
    void changeTaskStateSuccess();
    void changeTaskStateFail();
}
