package com.example.timeapp.ui.routines;
/*
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.App;
import com.example.timeapp.db.RoutineEntity;
import com.example.timeapp.db.TaskDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoutineRepository {
    private static RoutineRepository instance;
    private final TaskDatabase taskDatabase;
    private final ExecutorService executor;
    private LiveData<List<RoutineEntity>> routines;
    private LiveData<RoutineEntity> routine;

    private RoutineRepository() {
        taskDatabase = App.getDatabase();
        executor = Executors.newSingleThreadExecutor();
        routines = new MutableLiveData<>();
        routine = new MutableLiveData<>();
    }

    public static RoutineRepository getInstance() {
        if (instance == null)
            instance = new RoutineRepository();
        return instance;
    }

    
    public LiveData<List<RoutineEntity>> getRoutines() {
        routines = taskDatabase.routineDao().getAllRoutines();
        return routines;
    }

    
    public LiveData<List<RoutineEntity>> getRoutinesByDay(int day) {
        routines = taskDatabase.routineDao().getRoutinesByDay(day);
        return routines;
    }

    
    public LiveData<RoutineEntity> getRoutineById(int id) {
        routine = taskDatabase.routineDao().getRoutineById(id);
        return routine;
    }

    
    public void addRoutine(RoutineEntity routine) {
        executor.execute(() -> taskDatabase.routineDao().insertRoutine(routine));
    }

    
    public void deleteRoutine(RoutineEntity routine) {
        executor.execute(() -> taskDatabase.routineDao().deleteRoutine(routine));
    }

    
    public void updateRoutine(RoutineEntity routine) {
        executor.execute(() -> taskDatabase.routineDao().updateRoutine(routine));
    }
}


 */
