package com.example.timeapp;

import android.app.Application;
import android.util.Log;

import com.example.timeapp.db.TaskDatabase;

public class App extends Application {
    private static TaskDatabase database;

    public static TaskDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(this.toString(), "onCreate: ");
        database = TaskDatabase.getInstance(this);
    }
}
