package com.example.timeapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.time.OffsetTime;

@Entity(tableName = "task_table")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "task_name")
    private String taskName;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "task_date")
    private LocalDate taskDate;

    @TypeConverters({TimeConverter.class})
    @ColumnInfo(name = "task_time")
    private OffsetTime taskTime;

    @ColumnInfo(name = "task_priority")
    private String taskPriority;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    public TaskEntity() {
    }

    public TaskEntity(String taskName, String taskDescription, LocalDate taskDate, OffsetTime taskTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public OffsetTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(OffsetTime taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}