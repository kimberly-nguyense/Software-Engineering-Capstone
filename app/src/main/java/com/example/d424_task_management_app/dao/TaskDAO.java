package com.example.d424_task_management_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424_task_management_app.entities.Task;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM Tasks ORDER BY taskID ASC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM Tasks WHERE taskID = :taskID")
    Task getTask(int taskID);

    @Query("SELECT * FROM Tasks WHERE userID = :userID")
    List<Task> getTasksByUser(int userID);

    @Query("SELECT * FROM Tasks WHERE isCompleted = 0 AND userID = :userID ORDER BY taskID ASC")
    List<Task> getIncompleteTasks(int userID);

    @Query("SELECT * FROM Tasks WHERE startDate <= :selectedDate AND endDate >= :selectedDate AND isCompleted = 0 AND userID = :userID")
    List<Task> getUserIncompletedTasksByDate(String selectedDate, int userID);
}
