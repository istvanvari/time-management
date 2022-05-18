package com.example.timeapp.ui.today;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.timeapp.databinding.FragmentTodayBinding;
import com.google.android.material.snackbar.Snackbar;

public class TodayFragment extends Fragment implements RecyclerViewAdapter.ClickListener {
    String TAG = "HomeFragment";
    private RecyclerViewAdapter adapter;
    private FragmentTodayBinding binding;
    private RecyclerView recyclerView;
    private TaskViewModel taskViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        binding = FragmentTodayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(view -> startActivity(new Intent(getContext(), NewTaskActivity.class)));

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        taskViewModel.getTodayTasks().observe(getViewLifecycleOwner(), list -> adapter.updateData(list));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.deleteTask(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Snackbar.make(root, "Task deleted", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        return root;
    }

    public void openEditTaskActivity(int id) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
//        adapter.updateData(taskViewModel.getTasks().getValue());
        Log.d(TAG, "onResume: ");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}