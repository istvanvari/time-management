package com.example.timeapp.ui.today;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.timeapp.App;
import com.example.timeapp.db.TaskDatabase;
import com.example.timeapp.db.TaskEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    static String TAG = "RepositoryImpl";
    private static TaskRepository instance;
    private final TaskDatabase taskDatabase;
    private final ExecutorService executor;
    private final MutableLiveData<TaskEntity> task;
    private LiveData<List<TaskEntity>> tasks;

    public TaskRepository() {
        executor = Executors.newSingleThreadExecutor();
        Log.d(TAG, "RepositoryImpl: constructor");
        this.taskDatabase = App.getDatabase();
        this.tasks = new MutableLiveData<>();
        this.task = new MutableLiveData<>();
    }

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
            Log.d(TAG, "getInstance: get instance");
        }
        return instance;
    }


    public LiveData<List<TaskEntity>> getTasks() {
        tasks = taskDatabase.taskDao().getAllTasks();
        Log.d(TAG, "getTasks: ");
        return tasks;
    }

    public LiveData<List<TaskEntity>> getRepeatedTasks() {
        return taskDatabase.taskDao().getRepeatedTasks();
    }


    public LiveData<TaskEntity> getTask(int id) {
        task.setValue(taskDatabase.taskDao().getTaskById(id));
        return task;
    }


    public LiveData<List<TaskEntity>> getTasksByDate(String date) {
        tasks = taskDatabase.taskDao().getTasksByDate(date);
        return tasks;
    }


    public void addTask(TaskEntity task) {
        executor.execute(() -> taskDatabase.taskDao().insertTask(task));
        Log.d(TAG, "addTask: ");
    }


    public void deleteTask(TaskEntity task) {
        executor.execute(() -> taskDatabase.taskDao().deleteTask(task));
    }


    public void updateTask(TaskEntity task) {
        executor.execute(() -> taskDatabase.taskDao().updateTask(task));
    }

    public LiveData<List<TaskEntity>> getTasksSorted() {
        return taskDatabase.taskDao().getTasksSorted();
    }
}
