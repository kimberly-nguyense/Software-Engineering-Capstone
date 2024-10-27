package com.example.d424_task_management_app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.database.Repository;
import com.example.d424_task_management_app.entities.Subtask;

import java.util.List;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder> {
    private String taskName;
    private String taskStart;
    private String taskEnd;
    private List<Subtask> subtaskList;
    private final Context context;
    private final LayoutInflater mInflater;
    private final Repository repository;

    public SubtaskAdapter(Context context, Repository repository) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.repository = repository;
    }

    public class SubtaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView subtaskItemView;
        private final TextView subtaskItemView2;
        private final CheckBox isCompleted;

        public SubtaskViewHolder(@NonNull View itemView) {
            super(itemView);
            subtaskItemView = itemView.findViewById(R.id.text_taskName);
            subtaskItemView2 = itemView.findViewById(R.id.text_dueDate);
            isCompleted = itemView.findViewById(R.id.isCompleted);
            isCompleted.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Subtask subtask = subtaskList.get(position);
                subtask.setCompleted(!subtask.isCompleted());
                if (subtask.isCompleted()) {
                    subtask.setTimestampCompleted(System.currentTimeMillis());
                }else{
                    subtask.setTimestampCompleted(0);
                }
                repository.update(subtask);
            });
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Subtask subtask = subtaskList.get(position);
                Intent intent = new Intent(context, SubtaskDetails.class);
                intent.putExtra("subtaskID", subtask.getSubtaskID());
                intent.putExtra("subtaskName", subtask.getSubtaskName());
                intent.putExtra("subtaskDate", subtask.getSubtaskDate());
                intent.putExtra("taskID", subtask.getTaskID());
                intent.putExtra("subtaskNote", subtask.getNote());
                taskName = ((Activity) context).getIntent().getStringExtra("taskName");
                taskStart = ((Activity) context).getIntent().getStringExtra("taskStart");
                taskEnd = ((Activity) context).getIntent().getStringExtra("taskEnd");
                intent.putExtra("taskName", taskName);
                intent.putExtra("taskStart", taskStart);
                intent.putExtra("taskEnd", taskEnd);
                intent.putExtra("isSubtaskSaved", true);
                intent.putExtra("isSubtaskCompleted", subtask.isCompleted());
                context.startActivity(intent);
            });
        }
    }
    @NonNull
    @Override
    public SubtaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.subtask_list_item, parent, false);
        return new SubtaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubtaskViewHolder holder, int position) {
        if(subtaskList != null) {
            Subtask current = subtaskList.get(position);
            holder.subtaskItemView.setText(current.getSubtaskName());
            holder.subtaskItemView2.setText(current.getSubtaskDate());
            holder.isCompleted.setChecked(current.isCompleted());
        } else {
            holder.subtaskItemView.setText("No Subtask Name");
            holder.subtaskItemView2.setText("No Subtask Date");
            holder.isCompleted.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (subtaskList == null) {
            return 0;
        }
        return subtaskList.size();
    }

    public void setSubtasks(List<Subtask> subtasks) {
        subtaskList = subtasks;
        notifyDataSetChanged();
    }

}
