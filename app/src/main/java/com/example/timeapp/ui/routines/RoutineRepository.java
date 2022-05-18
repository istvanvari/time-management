package com.example.timeapp.ui.routines;

import androidx.lifecycle.LiveData;

import com.example.timeapp.db.RoutineEntity;

import java.util.List;

public interface RoutineRepository {
    LiveData<List<RoutineEntity>> getRoutines();
    LiveData<List<RoutineEntity>> getRoutinesByDay(int day);
    LiveData<RoutineEntity> getRoutineById(int id);
    void addRoutine(RoutineEntity routine);
    void deleteRoutine(RoutineEntity routine);
    void updateRoutine(RoutineEntity routine);
}
