package com.example.d424_task_management_app.database;

import android.app.Application;

import com.example.d424_task_management_app.dao.SubtaskDAO;
import com.example.d424_task_management_app.dao.TaskDAO;
import com.example.d424_task_management_app.entities.Subtask;
import com.example.d424_task_management_app.entities.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final TaskDAO taskListDAO;
    private final SubtaskDAO mSubtaskDAO;

    private List<Task> mAllTasks;
    private List<Subtask> mAllSubtasks;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        TaskDatabaseBuilder db = TaskDatabaseBuilder.getDatabase(application);
        taskListDAO = db.TaskDAO();
        mSubtaskDAO = db.SubtaskDAO();
    }

    public List<Task> getmAllTasks(){
        databaseWriteExecutor.execute(() -> mAllTasks = taskListDAO.getAllTasks());
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllTasks;
    }

    public Task getTask(int taskID) {
        return taskListDAO.getTask(taskID);
    }

    public long insert(Task task){
        final long[] taskID = new long[1];
        databaseWriteExecutor.execute(() -> taskID[0] = taskListDAO.insert(task));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return taskID[0];
    }

    public void update(Task task){
        databaseWriteExecutor.execute(() -> taskListDAO.update(task));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void delete(Task task){
        databaseWriteExecutor.execute(() -> taskListDAO.delete(task));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public List<Subtask> getmAllSubtasks(){
        databaseWriteExecutor.execute(() -> mAllSubtasks = mSubtaskDAO.getAllSubtasks());
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllSubtasks;
    }

    public List<Subtask> getAssociatedSubtasks(int taskID){
        databaseWriteExecutor.execute(() -> mAllSubtasks = mSubtaskDAO.getAssociatedSubtasks(taskID));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllSubtasks;
    }

    public void insert(Subtask subtask){
        databaseWriteExecutor.execute(() -> mSubtaskDAO.insert(subtask));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void update(Subtask subtask){
        databaseWriteExecutor.execute(() -> mSubtaskDAO.update(subtask));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void delete(Subtask subtask){
        databaseWriteExecutor.execute(() -> mSubtaskDAO.delete(subtask));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        databaseWriteExecutor.execute(() -> {
            taskListDAO.deleteAll();
            mSubtaskDAO.deleteAll();

            taskListDAO.resetTaskIdGenerator();
            mSubtaskDAO.resetSubtaskIdGenerator();
        });
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
