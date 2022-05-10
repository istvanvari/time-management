package com.example.timeapp.ui.today;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.Repository.RepositoryImpl;
import com.example.timeapp.db.TaskEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TodayViewModel extends ViewModel {
    private final RepositoryImpl repository;
    private final MutableLiveData<String> mText;
    String TAG = "HomeViewModel";
    private MutableLiveData<List<TaskEntity>> tasks;
    private MutableLiveData<TaskEntity> task;

    public TodayViewModel() {
        repository = RepositoryImpl.getInstance();
        int rowCount = repository.getNumberOfRows();

        tasks = new MutableLiveData<>();
        task = new MutableLiveData<>();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        try {
            tasks.setValue(repository.getTasks().getValue());
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
    }

    public LiveData<List<TaskEntity>> getTodaysTasks() {
        mText.setValue("This is today fragment");
        tasks.setValue(repository.getTasksByDate(LocalDate.now()).getValue());
        return tasks;
    }
}