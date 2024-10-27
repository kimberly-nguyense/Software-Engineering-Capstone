package com.example.d424_task_management_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.database.Repository;
import com.example.d424_task_management_app.entities.Subtask;
import com.example.d424_task_management_app.entities.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.logging.Handler;

public class TaskList extends AppCompatActivity {
    private Repository repository;
    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (taskAdapter == null) {
                    return false;
                }
                taskAdapter.filter(newText);
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton_addTask);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TaskList.this, TaskDetails.class);
            intent.putExtra("isTaskSaved", false); // Starting a new task
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.taskRecyclerView);
        repository = new Repository(getApplication());
        taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateTasks(){
        List<Task> allTasks = repository.getmAllTasks();
        taskAdapter.setTasks(allTasks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_data) {
            repository = new Repository(getApplication());
            repository.deleteAll();
            Toast.makeText(TaskList.this, "All Data Deleted", Toast.LENGTH_SHORT).show();
            onResume();
        }
        if (item.getItemId() == R.id.add_sample_data) {
            repository = new Repository(getApplication());
            Task task = new Task(0, "D424 Capstone", "School", "09/15/2024", "10/31/2024");
            int taskID = (int) repository.insert(task);
            Subtask subtask = new Subtask(0, "Task 3", "10/26/2024", taskID, "Complete and submit Task 3.");
            repository.insert(subtask);
            subtask = new Subtask(0, "Task 4", "10/28/2024", taskID, "Complete and submit Task 4.");
            repository.insert(subtask);

            task = new Task(0, "Leetcode", "Interview Prep", "05/23/2024", "12/31/2024");
            taskID = (int) repository.insert(task);
            subtask = new Subtask(0, "FizzBuzz", "08/12/2024", taskID, "Watch a tutorial on how to solve FizzBuzz.");
            repository.insert(subtask);
            Toast.makeText(TaskList.this, "Sample Data Added", Toast.LENGTH_SHORT).show();
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTasks();
    }
}