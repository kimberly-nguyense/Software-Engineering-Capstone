package com.example.d424_task_management_app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424_task_management_app.R;
import com.example.d424_task_management_app.entities.Subtask;

import java.util.List;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder> {
    private String taskName;
    private String taskStart;
    private String taskEnd;
    private List<Subtask> mSubtasks;
    private final Context context;
    private final LayoutInflater mInflater;

    public SubtaskAdapter(Context context, String taskName, String taskStart, String taskEnd) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
        this.taskName = taskName;
    }

    public class SubtaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView subtaskItemView;
        private final TextView subtaskItemView2;

        public SubtaskViewHolder(@NonNull View itemView) {
            super(itemView);
            subtaskItemView = itemView.findViewById(R.id.textView2);
            subtaskItemView2 = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Subtask subtask = mSubtasks.get(position);
                Intent intent = new Intent(context, SubtaskDetails.class);
                intent.putExtra("subtaskID", subtask.getSubtaskID());
                intent.putExtra("subtaskName", subtask.getSubtaskName());
                intent.putExtra("subtaskDate", subtask.getSubtaskDate());
                intent.putExtra("taskID", subtask.getTaskID());
                intent.putExtra("subtaskNote", subtask.getNote());
                taskName = ((Activity) context).getIntent().getStringExtra("taskName");
                intent.putExtra("taskName", taskName);
                intent.putExtra("taskStart", taskStart);
                intent.putExtra("taskEnd", taskEnd);
                intent.putExtra("isSubtaskSaved", true);
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
        if(mSubtasks != null) {
            Subtask current = mSubtasks.get(position);
            holder.subtaskItemView.setText(current.getSubtaskName());
            holder.subtaskItemView2.setText(current.getSubtaskDate());
        } else {
            holder.subtaskItemView.setText("No Subtask Name");
            holder.subtaskItemView2.setText("No Subtask Date");

        }
    }

    @Override
    public int getItemCount() {
        if (mSubtasks == null) {
            return 0;
        }
        return mSubtasks.size();
    }

    public void setSubtasks(List<Subtask> subtasks) {
        mSubtasks = subtasks;
        notifyDataSetChanged();
    }

}
