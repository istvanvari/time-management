package com.example.timeapp.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.MainActivity;
import com.example.timeapp.db.TaskDatabase;
import com.example.timeapp.db.TaskEntity;

import java.time.LocalDate;
import java.util.List;

public class RepositoryImpl implements Repository {
    private static RepositoryImpl instance;
    String TAG = "RepositoryImpl";
    private final TaskDatabase taskDatabase;


    private final MutableLiveData<List<TaskEntity>> tasks;

    public RepositoryImpl() {
        this.taskDatabase = MainActivity.getTaskDatabase();
        this.tasks = new MutableLiveData<>();
    }

    public static RepositoryImpl getInstance() {
        if (instance == null) {
            instance = new RepositoryImpl();
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
    public MutableLiveData<TaskEntity> getTasksByDate(LocalDate Date) {
        return null;
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
}
