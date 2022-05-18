package com.example.timeapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRoutine(RoutineEntity routineEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRoutine(RoutineEntity routineEntity);

    @Delete
    void deleteRoutine(RoutineEntity routineEntity);

    @Query("SELECT * FROM routine_table WHERE id = :id")
    LiveData<RoutineEntity> getRoutineById(int id);

    @Query("SELECT * FROM routine_table ORDER BY routine_time ASC")
    LiveData<List<RoutineEntity>> getAllRoutines();

    //get routines by day
    @Query("SELECT * FROM routine_table WHERE routine_day = :day ORDER BY routine_time ASC")
    LiveData<List<RoutineEntity>> getRoutinesByDay(int day);

}