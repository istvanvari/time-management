package com.example.timeapp.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.App;
import com.example.timeapp.db.TaskDatabase;
import com.example.timeapp.db.TaskEntity;

import java.time.LocalDate;
import java.util.List;

public class RepositoryImpl implements Repository {
    private static RepositoryImpl instance;
    private final TaskDatabase taskDatabase;
    private final MutableLiveData<List<TaskEntity>> tasks;
    String TAG = "RepositoryImpl";

    public RepositoryImpl() {
        Log.d("repo", "RepositoryImpl: constructor");
        this.taskDatabase = App.getDatabase();
        this.tasks = new MutableLiveData<>();
    }

    public static RepositoryImpl getInstance() {
        if (instance == null) {
            instance = new RepositoryImpl();
            Log.d("repo", "getInstance: get instance");
        }
        return instance;
    }

    @Override
    public MutableLiveData<List<TaskEntity>> getTasks() {
        tasks.setValue(taskDatabase.taskDao().getAllTasks());
        Log.d(TAG, "getTasks: " + tasks.getValue().size());
        return tasks;
    }

    @Override
    public MutableLiveData<TaskEntity> getTask(int id) {
        return null;
    }


    @Override
    public MutableLiveData<List<TaskEntity>> getTasksByDate(LocalDate Date) {
        tasks.setValue(taskDatabase.taskDao().getTasksByDate(Date.toString()));
        return tasks;
    }

    @Override
    public void addTask(TaskEntity task) {
        taskDatabase.taskDao().insertTask(task);
        Log.d(TAG, "addTask: ");
    }

    @Override
    public void deleteTask(TaskEntity task) {

    }

    @Override
    public void updateTask(TaskEntity task) {

    }

    public int getNumberOfRows() {
        return taskDatabase.taskDao().getNumberOfRows();
    }
}
