package com.example.timeapp.ui.today;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.db.TaskEntity;

import java.time.LocalDate;
import java.util.List;


public class TaskViewModel extends ViewModel {

    private final TaskRepository repository;
    String TAG = "HomeViewModel";
    private MutableLiveData<LocalDate> day = new MutableLiveData<>();
    private LiveData<List<TaskEntity>> tasks;
    private MutableLiveData<TaskEntity> task;

    public TaskViewModel() {
        repository = TaskRepository.getInstance();
        task = new MutableLiveData<>();
        tasks = repository.getTasksByDate(LocalDate.now().toString());
    }

    public LiveData<List<TaskEntity>> getTasks() {
        tasks = repository.getTasks();
        return tasks;
    }

    public LiveData<List<TaskEntity>> getTasksByDate(String date) {
        return repository.getTasksByDate(date);
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

    public LiveData<List<TaskEntity>> getTasksSorted() {
        return repository.getTasksSorted();
    }

    public LiveData<LocalDate> getDay() {
        return day;
    }

    public void setDay(LocalDate now) {
        this.day.setValue(now);
    }

}