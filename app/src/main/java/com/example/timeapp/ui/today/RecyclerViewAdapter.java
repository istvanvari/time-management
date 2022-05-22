package com.example.timeapp.ui.today;

import android.os.Bundle;
import android.util.Log;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private final ClickListener listener;
    private final LocalDate today;
    private List<TaskEntity> tasks = new ArrayList<>();

    public RecyclerViewAdapter(ClickListener listener, LocalDate today) {
        this.listener = listener;
        this.today = today == null ? LocalDate.now() : today;
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
        holder.taskTime.setText(task.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        holder.repeated_chip.setVisibility(task.isRepeated() ? View.VISIBLE : View.GONE);
        holder.reminder_chip.setVisibility(task.getReminderType() != 0 ? View.VISIBLE : View.GONE);
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
            if (bundle.containsKey("repeated")) {
                holder.repeated_chip.setVisibility(bundle.getBoolean("repeated") ? View.VISIBLE : View.GONE);
            }
            if (bundle.containsKey("reminderType")) {
                holder.reminder_chip.setVisibility(bundle.getBoolean("reminderType") ? View.VISIBLE : View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateData(List<TaskEntity> tasks) {
        tasks = filter(tasks);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallbacks(this.tasks, tasks));
        result.dispatchUpdatesTo(this);
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    private List<TaskEntity> filter(List<TaskEntity> tasks) {
        String todayString = today.toString();
        int todayWeekDay = today.getDayOfWeek().getValue();
        TemporalField tf = WeekFields.ISO.weekOfWeekBasedYear();
        int todayWeekNumber = today.get(tf);

        Log.d(TAG, "filter: " + todayString + " " + todayWeekDay + " " + todayWeekNumber);

        List<TaskEntity> filteredTasks = new ArrayList<>();
        for (TaskEntity task : tasks) {
            if (task.getDate().equals(todayString)) {
                filteredTasks.add(task);
                continue;
            }
            if (task.isRepeated()) {
                int taskPeriod = task.getPeriod();
                if (taskPeriod == 0) {
                    filteredTasks.add(task);
                } else if (task.getDay() == todayWeekDay) {
                    if (taskPeriod == 1) {
                        filteredTasks.add(task);
                    } else {
                        int taskWeekNumber = LocalDate.parse(task.getDate()).get(tf);
                        if (taskWeekNumber % taskPeriod == todayWeekNumber % taskPeriod) {
                            filteredTasks.add(task);
                        }
                    }
                }
            }
        }
        return filteredTasks;
    }

    public TaskEntity getTaskAt(int position) {
        return tasks.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskName;
        public TextView taskDescription;
        public TextView taskTime;
        public CardView cardView;
        public Chip repeated_chip, reminder_chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_desc);
            taskTime = itemView.findViewById(R.id.task_time);
            cardView = itemView.findViewById(R.id.card_view);
            repeated_chip = itemView.findViewById(R.id.repeated_tag);
            reminder_chip = itemView.findViewById(R.id.reminder_tag);
            cardView.setOnClickListener(v -> listener.openEditTaskActivity(tasks.get(getAdapterPosition()).getId()));
        }
    }
}
