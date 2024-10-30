package com.example.d424_task_management_app.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.database.Repository;
import com.example.d424_task_management_app.entities.Subtask;
import com.example.d424_task_management_app.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private List<Task> filteredTaskList;
    private final Context context;
    private final LayoutInflater mInflater;
    private final Repository repository;

    public TaskAdapter(Context context, Repository repository) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.repository = repository;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskItemView;
        private final TextView taskItemView2;
        private final TextView taskItemView3;
        private final CheckBox isCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskItemView = itemView.findViewById(R.id.textView1);
            taskItemView2 = itemView.findViewById(R.id.text_startDate);
            taskItemView3 = itemView.findViewById(R.id.text_endDate);
            isCompleted = itemView.findViewById(R.id.isCompleted);
            isCompleted.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Task task = taskList.get(position);
                List<Subtask> incompleteSubtasks = repository.getIncompleteSubtasks(task.getTaskID());

                if (incompleteSubtasks.isEmpty()) {
                    task.setCompleted(!task.isCompleted());
                    if (task.isCompleted()) {
                        task.setTimestampCompleted(System.currentTimeMillis());
                    }else{
                        task.setTimestampCompleted(0);
                    }
                    repository.update(task);
                    Toast.makeText(context, "Task has been completed.", Toast.LENGTH_SHORT).show();
                }else{
                    isCompleted.setChecked(false);
                    task.setCompleted(false);
                    repository.update(task);
                    Toast.makeText(context, "Task cannot be completed until all subtasks are completed.", Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Task task = taskList.get(position);
                Intent intent = new Intent(context, TaskDetails.class);
                intent.putExtra("taskID", task.getTaskID());
                intent.putExtra("taskName", task.getTaskName());
                intent.putExtra("taskCategoryName", task.getCategoryName());
                intent.putExtra("taskStartDate", task.getStartDate());
                intent.putExtra("taskEndDate", task.getEndDate());
                intent.putExtra("taskID", task.getTaskID());
                intent.putExtra("isTaskSaved", true);
                intent.putExtra("isTaskCompleted", task.isCompleted());
                context.startActivity(intent);
            });
        }
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if(filteredTaskList != null) {
            Task current = filteredTaskList.get(position);
            holder.taskItemView.setText(current.getTaskName());
            holder.taskItemView2.setText(current.getStartDate());
            holder.taskItemView3.setText(current.getEndDate());
            holder.isCompleted.setChecked(current.isCompleted());
        } else {
            holder.taskItemView.setText("No Task Name");
            holder.taskItemView2.setText("No Task Start Date");
            holder.taskItemView3.setText("No Task End Date");
            holder.isCompleted.setChecked(false);
        }
    }

    public void filter(String query) {
        filteredTaskList = new ArrayList<>();
        query = query.toLowerCase();

        if (query.isEmpty()) {
            filteredTaskList.addAll(taskList);
        } else {
            for (Task task : taskList) {
                if (task.getTaskName().toLowerCase().contains(query)
                        || task.getCategoryName().toLowerCase().contains(query)) {
                    filteredTaskList.add(task);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (filteredTaskList == null) {
            return 0;
        }
        return filteredTaskList.size();
    }

    public void setTasks(List<Task> tasks) {
        taskList = tasks;
        filteredTaskList = new ArrayList<>(taskList);
        notifyDataSetChanged();
    }
}
