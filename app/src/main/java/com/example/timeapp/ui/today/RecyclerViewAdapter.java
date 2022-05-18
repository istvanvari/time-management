package com.example.timeapp.ui.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.R;
import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.util.DiffUtilCallbacks;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private final RecyclerViewAdapter.ClickListener listener;
    private List<TaskEntity> tasks = new ArrayList<>();

    public RecyclerViewAdapter(RecyclerViewAdapter.ClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskEntity task = tasks.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.taskDescription.setText(task.getTaskDescription());
        holder.taskTime.setText(task.getTaskTime().toString());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position);
        else {
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey("taskName")) {
                holder.taskName.setText(bundle.getString("taskName"));
            }
            if (bundle.containsKey("taskDescription")) {
                holder.taskDescription.setText(bundle.getString("taskDescription"));
            }
            if (bundle.containsKey("taskTime")) {
                holder.taskTime.setText(bundle.getString("taskTime"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateData(List<TaskEntity> tasks) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallbacks(this.tasks, tasks));
        result.dispatchUpdatesTo(this);
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    public TaskEntity getTaskAt(int position) {
        return tasks.get(position);
    }

    public interface ClickListener {
        void openEditTaskActivity(int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskName;
        public TextView taskDescription;
        public TextView taskTime;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_desc);
            taskTime = itemView.findViewById(R.id.task_time);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(v -> listener.openEditTaskActivity(tasks.get(getAdapterPosition()).getId()));
        }
    }
}
