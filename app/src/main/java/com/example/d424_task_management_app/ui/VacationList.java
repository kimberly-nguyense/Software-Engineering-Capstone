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
import com.example.d424_task_management_app.entities.Excursion;
import com.example.d424_task_management_app.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton_addVacation);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            intent.putExtra("isVacationSaved", false); // Starting a new vacation
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.vacationRecyclerView);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_data) {
            repository = new Repository(getApplication());
            repository.deleteAll();
            Toast.makeText(VacationList.this, "All Data Deleted", Toast.LENGTH_SHORT).show();
            onResume();
        }
        if (item.getItemId() == R.id.add_sample_data) {
            repository = new Repository(getApplication());
            Vacation vacation = new Vacation(0, "Dallas", "Marriott", "05/23/2024", "06/15/2024");
            int vacationID = (int) repository.insert(vacation);
            Excursion excursion = new Excursion(0, "Art Gallery", "05/23/2024", vacationID, "Art Gallery of Dallas");
            repository.insert(excursion);

            vacation = new Vacation(0, "Carrollton", "Courtyard", "05/23/2024", "06/15/2024");
            vacationID = (int) repository.insert(vacation);
            excursion = new Excursion(0, "Museum", "05/23/2024", vacationID, "Museum of Art");
            repository.insert(excursion);
            Toast.makeText(VacationList.this, "Sample Data Added", Toast.LENGTH_SHORT).show();
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.vacationRecyclerView);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }
}