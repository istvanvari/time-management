package com.example.timeapp.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.timeapp.db.Converters.DateConverter;
import com.example.timeapp.db.Converters.TimeConverter;

@Database(entities = {TaskEntity.class, RoutineEntity.class}, version = 6)
@TypeConverters({DateConverter.class, TimeConverter.class})
public abstract class TaskDatabase extends RoomDatabase {

    private static final String DB_NAME = "tasks.db";
    private static TaskDatabase instance;

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d("db", "getInstance: create instance");
            instance = Room.databaseBuilder(context, TaskDatabase.class, DB_NAME).fallbackToDestructiveMigration().
                    allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract TaskDao taskDao();

    public abstract RoutineDao routineDao();
}

