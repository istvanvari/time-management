package com.example.timeapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.Repository.RepositoryImpl;
import com.example.timeapp.databinding.FragmentHomeBinding;
import com.example.timeapp.db.TaskEntity;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    String TAG = "HomeFragment";
    static RepositoryImpl repository;
    private RecyclerViewAdapter adapter;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addNewTask();
            }
        });
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        repository = RepositoryImpl.getInstance();


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        homeViewModel.getTasks().observe(getViewLifecycleOwner(), this::setRecyclerView);

        return root;
    }

    private void setRecyclerView(List<TaskEntity> taskEntities) {
        adapter = new RecyclerViewAdapter(taskEntities);
        recyclerView.setAdapter(adapter);
    }

    private void addNewTask() {
        TaskEntity task = new TaskEntity();
        task.setTaskName("Task 1");
        task.setTaskDescription("Description 1");
        task.setTaskDate(LocalDate.now());
        task.setTaskTime(OffsetTime.now());
        homeViewModel.addTask(task);
        Log.d(TAG, "addNewTask: ");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}