package com.example.d424_task_management_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424_task_management_app.entities.Subtask;

import java.util.List;

@Dao
public interface SubtaskDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Subtask subtask);

    @Update
    void update(Subtask subtask);

    @Delete
    void delete(Subtask subtask);

    @Query("SELECT * FROM Subtasks ORDER BY subtaskID ASC")
    List<Subtask> getAllSubtasks();

    @Query("SELECT * FROM Subtasks WHERE taskID = :prod ORDER BY subtaskID ASC")
    List<Subtask> getAssociatedSubtasks(int prod);

    @Query("DELETE FROM Subtasks")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Subtasks'")
    void resetSubtaskIdGenerator();
}
