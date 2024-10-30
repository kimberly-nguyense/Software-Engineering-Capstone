package com.example.d424_task_management_app.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.d424_task_management_app.entities.Category;
import com.example.d424_task_management_app.entities.Subtask;
import com.example.d424_task_management_app.entities.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskDetails extends AppCompatActivity {
    private String taskName;
    private String categoryName;
    private String taskStart;
    private String taskEnd;
    private int taskID;
    private EditText edit_taskName;
    private TextView edit_startDate;
    private TextView edit_endDate;
    private Repository repository;
    private boolean isTaskSaved = false;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;
    private final Calendar myCalendar = Calendar.getInstance();
    private SubtaskAdapter subtaskAdapter;
    private UserSessionManagement userSessionManagement;
    private int userID;
    private Spinner categorySpinner;
    private ArrayAdapter<Category> categoryAdapter;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userSessionManagement = new UserSessionManagement(this);
        userID = userSessionManagement.getCurrentUserID();

        // Set up Recycler View to show subtasks associated with task
        RecyclerView recyclerView = findViewById(R.id.subtaskRecyclerView);
        repository = new Repository(getApplication());
        subtaskAdapter = new SubtaskAdapter(this, repository);
        recyclerView.setAdapter(subtaskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Subtask> filteredSubtasks = repository.getAssociatedSubtasks(taskID);
        subtaskAdapter.setSubtasks(filteredSubtasks);
        categorySpinner = findViewById(R.id.spinner_categories);
        loadCategorySpinner();

        // Get input fields
        edit_taskName = findViewById(R.id.edit_taskName);
        edit_startDate = findViewById(R.id.edit_startDate);
        edit_endDate = findViewById(R.id.edit_endDate);
        // Get clicked task details
        taskName = getIntent().getStringExtra("taskName");
        categoryName = getIntent().getStringExtra("taskCategoryName");
        taskStart = getIntent().getStringExtra("taskStartDate");
        taskEnd = getIntent().getStringExtra("taskEndDate");
        taskID = getIntent().getIntExtra("taskID", -1);
        // Check if task has been saved
        isTaskSaved = getIntent().getBooleanExtra("isTaskSaved", false);
        edit_taskName.addTextChangedListener(textWatcher);
        edit_startDate.addTextChangedListener(textWatcher);
        edit_endDate.addTextChangedListener(textWatcher);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton_addSubtask);
        fab.setOnClickListener(view -> {
            if (!isTaskSaved) {
                Toast.makeText(TaskDetails.this, "Please save the task before adding an subtask.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(TaskDetails.this, SubtaskDetails.class);
                intent.putExtra("taskID", taskID);
                intent.putExtra("taskName", edit_taskName.getText().toString().trim());
                intent.putExtra("taskStart", edit_startDate.getText().toString());
                intent.putExtra("taskEnd", edit_endDate.getText().toString());
                startActivity(intent);
            }
        });

        edit_startDate.setOnClickListener(view -> new DatePickerDialog(TaskDetails.this,
                startDateListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        startDateListener = (datePicker, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelStartDate();
        };

        edit_endDate.setOnClickListener(view -> new DatePickerDialog(TaskDetails.this,
                endDateListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        endDateListener = (datePicker, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelEndDate();
        };
    }
    
    public void updateTasks(){
        List<Subtask> associatedSubtasks = repository.getAssociatedSubtasks(taskID);
        subtaskAdapter.setSubtasks(associatedSubtasks);
    }

    public void updateLabelStartDate() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        edit_startDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void updateLabelEndDate() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        edit_endDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void loadCategorySpinner() {
        new Thread(() -> {
            List<Category> categories = repository.getAllCategories();
            runOnUiThread(() -> {
                categoryList = new ArrayList<>();
                categoryList.add(new Category(0, "Select a Category"));
                categoryList.addAll(categories);

                categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryAdapter);

                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
                        categoryName = selectedCategory.getCategoryName();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        categoryName = null;
                    }
                });

                // Set input fields
                setTaskDetailsWithoutTriggeringTextWatcher();
            });
        }).start();
    }

    private void setTaskDetailsWithoutTriggeringTextWatcher() {
        edit_taskName.removeTextChangedListener(textWatcher);
        edit_startDate.removeTextChangedListener(textWatcher);
        edit_endDate.removeTextChangedListener(textWatcher);

        edit_taskName.setText(taskName);
        edit_startDate.setText(taskStart);
        edit_endDate.setText(taskEnd);

        if (taskID == -1) {
            categorySpinner.setSelection(0);
        } else {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getCategoryName().equals(categoryName)) {
                    categorySpinner.setSelection(i);
                    break;
                }
            }
        }

        edit_taskName.addTextChangedListener(textWatcher);
        edit_startDate.addTextChangedListener(textWatcher);
        edit_endDate.addTextChangedListener(textWatcher);
    }



    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isTaskSaved = false;
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_details, menu);
        return true;
    }

    public int checkValidDate() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        String startDate = edit_startDate.getText().toString();
        String endDate = edit_endDate.getText().toString();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(TaskDetails.this, "Date fields cannot be empty", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            if (start != null && start.after(end)) {
                Toast.makeText(TaskDetails.this, "Error: Start date must be before end date", Toast.LENGTH_SHORT).show();
                return -1;
            }
        } catch (ParseException e) {
            Toast.makeText(TaskDetails.this, "Invalid date format. Please use MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Task task;
        if (item.getItemId() == R.id.save_task) {
            categoryName = ((Category) categorySpinner.getSelectedItem()).getCategoryName();
            if (checkValidDate() == -1) {
                return true;
            }else if (edit_taskName.getText().toString().isEmpty()) {
                Toast.makeText(TaskDetails.this, "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                return true;
            }else if (edit_startDate.getText().toString().isEmpty()) {
                Toast.makeText(TaskDetails.this, "Start date cannot be empty", Toast.LENGTH_SHORT).show();
                return true;
            }else if (edit_endDate.getText().toString().isEmpty()) {
                Toast.makeText(TaskDetails.this, "End date cannot be empty", Toast.LENGTH_SHORT).show();
                return true;
            }
            // If new task, get next task ID and create new task from input fields
            else if (taskID == -1) {
                if (repository.getmAllTasks().isEmpty()) {
                    taskID = 1;
                } else {
                    taskID = repository.getmAllTasks().get(repository.getmAllTasks().size() - 1).getTaskID() + 1;
                }task = new Task(taskID,
                        edit_taskName.getText().toString().trim(),
                        categoryName,
                        edit_startDate.getText().toString(),
                        edit_endDate.getText().toString(),
                        userID);
                Toast.makeText(TaskDetails.this, "Adding Task", Toast.LENGTH_SHORT).show();
                repository.insertTask(task);
            } else {
                task = new Task(taskID,
                        edit_taskName.getText().toString().trim(),
                        categoryName,
                        edit_startDate.getText().toString(),
                        edit_endDate.getText().toString(),
                        userID);
                Toast.makeText(TaskDetails.this, "Updating Task", Toast.LENGTH_SHORT).show();
                repository.updateTask(task);
            }
            isTaskSaved = true;
            return true;
        }
        if (item.getItemId() == R.id.delete_task) {
            categoryName = ((Category) categorySpinner.getSelectedItem()).getCategoryName();
            if (!isTaskSaved) {
                Toast.makeText(TaskDetails.this, "Cannot delete task that has not been saved", Toast.LENGTH_SHORT).show();
                return true;
            }
            List<Subtask> associatedSubtasks = repository.getAssociatedSubtasks(taskID);
            if (!associatedSubtasks.isEmpty()) {
                Toast.makeText(TaskDetails.this, "Cannot delete task with associated subtasks", Toast.LENGTH_SHORT).show();
            } else {
                task = new Task(taskID,
                        edit_taskName.getText().toString().trim(),
                        categoryName,
                        edit_startDate.getText().toString(),
                        edit_endDate.getText().toString(),
                        userID);
                Toast.makeText(TaskDetails.this, "Deleting Task", Toast.LENGTH_SHORT).show();
                repository.deleteTask(task);
                this.finish();
            }
            return true;
        }
        if (item.getItemId() == R.id.share_details) {
            if (!isTaskSaved) {
                Toast.makeText(TaskDetails.this, "Cannot share task that has not been saved", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (checkValidDate() == -1) {
                return true;
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            StringBuilder builder = new StringBuilder();
            builder.append("Task Name: ").append(edit_taskName.getText().toString().trim()).append("\nTask Date: ").append(edit_startDate.getText().toString()).append(" - ").append(edit_endDate.getText().toString()).append("\n\n");
            if (repository.getAssociatedSubtasks(taskID).isEmpty()) {
                builder.append("No subtasks associated with this task");
            }
            else{
                for (Subtask subtask : repository.getAssociatedSubtasks(taskID)) {
                    builder.append("Subtask Name: ").append(subtask.getSubtaskName()).append("\nSubtask Date: ").append(subtask.getSubtaskDate()).append("\n\n");
                }
            }
            intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
            intent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId() == R.id.notify_task) {
            if (!isTaskSaved) {
                Toast.makeText(TaskDetails.this, "Cannot notify task that has not been saved", Toast.LENGTH_SHORT).show();
                return true;
            }
            String dateFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

            Date startDate;
            Date endDate;
            try {
                startDate = sdf.parse(edit_startDate.getText().toString());
                endDate = sdf.parse(edit_endDate.getText().toString());
            } catch (ParseException e) {
                Toast.makeText(TaskDetails.this, "Invalid date format. Please use MM/DD/YYYY", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return true;
            }
            if (startDate == null || endDate == null) {
                Toast.makeText(TaskDetails.this, "Invalid date format. Please use MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            } else {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long triggerTime = System.currentTimeMillis() + 60000;

                Intent intent = new Intent(TaskDetails.this, MyReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(TaskDetails.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S){
                    if (alarmManager.canScheduleExactAlarms()){
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, sender);
                    }
                }
                else{
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
                }

                long triggerTimeStart = startDate.getTime();
                Intent intentStart = new Intent(TaskDetails.this, MyReceiver.class);
                intentStart.putExtra("name", edit_taskName.getText().toString().trim());
                intentStart.putExtra("date", edit_startDate.getText().toString());
                PendingIntent senderStart = PendingIntent.getBroadcast(TaskDetails.this, ++Main.numAlert, intentStart, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeStart, senderStart);

                long triggerTimeEnd = endDate.getTime();
                Intent intentEnd = new Intent(TaskDetails.this, MyReceiver.class);
                intentEnd.putExtra("name", edit_taskName.getText().toString().trim());
                intentEnd.putExtra("date", edit_endDate.getText().toString());
                PendingIntent senderEnd = PendingIntent.getBroadcast(TaskDetails.this, ++Main.numAlert, intentEnd, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeEnd, senderEnd);
                Toast.makeText(TaskDetails.this,
                        edit_taskName.getText().toString().trim() +  " notifications set for " + edit_startDate.getText().toString() + " and " + edit_endDate.getText().toString(),
                        Toast.LENGTH_SHORT).show();
                onResume();
            }
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTasks();
    }
}