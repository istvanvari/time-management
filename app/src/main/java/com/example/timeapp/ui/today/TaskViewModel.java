package com.example.timeapp.ui.today;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.Repository.RepositoryImpl;
import com.example.timeapp.db.TaskEntity;

import java.util.List;


public class TaskViewModel extends ViewModel {
    private final RepositoryImpl repository;
    private final MutableLiveData<String> mText;
    String TAG = "HomeViewModel";
    private LiveData<List<TaskEntity>> tasks;
    private MutableLiveData<TaskEntity> task;

    public TaskViewModel() {
        repository = RepositoryImpl.getInstance();

        task = new MutableLiveData<>();
        tasks = repository.getTasks();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<TaskEntity>> getTasks() {
        return tasks;
    }

    public LiveData<TaskEntity> getTask(int id) {
        return repository.getTask(id);
    }

    public void addTask(TaskEntity task) {
        repository.addTask(task);
    }

    public LiveData<List<TaskEntity>> getTodaysTasks() {
        mText.setValue("This is today fragment");
        return tasks;
    }

    public void updateTask(TaskEntity task) {
        repository.updateTask(task);
    }
}