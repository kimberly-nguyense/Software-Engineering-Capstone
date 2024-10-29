package com.example.d424_task_management_app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.database.Repository;
import com.example.d424_task_management_app.entities.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView taskDetails;
    private Repository repository;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String todayDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        selectedDate = todayDate;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        calendarView = findViewById(R.id.calendarView);
        taskDetails = findViewById(R.id.taskDetails);
        repository = new Repository(getApplication());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%02d/%02d/%04d", month + 1, dayOfMonth, year);
            List<Task> tasks = repository.getTasksByDate(selectedDate);
            displayTaskDetails(tasks);
        });
        List<Task> tasks = repository.getTasksByDate(selectedDate);
        displayTaskDetails(tasks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> tasks = repository.getTasksByDate(selectedDate);
        displayTaskDetails(tasks);
    }

    private void displayTaskDetails(List<Task> tasks) {
        StringBuilder details = new StringBuilder();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                details.append("Task: ").append(task.getTaskName()).append("\n");
                details.append("Category: ").append(task.getCategoryName()).append("\n");
                details.append("Start Date: ").append(task.getStartDate()).append("\n");
                details.append("End Date: ").append(task.getEndDate()).append("\n\n");
            }
            taskDetails.setText(details.toString());
        } else {
            taskDetails.setText("No tasks found for the selected date.");
        }
    }
}
