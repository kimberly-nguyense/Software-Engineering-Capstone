package com.example.d424_task_management_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d424_task_management_app.dao.CategoryDAO;
import com.example.d424_task_management_app.dao.SubtaskDAO;
import com.example.d424_task_management_app.dao.TaskDAO;
import com.example.d424_task_management_app.dao.UserDAO;
import com.example.d424_task_management_app.entities.Category;
import com.example.d424_task_management_app.entities.Subtask;
import com.example.d424_task_management_app.entities.Task;
import com.example.d424_task_management_app.entities.User;

@Database(entities = {Task.class, Subtask.class, User.class, Category.class}, version = 8, exportSchema = false)
public abstract class TaskDatabaseBuilder extends RoomDatabase {
    public abstract TaskDAO TaskDAO();
    public abstract SubtaskDAO SubtaskDAO();
    public abstract UserDAO UserDAO();
    public abstract CategoryDAO CategoryDAO();
    public static TaskDatabaseBuilder INSTANCE;

    public static TaskDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskDatabaseBuilder.class, "TaskPlannerDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                    INSTANCE.insertDefaultCategories();
                }
            }
        }
        return INSTANCE;
    }

    public void insertDefaultCategories() {
        new Thread(() -> {
            CategoryDAO categoryDAO = CategoryDAO();
            if (categoryDAO.getCount() == 0) {
                categoryDAO.insert(new Category(1, "No Category"));
                categoryDAO.insert(new Category(2, "Home"));
                categoryDAO.insert(new Category(3, "School"));
            }
        }).start();
    }
}
