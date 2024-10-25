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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.database.Repository;
import com.example.d424_task_management_app.entities.Subtask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SubtaskDetails extends AppCompatActivity {
    Repository repository;
    String subtaskName;
    String subtaskDate;
    String subtaskNote;
    int subtaskID;
    int taskID;
    String taskName;
    String taskStart;
    String taskEnd;
    TextView editTaskName;
    EditText editName;
    TextView editDate;
    EditText editNote;

    boolean isSubtaskSaved = false;

    DatePickerDialog.OnDateSetListener dateListener;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subtask_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repository = new Repository(getApplication());
        // Get input fields
        editTaskName = findViewById(R.id.edit_taskName);
        editName = findViewById(R.id.edit_subtaskName);
        editDate = findViewById(R.id.edit_subtaskDate);
        editNote = findViewById(R.id.edit_subtaskNote);
        // Get passed values
        taskID = getIntent().getIntExtra("taskID", -1);
        taskName = getIntent().getStringExtra("taskName");
        taskStart = getIntent().getStringExtra("taskStart");
        taskEnd = getIntent().getStringExtra("taskEnd");

        subtaskID = getIntent().getIntExtra("subtaskID", -1);
        subtaskName = getIntent().getStringExtra("subtaskName");
        subtaskDate = getIntent().getStringExtra("subtaskDate");
        subtaskNote = getIntent().getStringExtra("subtaskNote");
        // Check if subtask has been saved
        isSubtaskSaved = getIntent().getBooleanExtra("isSubtaskSaved", false);
        editTaskName.addTextChangedListener(textWatcher);
        editName.addTextChangedListener(textWatcher);
        editDate.addTextChangedListener(textWatcher);
        editNote.addTextChangedListener(textWatcher);
        // Set input fields
        setTaskDetailsWithoutTriggeringTextWatcher();

        if(subtaskID != -1){
            isSubtaskSaved = true;
        }

        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        editDate.setOnClickListener(view -> {

            String info = editDate.getText().toString();
            if (info.equals("")) {
                info = sdf.format(Calendar.getInstance().getTime());
            }
            try {
                myCalendar.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(SubtaskDetails.this,
                    dateListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabelDate();
            }
        };
    }

    public void updateLabelDate() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        editDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void setTaskDetailsWithoutTriggeringTextWatcher() {
        editTaskName.removeTextChangedListener(textWatcher);
        editName.removeTextChangedListener(textWatcher);
        editDate.removeTextChangedListener(textWatcher);
        editNote.removeTextChangedListener(textWatcher);

        editTaskName.setText(taskName);
        editName.setText(subtaskName);
        editDate.setText(subtaskDate);
        editNote.setText(subtaskNote);

        editTaskName.addTextChangedListener(textWatcher);
        editName.addTextChangedListener(textWatcher);
        editDate.addTextChangedListener(textWatcher);
        editNote.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isSubtaskSaved = false;
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subtask_details, menu);
        return true;
    }

    public int checkValidDate() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        String subtaskDate = editDate.getText().toString();
        String startDate = getIntent().getStringExtra("taskStart");
        String endDate = getIntent().getStringExtra("taskEnd");
        if (subtaskDate == null || subtaskDate.isEmpty() ||
                startDate == null || startDate.isEmpty() ||
                endDate == null || endDate.isEmpty()) {

            Toast.makeText(SubtaskDetails.this, "Date fields cannot be empty", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            Date date = sdf.parse(subtaskDate);
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            if (date.before(start) || date.after(end)) {
                Toast.makeText(SubtaskDetails.this, "Error: Subtask must be between Task start and end dates.", Toast.LENGTH_SHORT).show();
                return -1;
            }
        } catch (ParseException e) {
            Toast.makeText(SubtaskDetails.this, "Invalid date format. Please use MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Subtask subtask;
        repository = new Repository(getApplication());
        if (item.getItemId() == R.id.save_subtask) {
            if (checkValidDate() == -1) {
            }
            else if (editName.getText().toString().isEmpty()) {
                Toast.makeText(SubtaskDetails.this, "Subtask name cannot be empty", Toast.LENGTH_SHORT).show();
                return true;
            }
            // If new subtask, get next subtask ID and create new subtask from input fields
            else if (subtaskID == -1) {
                if (repository.getmAllSubtasks().size() == 0) {
                    subtaskID = 1;
                } else {
                    subtaskID = repository.getmAllSubtasks().get(repository.getmAllSubtasks().size() - 1).getSubtaskID() + 1;
                }
                subtask = new Subtask(subtaskID,
                        editName.getText().toString(),
                        editDate.getText().toString(), taskID,
                        editNote.getText().toString());
                Toast.makeText(SubtaskDetails.this, "Adding Subtask", Toast.LENGTH_SHORT).show();
                repository.insert(subtask);
            } else {
                subtask = new Subtask(subtaskID,
                        editName.getText().toString(),
                        editDate.getText().toString(), taskID,
                        editNote.getText().toString()
                );
                Toast.makeText(SubtaskDetails.this, "Updating Subtask", Toast.LENGTH_SHORT).show();
                repository.update(subtask);
            }
            isSubtaskSaved = true;
            return true;
        }
        if (item.getItemId() == R.id.delete_subtask) {
            if(isSubtaskSaved == false){
                Toast.makeText(SubtaskDetails.this, "Cannot delete subtask that has not been saved", Toast.LENGTH_SHORT).show();
                return true;
            }
            subtask = new Subtask(subtaskID,
                    editName.getText().toString(),
                    editDate.getText().toString(), taskID,
                    editNote.getText().toString()
            );
            Toast.makeText(SubtaskDetails.this, "Deleting Subtask", Toast.LENGTH_SHORT).show();
            repository.delete(subtask);
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.notify_date) {
            if(isSubtaskSaved == false){
                Toast.makeText(SubtaskDetails.this, "Cannot notify subtask that has not been saved", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (checkValidDate() == -1) {
                return true;
            }
            String dateFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

            subtaskDate = editDate.getText().toString();

            if (subtaskDate == null || subtaskDate.isEmpty()){
                Toast.makeText(SubtaskDetails.this, "Date fields cannot be empty", Toast.LENGTH_SHORT).show();
                return true;
            }

            Date date = null;
            try {
                date = sdf.parse(subtaskDate);
            } catch (ParseException e) {
                Toast.makeText(SubtaskDetails.this, "Invalid date format. Please use MM/DD/YYYY", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return true;
            }

            Long triggerTime = date.getTime();
            Intent intent = new Intent(SubtaskDetails.this, MyReceiver.class);
            intent.putExtra("name", editName.getText().toString());
            intent.putExtra("date", editDate.getText().toString());

            PendingIntent sender = PendingIntent.getBroadcast(SubtaskDetails.this, ++Main.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
            Toast.makeText(SubtaskDetails.this, editName.getText().toString() + " notification set for " + editDate.getText().toString(), Toast.LENGTH_SHORT).show();
            return true;        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }
}