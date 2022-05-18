package com.example.timeapp.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.timeapp.db.RoutineEntity;

import java.util.List;

public class RoutineDiffUtilCallbacks extends DiffUtil.Callback {

    private List<RoutineEntity> oldList;
    private List<RoutineEntity> newList;

    public RoutineDiffUtilCallbacks(List<RoutineEntity> oldList, List<RoutineEntity> newList) {
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
        RoutineEntity oldRoutine = oldList.get(oldItemPosition);
        RoutineEntity newRoutine = newList.get(newItemPosition);

        Bundle diff = new Bundle();

        if (!oldRoutine.getName().equals(newRoutine.getName())) {
            diff.putString("routineName", newRoutine.getName());
        }

        if (!oldRoutine.getDescription().equals(newRoutine.getDescription())) {
            diff.putString("routineDescription", newRoutine.getDescription());
        }

        if (!oldRoutine.getTime().equals(newRoutine.getTime())) {
            diff.putString("routineTime", newRoutine.getTime());
        }

        if (oldRoutine.getDay() != newRoutine.getDay()) {
            diff.putInt("routineDay", newRoutine.getDay());
        }

        if (diff.size() == 0) {
            return null;
        }

        return diff;
    }
}
