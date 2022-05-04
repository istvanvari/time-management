package com.example.timeapp.ui.home;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.Repository.RepositoryImpl;
import com.example.timeapp.db.TaskEntity;

import java.util.ArrayList;
import java.util.List;


public class HomeViewModel extends ViewModel {
    String TAG = "HomeViewModel";
    private final MutableLiveData<String> mText;
    private final RepositoryImpl repository;
    private MutableLiveData<List<TaskEntity>> tasks;
    private MutableLiveData<TaskEntity> task;

    public HomeViewModel() {
        repository = RepositoryImpl.getInstance();

        tasks = new MutableLiveData<>();
        task = new MutableLiveData<>();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        try {
            tasks = repository.getTasks();
        } catch (Exception e) {
            tasks.setValue(new ArrayList<>());
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<TaskEntity>> getTasks() {
        return tasks;
    }

    public void addTask(TaskEntity task) {
        repository.addTask(task);
        tasks.setValue(repository.getTasks().getValue());
        Log.d(TAG, "addTask: " + tasks.getValue().size());
    }
}