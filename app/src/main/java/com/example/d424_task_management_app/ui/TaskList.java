package com.example.d424_task_management_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

public class TaskList extends AppCompatActivity {
    private Repository repository;

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

        FloatingActionButton fab = findViewById(R.id.floatingActionButton_addTask);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TaskList.this, TaskDetails.class);
            intent.putExtra("isTaskSaved", false); // Starting a new task
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.taskRecyclerView);
        repository = new Repository(getApplication());
        List<Task> allTasks = repository.getmAllTasks();
        final TaskAdapter taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            Task task = new Task(0, "Dallas", "Marriott", "05/23/2024", "06/15/2024");
            int taskID = (int) repository.insert(task);
            Subtask subtask = new Subtask(0, "Art Gallery", "05/23/2024", taskID, "Art Gallery of Dallas");
            repository.insert(subtask);

            task = new Task(0, "Carrollton", "Courtyard", "05/23/2024", "06/15/2024");
            taskID = (int) repository.insert(task);
            subtask = new Subtask(0, "Museum", "05/23/2024", taskID, "Museum of Art");
            repository.insert(subtask);
            Toast.makeText(TaskList.this, "Sample Data Added", Toast.LENGTH_SHORT).show();
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> allTasks = repository.getmAllTasks();
        final TaskAdapter taskAdapter = new TaskAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.taskRecyclerView);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter.setTasks(allTasks);
    }
}