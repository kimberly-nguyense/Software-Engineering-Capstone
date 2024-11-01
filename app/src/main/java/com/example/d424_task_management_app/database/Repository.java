package com.example.d424_task_management_app.database;

import android.app.Application;

import com.example.d424_task_management_app.dao.CategoryDAO;
import com.example.d424_task_management_app.dao.SubtaskDAO;
import com.example.d424_task_management_app.dao.TaskDAO;
import com.example.d424_task_management_app.dao.UserDAO;
import com.example.d424_task_management_app.entities.Category;
import com.example.d424_task_management_app.entities.Subtask;
import com.example.d424_task_management_app.entities.Task;
import com.example.d424_task_management_app.entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final TaskDAO taskListDAO;
    private final SubtaskDAO mSubtaskDAO;
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;

    private List<Task> mAllTasks;
    private List<Subtask> mAllSubtasks;
    private List<Task> mUserTasks;
    private List<Category> mAllCategories;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        TaskDatabaseBuilder db = TaskDatabaseBuilder.getDatabase(application);
        taskListDAO = db.TaskDAO();
        mSubtaskDAO = db.SubtaskDAO();
        userDAO = db.UserDAO();
        categoryDAO = db.CategoryDAO();
    }

    public Repository(TaskDAO taskListDAO, SubtaskDAO mSubtaskDAO, UserDAO userDAO, CategoryDAO categoryDAO) {
        this.taskListDAO = taskListDAO;
        this.mSubtaskDAO = mSubtaskDAO;
        this.userDAO = userDAO;
        this.categoryDAO = categoryDAO;
    }

    public List<Task> getmAllTasks() {
        databaseWriteExecutor.execute(() -> mAllTasks = taskListDAO.getAllTasks());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllTasks;
    }

    public Task getTask(int taskID) {
        return taskListDAO.getTask(taskID);
    }

    public List<Task> getTasksByUser(int userID) {
        databaseWriteExecutor.execute(() -> mUserTasks = taskListDAO.getTasksByUser(userID));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mUserTasks;
    }

    public boolean createTask(Task task) {
        if (task.getTaskName().isEmpty()) {
            return false;
        }
        databaseWriteExecutor.execute(() -> {
            taskListDAO.insert(task);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public long insertTask(Task task) {
        final long[] taskID = new long[1];
        databaseWriteExecutor.execute(() -> taskID[0] = taskListDAO.insert(task));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return taskID[0];
    }

    public void updateTask(Task task) {
        databaseWriteExecutor.execute(() -> taskListDAO.update(task));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Task task) {
        databaseWriteExecutor.execute(() -> taskListDAO.delete(task));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Subtask> getmAllSubtasks() {
        databaseWriteExecutor.execute(() -> mAllSubtasks = mSubtaskDAO.getAllSubtasks());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllSubtasks;
    }

    public List<Subtask> getAssociatedSubtasks(int taskID) {
        databaseWriteExecutor.execute(() -> mAllSubtasks = mSubtaskDAO.getAssociatedSubtasks(taskID));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllSubtasks;
    }

    public void insertSubtask(Subtask subtask) {
        databaseWriteExecutor.execute(() -> mSubtaskDAO.insert(subtask));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateSubtask(Subtask subtask) {
        databaseWriteExecutor.execute(() -> mSubtaskDAO.update(subtask));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubtask(Subtask subtask) {
        databaseWriteExecutor.execute(() -> mSubtaskDAO.delete(subtask));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Subtask> getIncompleteSubtasks(int taskID) {
        databaseWriteExecutor.execute(() -> mAllSubtasks = mSubtaskDAO.incompleteSubtasks(taskID));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllSubtasks;
    }

    public List<Task> getIncompleteTasks(int userID) {
        databaseWriteExecutor.execute(() -> mAllTasks = taskListDAO.getIncompleteTasks(userID));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllTasks;
    }

    public List<Task> getUserIncompletedTasksByDate(String selectedDate, int userID) {
        databaseWriteExecutor.execute(() -> mAllTasks = taskListDAO.getUserIncompletedTasksByDate(selectedDate, userID));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
            }
        return mAllTasks;
    }

    public User getUser(String username) {
        final User[] user = new User[1];
        databaseWriteExecutor.execute(() -> user[0] = userDAO.getUser(username));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return user[0];
    }

    public int insertUser(User user) {
        final int[] userID = new int[1];
        databaseWriteExecutor.execute(() -> userID[0] = (int) userDAO.insert(user));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userID[0];
    }

    public void updateUser(User user) {
        databaseWriteExecutor.execute(() -> userDAO.update(user));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) {
        databaseWriteExecutor.execute(() -> userDAO.delete(user));
    }

    public void addSampleData(int userID) {
        databaseWriteExecutor.execute(() -> {
            Task task = new Task(0, "D424 Capstone", "School", "09/15/2024", "10/31/2024", userID);
            int taskID = (int) taskListDAO.insert(task);
            Subtask subtask = new Subtask(0, "Task 3", "10/26/2024", taskID, userID, "Complete and submit Task 3.");
            mSubtaskDAO.insert(subtask);
            subtask = new Subtask(0, "Task 4", "10/28/2024", taskID, userID, "Complete and submit Task 4.");
            mSubtaskDAO.insert(subtask);

            task = new Task(0, "Leetcode", "Work", "05/23/2024", "12/31/2024", userID);
            taskID = (int) taskListDAO.insert(task);
            subtask = new Subtask(0, "FizzBuzz", "08/12/2024", taskID, userID, "Watch a tutorial on how to solve FizzBuzz.");
            mSubtaskDAO.insert(subtask);
        });
    }
    public void insertCategory(Category category) {
        databaseWriteExecutor.execute(() -> {
            categoryDAO.insert(category);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void updateCategory(Category category) {
        databaseWriteExecutor.execute(() -> {
            categoryDAO.update(category);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(Category category) {
        databaseWriteExecutor.execute(() -> {
            categoryDAO.delete(category);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public List<Category> getAllCategories() {
        databaseWriteExecutor.execute(() -> {
            mAllCategories = categoryDAO.getAllCategories();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCategories;
    }
}
