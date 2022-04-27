package com.example.timeapp.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask(TaskEntity taskEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntity taskEntity);

    @Delete
    void deleteTask(TaskEntity taskEntity);

    @Query("SELECT * FROM task_table WHERE id = :id")
    TaskEntity getTaskById(int id);

    @Query("SELECT * FROM task_table")
    List<TaskEntity> getAllTasks();

    //query for tasks by date
    @Query("SELECT * FROM task_table WHERE task_date = :date")
    List<TaskEntity> getTasksByDate(String date);

}