package com.example.d424_task_management_app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tasks")
public class Task {
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String TaskName) {
        this.taskName = TaskName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int gettaskID() {
        return taskID;
    }

    public void settaskID(int taskID) {
        this.taskID = taskID;
    }

    public Task(int taskID, String taskName, String hotelName, String startDate, String endDate) {
        this.hotelName = hotelName;
        this.taskName = taskName;
        this.taskID = taskID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    @PrimaryKey(autoGenerate = true)
    private int taskID;
    private String taskName;
    private String hotelName;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    private String startDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private String endDate;


    public int getTaskID() {
        return taskID;
    }

}
