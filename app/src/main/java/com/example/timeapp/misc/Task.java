package com.example.timeapp.misc;

//bean class for task
public class Task {
    private long id;
    private String taskName;
    private String taskDescription;
    private String taskDate;
    private String taskTime;
    private String taskPriority;
    private String[] taskTags;

    public Task( long id, String taskName, String taskDescription, String taskDate, String taskTime,
                String taskPriority, String[] taskTags) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskPriority = taskPriority;
        this.taskTags = taskTags;
    }

    public long getId() {
        return this.id;
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

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }
}

