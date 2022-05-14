package com.example.timeapp.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.App;
import com.example.timeapp.db.TaskDatabase;
import com.example.timeapp.db.TaskEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepositoryImpl implements Repository {
    static String TAG = "RepositoryImpl";
    private static RepositoryImpl instance;
    private final TaskDatabase taskDatabase;
    private final ExecutorService executor;
    private  LiveData<List<TaskEntity>> tasks;
    private final MutableLiveData<TaskEntity> task;

    public RepositoryImpl() {
        executor = Executors.newSingleThreadExecutor();
        Log.d(TAG, "RepositoryImpl: constructor");
        this.taskDatabase = App.getDatabase();
        this.tasks = taskDatabase.taskDao().getAllTasks();
        this.task = new MutableLiveData<>();
    }

    public static RepositoryImpl getInstance() {
        if (instance == null) {
            instance = new RepositoryImpl();
            Log.d(TAG, "getInstance: get instance");
        }
        return instance;
    }

    @Override
    public LiveData<List<TaskEntity>> getTasks() {
        Log.d(TAG, "getTasks: ");
        return tasks;
    }

    @Override
    public MutableLiveData<TaskEntity> getTask(int id) {
        task.setValue(taskDatabase.taskDao().getTaskById(id));
        return task;
    }


    @Override
    public LiveData<List<TaskEntity>> getTasksByDate(LocalDate Date) {
        tasks = taskDatabase.taskDao().getTasksByDate(Date.toString());
        return tasks;
    }

    @Override
    public void addTask(TaskEntity task) {
        taskDatabase.taskDao().insertTask(task);
        Log.d(TAG, "addTask: ");
    }

    @Override
    public void deleteTask(TaskEntity task) {
        executor.execute(() -> taskDatabase.taskDao().deleteTask(task));
    }

    @Override
    public void updateTask(TaskEntity task) {
        executor.execute(() -> taskDatabase.taskDao().updateTask(task));
    }

}
