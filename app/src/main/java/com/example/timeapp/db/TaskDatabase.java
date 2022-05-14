package com.example.timeapp.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TaskEntity.class}, version = 2)
@TypeConverters({DateConverter.class, TimeConverter.class})
public abstract class TaskDatabase extends RoomDatabase {

    private static final String DB_NAME = "tasks.db";
    private static TaskDatabase instance;

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d("db", "getInstance: create instance");
            instance = Room.databaseBuilder(context, TaskDatabase.class, DB_NAME).fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }

        return instance;
    }

    public abstract TaskDao taskDao();

}

