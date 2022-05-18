package com.example.timeapp.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.timeapp.db.TaskEntity;

import java.util.List;

public class DiffUtilCallbacks extends DiffUtil.Callback {

    private List<TaskEntity> oldList;
    private List<TaskEntity> newList;

    public DiffUtilCallbacks(List<TaskEntity> oldList, List<TaskEntity> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compareTo(newList.get(newItemPosition)) == 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        TaskEntity oldTask = oldList.get(oldItemPosition);
        TaskEntity newTask = newList.get(newItemPosition);

        Bundle diff = new Bundle();

        if (!oldTask.getTaskName().equals(newTask.getTaskName())) {
            diff.putString("taskName", newTask.getTaskName());
        }

        if (!oldTask.getTaskDescription().equals(newTask.getTaskDescription())) {
            diff.putString("taskDescription", newTask.getTaskDescription());
        }

        if (!oldTask.getTaskTime().equals(newTask.getTaskTime())) {
            diff.putString("taskTime", newTask.getTaskTime().toString());
        }

        if (diff.size() == 0) {
            return null;
        }

        return diff;
    }

}

