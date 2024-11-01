package com.example.d424_task_management_app;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

import com.example.d424_task_management_app.dao.CategoryDAO;
import com.example.d424_task_management_app.dao.TaskDAO;
import com.example.d424_task_management_app.dao.SubtaskDAO;
import com.example.d424_task_management_app.dao.UserDAO;
import com.example.d424_task_management_app.database.Repository;
import com.example.d424_task_management_app.entities.Task;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryTest {
    @Mock
    private TaskDAO taskDAO;
    @Mock
    private SubtaskDAO subtaskDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private CategoryDAO categoryDAO;
    private Repository repository;

    @Before
    public void setup() {
        repository = new Repository(taskDAO, subtaskDAO, userDAO, categoryDAO);
    }

    @Test
    public void createTask_withEmptyName_shouldNotCreateTask() {
        Task task = new Task();
        task.setTaskName("");
        task.setCategoryName("School");
        task.setStartDate("09/15/2024");
        task.setEndDate("10/31/2024");
        boolean result = repository.createTask(task);
        assertEquals(false, result);
    }

    @Test
    public void createTask_withValidData_shouldCreateTask() {
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setCategoryName("School");
        task.setStartDate("09/15/2024");
        task.setEndDate("10/31/2024");
        boolean result =  repository.createTask(task);
        assertEquals(true, result);
    }
}