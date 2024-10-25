package com.example.d424_task_management_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d424_task_management_app.dao.ExcursionDAO;
import com.example.d424_task_management_app.dao.VacationDAO;
import com.example.d424_task_management_app.entities.Excursion;
import com.example.d424_task_management_app.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 24, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO VacationDAO();
    public abstract ExcursionDAO ExcursionDAO();
    public static VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VacationDatabaseBuilder.class, "VacationPlannerDatabase.db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}