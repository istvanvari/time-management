package com.example.timeapp.ui.routines;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.databinding.FragmentRoutinesBinding;
import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.ui.today.EditTaskActivity;
import com.example.timeapp.ui.today.TaskViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoutinesFragment extends Fragment implements RoutinesRecyclerViewAdapter.RoutinesClickListener {
    private static final String TAG = "RoutinesFragment";
    int today = LocalDate.now().getDayOfWeek().getValue();
    int day = today;
    private RecyclerView recyclerView;
    private List<TaskEntity> tasks = new ArrayList<>();
    private RoutinesRecyclerViewAdapter adapter;
    private FragmentRoutinesBinding binding;
    private TaskViewModel routinesViewModel;
    private ChipGroup chipGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routinesViewModel =
                new ViewModelProvider(this).get(TaskViewModel.class);

        binding = FragmentRoutinesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(view ->
                startActivity(new Intent(getContext(), EditTaskActivity.class).putExtra("action", 0).putExtra("task_type", 1)));
        chipGroup = binding.chipGroup;

        recyclerView = binding.recyclerViewRoutines;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        adapter = new RoutinesRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        routinesViewModel.getRepeatedTasks().observe(getViewLifecycleOwner(), list -> {
                    tasks = list;
                    adapter.updateData(list, day);
                }
        );
        setListeners();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                routinesViewModel.deleteTask(adapter.getRoutineAt(viewHolder.getAdapterPosition()));
                routinesViewModel.deleteAlarm(adapter.getRoutineAt(viewHolder.getAdapterPosition()), getContext());
                Snackbar.make(root, "Routine task deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", view -> {
                            routinesViewModel.addTask(routinesViewModel.getCache());
                            routinesViewModel.setAlarm(routinesViewModel.getCache(), getContext());
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        chipGroup.check(chipGroup.getChildAt(today).getId());
        adapter.updateData(tasks, today);

        return root;
    }


    private void setListeners() {
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1)
                return;
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    day = i;
                    break;
                }
            }
            adapter.updateData(tasks, day);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRoutinesClick(int id) {
        startActivity(new Intent(getContext(), EditTaskActivity.class).putExtra("action", 1).putExtra("id", id));
    }
}