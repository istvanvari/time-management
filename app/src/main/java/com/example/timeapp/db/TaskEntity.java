package com.example.timeapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.timeapp.db.Converters.TimeConverter;

import java.time.OffsetTime;

@Entity(tableName = "task_table")
public class TaskEntity implements Comparable<TaskEntity> {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "task_name")
    private String name;

    @ColumnInfo(name = "task_description")
    private String description;

    @ColumnInfo(name = "task_date")
    private String date;

    @TypeConverters({TimeConverter.class})
    @ColumnInfo(name = "task_time")
    private OffsetTime time;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @ColumnInfo(name = "period")
    private int period;

    @ColumnInfo(name = "day")
    private int day;

    @ColumnInfo(name = "is_repeated")
    private boolean isRepeated;

    @Ignore
    public TaskEntity() {
    }

    public TaskEntity(String name, String description, String date, OffsetTime time, boolean isCompleted, int period, int day, boolean isRepeated) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.isCompleted = isCompleted;
        this.period = period;
        this.day = day;
        this.isRepeated = isRepeated;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OffsetTime getTime() {
        return time;
    }

    public void setTime(OffsetTime time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    @Override
    public int compareTo(TaskEntity o) {
        TaskEntity task = (TaskEntity) o;
        if (task.getName().equals(this.name) &&
                task.getDescription().equals(this.description) &&
                task.getDate().equals(this.date) &&
                task.getTime().equals(this.time) &&
                task.getPeriod() == this.period &&
                task.getDay() == this.day &&
                task.isRepeated() == this.isRepeated)
            return 0;
        return 1;
    }
}