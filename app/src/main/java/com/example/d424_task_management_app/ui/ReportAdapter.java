package com.example.d424_task_management_app.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.entities.Task;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Task> tasks;
    private LayoutInflater mInflater;
    private String currentDateTimestamp;

    public ReportAdapter(List<Task> tasks, LayoutInflater mInflater, String currentDateTimestamp) {
        this.tasks = tasks;
        this.mInflater = mInflater;
        this.currentDateTimestamp = currentDateTimestamp;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView text_taskName;
        private final TextView text_startDate;
        private final TextView text_endDate;
        private final TextView text_taskCount;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            text_taskName = itemView.findViewById(R.id.text_taskName);
            text_startDate = itemView.findViewById(R.id.text_startDate);
            text_endDate = itemView.findViewById(R.id.text_endDate);
            text_taskCount = itemView.findViewById(R.id.text_taskCount);
        }
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.report_list_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Task current = tasks.get(position);
        if (current == null) {
            holder.text_taskName.setText("No Task Name");
            holder.text_startDate.setText("No Task Start Date");
            holder.text_endDate.setText("No Task End Date");
            holder.text_taskCount.setText("0");
        }else{
            holder.text_taskName.setText(current.getTaskName());
            holder.text_startDate.setText(current.getStartDate());
            holder.text_endDate.setText(current.getEndDate());
            holder.text_taskCount.setText(String.valueOf(position + 1));
        }
    }

    @Override
    public int getItemCount() {
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }
}
