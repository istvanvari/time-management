package com.example.timeapp.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.db.TaskEntity;

import java.time.LocalDate;
import java.util.List;

public interface Repository {
    MutableLiveData<List<TaskEntity>> getTasks();
    MutableLiveData<TaskEntity> getTask(int id);
    MutableLiveData<TaskEntity> getTasksByDate(LocalDate Date);
    void addTask(TaskEntity task);
    void deleteTask(TaskEntity task);
    void updateTask(TaskEntity task);
}
