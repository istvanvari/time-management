package com.example.timeapp.ui.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.databinding.FragmentRoutinesBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class RoutinesFragment extends Fragment implements RoutinesRecyclerViewAdapter.RoutinesClickListener {
    private static final String TAG = "RoutinesFragment";
    int today = LocalDate.now().getDayOfWeek().getValue();
    private RecyclerView recyclerView;
    private RoutinesRecyclerViewAdapter adapter;
    private FragmentRoutinesBinding binding;
    private RoutinesViewModel routinesViewModel;
    private ChipGroup chipGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routinesViewModel =
                new ViewModelProvider(this).get(RoutinesViewModel.class);

        binding = FragmentRoutinesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(view ->
                startActivity(new Intent(getContext(), NewEditRoutineActivity.class).putExtra("type", "new")));
        chipGroup = binding.chipGroup;

        recyclerView = binding.recyclerViewRoutines;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new RoutinesRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        routinesViewModel.getRoutines().observe(getViewLifecycleOwner(), list -> adapter.updateData(list));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                routinesViewModel.deleteRoutine(adapter.getRoutineAt(viewHolder.getAdapterPosition()));
                Snackbar.make(root, "Routine deleted", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "checkedId: " + checkedId);
            if (checkedId == -1) {
                return;
            }
            Chip chip = (Chip) chipGroup.findViewById(checkedId);
            String chiptext = chip.getText().toString().toUpperCase();
            int day;
            if (chiptext.equals("DAILY")) {
                day = 0;
            } else {
                day = DayOfWeek.valueOf(chiptext).getValue();
            }
            adapter.getFilter().filter(String.valueOf(day));
        });

        chipGroup.check(today + 1);
        adapter.getFilter().filter(String.valueOf(today + 1));

        return root;
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
        startActivity(new Intent(getContext(), NewEditRoutineActivity.class).putExtra("type", "edit").putExtra("id", id));
    }
}