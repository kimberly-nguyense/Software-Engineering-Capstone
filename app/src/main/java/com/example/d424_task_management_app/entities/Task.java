package com.example.d424_task_management_app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int taskID;
    private String taskName;
    private String categoryName;
    private String startDate;
    private String endDate;

    public Task(int taskID, String taskName, String categoryName, String startDate, String endDate) {
        this.categoryName = categoryName;
        this.taskName = taskName;
        this.taskID = taskID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String TaskName) {
        this.taskName = TaskName;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
    public int getTaskID() {
        return taskID;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String toString(){
        return "Task: " + taskName + "\nCategory: " + categoryName + "\nStart Date: " + startDate + "\nEnd Date: " + endDate;
    }
}
