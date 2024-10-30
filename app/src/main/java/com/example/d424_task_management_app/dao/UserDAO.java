package com.example.d424_task_management_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424_task_management_app.entities.User;

@Dao
public interface UserDAO {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Query("SELECT * FROM Users WHERE username = :username LIMIT 1")
    User getUser(String username);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM Users")
    void deleteAll();

    @Query("SELECT userID FROM Users WHERE username = :username LIMIT 1")
    int getUserID(String username);
}
