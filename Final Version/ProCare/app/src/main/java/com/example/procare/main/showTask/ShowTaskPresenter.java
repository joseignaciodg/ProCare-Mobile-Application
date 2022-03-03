package com.example.procare.main.showTask;

import android.content.Context;

import com.example.procare.data.Task;

public class ShowTaskPresenter {
    private ShowTaskView mShowTaskView;
    private ShowTaskModel mShowTaskModel;

    public ShowTaskPresenter(ShowTaskView view) {
        this.mShowTaskView = view;
        this.mShowTaskModel = new ShowTaskModel((Context) mShowTaskView);
    }

    public Task getTaskById(Integer taskId) {
        return mShowTaskModel.getTaskById(taskId);
    }

    public void removeTask(Integer taskId) {
        if (mShowTaskModel.removeTask(taskId) > 0)
            mShowTaskView.removeTaskSuccess();
        else
            mShowTaskView.removeTaskFail();

    }

    public String getPetName(Integer petId) {
        return mShowTaskModel.getPetName(petId);
    }

    public void changeTaskState(Integer taskId) {
        if(mShowTaskModel.changeTaskState(taskId) > 0)
            mShowTaskView.changeTaskStateSuccess();
        else
            mShowTaskView.changeTaskStateFail();
    }
}
