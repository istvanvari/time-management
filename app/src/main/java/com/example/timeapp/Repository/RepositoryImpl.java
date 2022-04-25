package com.example.timeapp.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.misc.Task;

import java.util.List;

public class RepositoryImpl implements Repository {
    @Override
    public MutableLiveData<List<Task>> getTasks() {
        return null;
    }

    @Override
    public MutableLiveData<Task> getTask(int id) {
        return null;
    }

    @Override
    public void addTask(Task task) {

    }

    @Override
    public void deleteTask(Task task) {

    }

    @Override
    public void updateTask(Task task) {

    }
}
