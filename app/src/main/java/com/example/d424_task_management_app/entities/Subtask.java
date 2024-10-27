package com.example.d424_task_management_app.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Subtasks")
public class Subtask extends Task {
    @PrimaryKey(autoGenerate = true)
    private int subtaskID;
    private String subtaskName;
    private String subtaskDate;
    private int taskID;
    private String note;
    private boolean isCompleted;
    private long timestampCompleted;

    public Subtask(int subtaskID, String subtaskName, String subtaskDate, int taskID, String note) {
        super(taskID, "", "", "", "");
        this.subtaskID = subtaskID;
        this.note = note;
        this.taskID = taskID;
        this.subtaskDate = subtaskDate;
        this.subtaskName = subtaskName;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public int getSubtaskID() {
        return subtaskID;
    }
    public void setSubtaskID(int subtaskID) {
        this.subtaskID = subtaskID;
    }
    public String getSubtaskName() {
        return subtaskName;
    }
    public void setSubtaskName(String subtaskName) {
        this.subtaskName = subtaskName;
    }
    public String getSubtaskDate() {
        return subtaskDate;
    }
    public void setSubtaskDate(String subtaskDate) {
        this.subtaskDate = subtaskDate;
    }
    public int getTaskID() {
        return taskID;
    }
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getTimestampCompleted() {
        return timestampCompleted;
    }

    public void setTimestampCompleted(long timestampCompleted) {
        this.timestampCompleted = timestampCompleted;
    }

    @NonNull
    @Override
    public String toString(){
        return "Task: " + getTaskName() +
                "\n\nSubtask: " + subtaskName + "\nDate: " + subtaskDate + "\nNote: " + note;
    }
}
