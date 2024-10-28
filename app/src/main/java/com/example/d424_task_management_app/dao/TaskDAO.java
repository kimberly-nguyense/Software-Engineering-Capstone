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

    @Query("SELECT * FROM Tasks WHERE isCompleted = 0")
    List<Task> getIncompleteTasks();

    @Query("DELETE FROM Tasks")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Tasks'")
    void resetTaskIdGenerator();
}
