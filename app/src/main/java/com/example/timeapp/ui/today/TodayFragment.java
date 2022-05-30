package com.example.timeapp.ui.today;

import static com.example.timeapp.util.Constants.ACTION_TYPE_EDIT;
import static com.example.timeapp.util.Constants.ACTION_TYPE_NEW;

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

import com.example.timeapp.databinding.FragmentTodayBinding;
import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.util.ClickListener;
import com.google.android.material.snackbar.Snackbar;

public class TodayFragment extends Fragment implements ClickListener {
    String TAG = "HomeFragment";
    private RecyclerViewAdapter adapter;
    private FragmentTodayBinding binding;
    private RecyclerView recyclerView;
    private TaskViewModel taskViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        binding = FragmentTodayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(view -> openAddTaskActivity());

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(this, taskViewModel.getDay().getValue());
        recyclerView.setAdapter(adapter);

        taskViewModel.getTasks().observe(getViewLifecycleOwner(), list -> adapter.updateData(list));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                TaskEntity task = adapter.getTaskAt(viewHolder.getAdapterPosition());

                if (direction == ItemTouchHelper.RIGHT) {
                    taskViewModel.deleteTask(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                    taskViewModel.deleteAlarm(adapter.getTaskAt(viewHolder.getAdapterPosition()), getContext());
                    Snackbar.make(root, "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", view -> {
                                taskViewModel.addTask(taskViewModel.getCache());
                                taskViewModel.setAlarm(taskViewModel.getCache(), getContext());
                            })
                            .show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    if (task.isCompleted()) {
                        task.setCompleted(false);
                        taskViewModel.updateTask(task);
                        taskViewModel.setAlarm(task, getContext());
                        Snackbar.make(root, "Task marked as incomplete", Snackbar.LENGTH_SHORT).show();
                    } else {
                        task.setCompleted(true);
                        taskViewModel.updateTask(task);
                        Snackbar.make(root, "Task marked as complete", Snackbar.LENGTH_SHORT).show();
                    }
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
        }).attachToRecyclerView(recyclerView);


        return root;
    }

    public void openEditTaskActivity(int id) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra("id", id).putExtra("action", ACTION_TYPE_EDIT);
        startActivity(intent);
    }

    public void openAddTaskActivity() {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra("action", ACTION_TYPE_NEW);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}