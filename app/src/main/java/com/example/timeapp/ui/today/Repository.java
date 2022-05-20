package com.example.timeapp.ui.today;

import androidx.lifecycle.LiveData;

import com.example.timeapp.db.TaskEntity;

import java.util.List;

public interface Repository {
    LiveData<List<TaskEntity>> getTasks();
    LiveData<TaskEntity> getTask(int id);
    LiveData<List<TaskEntity>> getTasksByDate(String date);
    void addTask(TaskEntity task);
    void deleteTask(TaskEntity task);
    void updateTask(TaskEntity task);
}
