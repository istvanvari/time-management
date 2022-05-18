package com.example.timeapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "routine_table")
public class RoutineEntity implements Comparable<RoutineEntity> {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "routine_name")
    private String Name;

    @ColumnInfo(name = "routine_description")
    private String Description;

    @ColumnInfo(name = "routine_time")
    private String Time;

    @ColumnInfo(name = "routine_repeat")
    private int RepeatPeriod;

    @ColumnInfo(name = "routine_day")
    private int Day;

    @Ignore
    public RoutineEntity() {
    }

    public RoutineEntity(String Name, String Description,
                         String Time, int RepeatPeriod, int Day) {
        this.Name = Name;
        this.Description = Description;
        this.Time = Time;
        this.RepeatPeriod = RepeatPeriod;
        this.Day = Day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public int getRepeatPeriod() {
        return RepeatPeriod;
    }

    public void setRepeatPeriod(int repeatPeriod) {
        this.RepeatPeriod = repeatPeriod;
    }


    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        this.Day = day;
    }

    @Override
    public int compareTo(RoutineEntity routineEntity) {
        if (routineEntity.getName().equals(this.Name) &&
                routineEntity.getDescription().equals(this.Description) &&
                routineEntity.getTime().equals(this.Time) &&
                routineEntity.getRepeatPeriod() == this.RepeatPeriod &&
                routineEntity.getDay() == this.Day) {
            return 0;
        } else {
            return 1;
        }
    }
}