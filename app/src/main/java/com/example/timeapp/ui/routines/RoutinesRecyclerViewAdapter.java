package com.example.timeapp.ui.routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.R;
import com.example.timeapp.db.RoutineEntity;
import com.example.timeapp.util.RoutineDiffUtilCallbacks;

import java.util.ArrayList;
import java.util.List;

public class RoutinesRecyclerViewAdapter extends RecyclerView.Adapter<RoutinesRecyclerViewAdapter.RoutinesViewHolder> implements Filterable {

    private final RoutinesRecyclerViewAdapter.RoutinesClickListener listener;
    private List<RoutineEntity> routines = new ArrayList<>();
    private List<RoutineEntity> filteredRoutines = new ArrayList<>();

    public RoutinesRecyclerViewAdapter(RoutinesRecyclerViewAdapter.RoutinesClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_routine, parent, false);
        return new RoutinesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {
        RoutineEntity routine = filteredRoutines.get(position);
        holder.routineName.setText(routine.getName());
        holder.routineDescription.setText(routine.getDescription());
        holder.routineTime.setText(routine.getTime());
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
        }
    }

    @Override
    public int getItemCount() {
        return filteredRoutines.size();
    }

    public void updateData(List<RoutineEntity> newRoutines) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new RoutineDiffUtilCallbacks(this.routines, newRoutines));
        result.dispatchUpdatesTo(this);
        this.routines.clear();
        this.routines.addAll(newRoutines);
    }

    public RoutineEntity getRoutineAt(int position) {
        return filteredRoutines.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredRoutines = routines;
                } else {
                    List<RoutineEntity> filteredList = new ArrayList<>();
                    for (RoutineEntity routine : routines) {
                        if (routine.getDay() == Integer.parseInt(charString)) {
                            filteredList.add(routine);
                        }
                    }
                    filteredRoutines = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredRoutines;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredRoutines = (List<RoutineEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface RoutinesClickListener {
        void onRoutinesClick(int id);
    }

    public class RoutinesViewHolder extends RecyclerView.ViewHolder {

        public TextView routineName, routineDescription, routineTime;

        public RoutinesViewHolder(@NonNull View itemView) {
            super(itemView);
            routineName = itemView.findViewById(R.id.routine_name);
            routineDescription = itemView.findViewById(R.id.routine_desc);
            routineTime = itemView.findViewById(R.id.routine_time);
            itemView.setOnClickListener(v -> listener.onRoutinesClick(filteredRoutines.get(getAdapterPosition()).getId()));
        }
    }
}