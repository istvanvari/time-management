package com.example.timeapp.ui.today;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.util.TaskReminderBroadcast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.List;


public class TaskViewModel extends ViewModel {
    private final TaskRepository repository;
    String TAG = "HomeViewModel";
    private TaskEntity cache = null;
    private AlarmManager alarmManager;
    private MutableLiveData<LocalDate> day = new MutableLiveData<>();
    private LiveData<List<TaskEntity>> repeatedTasks;
    private LiveData<List<TaskEntity>> tasks;
    private MutableLiveData<TaskEntity> task;

    public TaskViewModel() {
        repository = TaskRepository.getInstance();
        task = new MutableLiveData<>();
        tasks = repository.getTasksByDate(LocalDate.now().toString());
        repeatedTasks = repository.getRepeatedTasks();
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
        cache = task;
        repository.deleteTask(task);
    }

    public TaskEntity getCache() {
        return cache;
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

    public void setAlarm(TaskEntity task, Context context) {
        LocalDate date = LocalDate.parse(task.getDate());
        OffsetTime time = task.getTime();

        Intent intent = new Intent(context, TaskReminderBroadcast.class);
        intent.putExtra("name", task.getName());
        intent.putExtra("time", task.getTime().toString());
        intent.putExtra("remindType", task.getReminderType());

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, 0);


        long timeInMillis = LocalDateTime.of(date, LocalTime.from(time)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        switch (task.getReminderType()) {
            case 1:
                break;
            case 2:
                timeInMillis -= (1000 * 60 * 5);
                break;
            case 3:
                timeInMillis -= (1000 * 60 * 15);
                break;
            case 4:
                timeInMillis -= (1000 * 60 * 30);
                break;
            case 5:
                timeInMillis -= 3600000;
                break;
        }


        if (task.isRepeated()) {
            if (task.getPeriod() == 0) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY * 7 * task.getPeriod(), pendingIntent);
            }
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
        Log.d(TAG, "setAlarm: alarm set, intent id" + task.getId());
    }

    public void deleteAlarm(TaskEntity task, Context context) {
        Intent intent = new Intent(context, TaskReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, 0);
        if (alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
    }


    public LiveData<List<TaskEntity>> getRepeatedTasks() {
        return repeatedTasks;
    }
}