package com.example.timeapp.ui.today;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.db.TaskEntity;

import java.time.LocalDate;
import java.util.List;


public class TaskViewModel extends ViewModel {

    private final RepositoryImpl repository;
    String TAG = "HomeViewModel";
    private LiveData<List<TaskEntity>> tasks;
    private MutableLiveData<TaskEntity> task;

    public TaskViewModel() {
        repository = RepositoryImpl.getInstance();
        task = new MutableLiveData<>();
        tasks = repository.getTasksByDate(LocalDate.now().toString());
//        tasks = new MutableLiveData<>();
    }

    public LiveData<List<TaskEntity>> getTasks() {
        tasks = repository.getTasks();
        return tasks;
    }

    public LiveData<List<TaskEntity>> getTodayTasks() {

        return tasks;
    }

    public LiveData<TaskEntity> getTask(int id) {
        return repository.getTask(id);
    }

    public void addTask(TaskEntity task) {
        repository.addTask(task);
    }

    public void updateTask(TaskEntity task) {
        repository.updateTask(task);
    }

    public void deleteTask(TaskEntity task) {
        repository.deleteTask(task);
    }
}