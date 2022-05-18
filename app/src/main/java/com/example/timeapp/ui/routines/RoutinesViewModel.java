package com.example.timeapp.ui.routines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.db.RoutineEntity;

import java.util.List;

public class RoutinesViewModel extends ViewModel {
    private LiveData<List<RoutineEntity>> routines;
    private LiveData<RoutineEntity> routine;
    private RoutineRepositoryImpl repository;

    public RoutinesViewModel() {
        repository = RoutineRepositoryImpl.getInstance();
//        routines = new MutableLiveData<>();
        routines = repository.getRoutines();

    }

    public LiveData<List<RoutineEntity>> getRoutines() {
        return routines;
    }


    public LiveData<RoutineEntity> getRoutineById(int id) {
        routine = repository.getRoutineById(id);
        return routine;
    }


    public void addRoutine(RoutineEntity routine) {
        repository.addRoutine(routine);
    }

    public void deleteRoutine(RoutineEntity routine) {
        repository.deleteRoutine(routine);
    }

    public void updateRoutine(RoutineEntity routine) {
        repository.updateRoutine(routine);
    }

}