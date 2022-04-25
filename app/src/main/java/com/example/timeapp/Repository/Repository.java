package com.example.timeapp.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.misc.Task;

import java.util.List;

public interface Repository {
    MutableLiveData<List<Task>> getTasks();
    MutableLiveData<Task> getTask(int id);
    void addTask(Task task);
    void deleteTask(Task task);
    void updateTask(Task task);
}
