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

        if (!oldTask.getName().equals(newTask.getName())) {
            diff.putString("taskName", newTask.getName());
        }

        if (!oldTask.getDescription().equals(newTask.getDescription())) {
            diff.putString("taskDescription", newTask.getDescription());
        }

        if (!oldTask.getTime().equals(newTask.getTime())) {
            diff.putString("taskTime", newTask.getTime().toString());
        }

        if (oldTask.isRepeated() != newTask.isRepeated()) {
            diff.putBoolean("repeated", newTask.isRepeated());
        }

        if (diff.size() == 0) {
            return null;
        }

        return diff;
    }

}


