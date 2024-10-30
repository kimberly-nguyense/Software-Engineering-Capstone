package com.example.d424_task_management_app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userID;
    private String username;
    private String hashedPassword;
    private String salt;


    public User(String username, String hashedPassword, String salt) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
