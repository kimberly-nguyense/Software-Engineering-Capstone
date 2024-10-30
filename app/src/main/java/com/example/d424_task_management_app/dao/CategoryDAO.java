package com.example.d424_task_management_app.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.d424_task_management_app.entities.Category;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert
    void insert(Category category);
    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM Categories")
    List<Category> getAllCategories();

    @Query("SELECT COUNT(*) FROM Categories")
    int getCount();
}
