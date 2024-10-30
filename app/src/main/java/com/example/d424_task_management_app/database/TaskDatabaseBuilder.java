package com.example.d424_task_management_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d424_task_management_app.dao.SubtaskDAO;
import com.example.d424_task_management_app.dao.TaskDAO;
import com.example.d424_task_management_app.dao.UserDAO;
import com.example.d424_task_management_app.entities.Subtask;
import com.example.d424_task_management_app.entities.Task;
import com.example.d424_task_management_app.entities.User;

@Database(entities = {Task.class, Subtask.class, User.class}, version = 6, exportSchema = false)
public abstract class TaskDatabaseBuilder extends RoomDatabase {
    public abstract TaskDAO TaskDAO();
    public abstract SubtaskDAO SubtaskDAO();
    public abstract UserDAO UserDAO();
    public static TaskDatabaseBuilder INSTANCE;

    static TaskDatabaseBuilder getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (TaskDatabaseBuilder.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabaseBuilder.class, "TaskPlannerDatabase.db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}