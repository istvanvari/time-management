package com.example.timeapp.ui.calendar;

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
import com.example.timeapp.util.ClickListener;
import com.example.timeapp.util.DiffUtilCallbacks;
import com.google.android.material.chip.Chip;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final ClickListener listener;
    private List<TaskEntity> tasks = new ArrayList<>();

    public CalendarAdapter(ClickListener listener) {
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
        holder.taskName.setText(task.getName());
        holder.taskDescription.setText(task.getDescription());
        holder.chip.setVisibility(View.GONE);
        holder.taskTime.setText(task.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
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

    public void updateData(List<TaskEntity> tasks, String date) {
        tasks = filter(tasks, date);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallbacks(this.tasks, tasks));
        result.dispatchUpdatesTo(this);
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    private List<TaskEntity> filter(List<TaskEntity> tasks, String date) {
        List<TaskEntity> filtered = new ArrayList<>();
        for (TaskEntity task : tasks) {
            if (task.getDate().equals(date) && !task.isRepeated()) {
                filtered.add(task);
            }
        }
        return filtered;
    }


    public TaskEntity getTaskAt(int position) {
        return tasks.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskName;
        public TextView taskDescription;
        public TextView taskTime;
        public Chip chip;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_desc);
            taskTime = itemView.findViewById(R.id.task_time);
            chip = itemView.findViewById(R.id.repeated_tag);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(v -> listener.openEditTaskActivity(tasks.get(getAdapterPosition()).getId()));
        }
    }
}
