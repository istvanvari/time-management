package com.example.timeapp.ui.routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.R;
import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.util.DiffUtilCallbacks;
import com.google.android.material.chip.Chip;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RoutinesRecyclerViewAdapter extends RecyclerView.Adapter<RoutinesRecyclerViewAdapter.RoutinesViewHolder> {

    private final RoutinesRecyclerViewAdapter.RoutinesClickListener listener;
    private List<TaskEntity> routines = new ArrayList<>();

    public RoutinesRecyclerViewAdapter(RoutinesRecyclerViewAdapter.RoutinesClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new RoutinesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {
        TaskEntity routine = routines.get(position);
        holder.routineName.setText(routine.getName());
        holder.routineDescription.setText(routine.getDescription());
        holder.routineTime.setText(routine.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        holder.repeated_chip.setVisibility(routine.isRepeated() ? View.VISIBLE : View.GONE);
        holder.reminder_chip.setVisibility(routine.getReminderType() != 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position);
        else {
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey("routineName")) {
                holder.routineName.setText(bundle.getString("routineName"));
            }
            if (bundle.containsKey("routineDescription")) {
                holder.routineDescription.setText(bundle.getString("routineDescription"));
            }
            if (bundle.containsKey("routineTime")) {
                holder.routineTime.setText(bundle.getString("routineTime"));
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
        return routines.size();
    }

    public void updateData(List<TaskEntity> newRoutines, int day) {
        newRoutines = filter(newRoutines, day);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallbacks(this.routines, newRoutines));
        this.routines.clear();
        this.routines.addAll(newRoutines);
        result.dispatchUpdatesTo(this);
    }

    private List<TaskEntity> filter(List<TaskEntity> list, int day) {
        List<TaskEntity> filteredList = new ArrayList<>();
        for (TaskEntity t : list) {
            if (t.getDay() == day) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }

    public TaskEntity getRoutineAt(int position) {
        return routines.get(position);
    }

    public interface RoutinesClickListener {
        void onRoutinesClick(int id);
    }

    public class RoutinesViewHolder extends RecyclerView.ViewHolder {

        public TextView routineName, routineDescription, routineTime;
        public Chip reminder_chip, repeated_chip;

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            routineName = itemView.findViewById(R.id.task_name);
            routineDescription = itemView.findViewById(R.id.task_desc);
            routineTime = itemView.findViewById(R.id.task_time);
            reminder_chip = itemView.findViewById(R.id.reminder_tag);
            repeated_chip = itemView.findViewById(R.id.repeated_tag);
            itemView.setOnClickListener(v -> listener.onRoutinesClick(routines.get(getAdapterPosition()).getId()));
        }
    }
}