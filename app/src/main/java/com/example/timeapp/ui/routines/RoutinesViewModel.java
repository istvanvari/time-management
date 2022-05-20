package com.example.timeapp.ui.routines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.ui.today.TaskRepository;

import java.util.List;

public class RoutinesViewModel extends ViewModel {
    private LiveData<List<TaskEntity>> repeatedTasks;
    private TaskRepository repository;

    public RoutinesViewModel() {
        repository = repository.getInstance();
        repeatedTasks = repository.getRepeatedTasks();
    }

    public LiveData<List<TaskEntity>> getRepeatedTasks() {
        return repeatedTasks;
    }


    public LiveData<TaskEntity> getTaskById(int id) {
        return repository.getTask(id);
    }

    public LiveData<List<TaskEntity>> getAllTasks() {
        return repository.getTasks();
    }


    public void addTask(TaskEntity routine) {
        repository.addTask(routine);
    }

    public void deleteTask(TaskEntity routine) {
        repository.deleteTask(routine);
    }

    public void updateTask(TaskEntity routine) {
        repository.updateTask(routine);
    }

}
