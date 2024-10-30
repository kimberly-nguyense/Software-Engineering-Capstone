package com.example.d424_task_management_app.ui;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManagement {
    private static final String KEY_USER_ID = "user_id";
    private static final String PREF_NAME = "user_session";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserSessionManagement(Context applicationContext) {
        sharedPreferences = applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(int userID) {
        editor.putInt(KEY_USER_ID, userID);
        editor.apply();
    }

    public int getCurrentUserID() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
