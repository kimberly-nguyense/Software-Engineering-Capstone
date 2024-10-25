package com.example.d424_task_management_app.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.entities.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> mTasks;
    private final Context context;
    private final LayoutInflater mInflater;

    public TaskAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskItemView;
        private final TextView taskItemView2;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskItemView = itemView.findViewById(R.id.textView1);
            taskItemView2 = itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Task task = mTasks.get(position);
                Intent intent = new Intent(context, TaskDetails.class);
                intent.putExtra("taskID", task.getTaskID());
                intent.putExtra("taskName", task.getTaskName());
                intent.putExtra("taskHotelName", task.getHotelName());
                intent.putExtra("taskStartDate", task.getStartDate());
                intent.putExtra("taskEndDate", task.getEndDate());
                intent.putExtra("taskID", task.getTaskID());
                intent.putExtra("isTaskSaved", true);
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
        if(mTasks != null) {
            Task current = mTasks.get(position);
            holder.taskItemView.setText(current.getTaskName());
            holder.taskItemView2.setText(current.getStartDate() + " - " + current.getEndDate());
        } else {
            holder.taskItemView.setText("No Task Name");
            holder.taskItemView2.setText("No Task Date");
        }
    }

    @Override
    public int getItemCount() {
        if (mTasks == null) {
            return 0;
        }
        return mTasks.size();
    }

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

}
