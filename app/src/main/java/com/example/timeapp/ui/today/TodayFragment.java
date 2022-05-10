package com.example.timeapp.ui.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.databinding.FragmentHomeBinding;
import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.ui.NewTaskActivity;

import java.util.ArrayList;
import java.util.List;

public class TodayFragment extends Fragment {
    String TAG = "HomeFragment";
    private RecyclerViewAdapter adapter;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private TodayViewModel todayViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewTaskActivity.class));
            }
        });
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);


        final TextView textView = binding.textHome;
        todayViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        todayViewModel.getTasks().observe(getViewLifecycleOwner(), this::setRecyclerView);
        //todayViewModel.getTodaysTasks().observe(getViewLifecycleOwner(), this::setRecyclerView);

        return root;
    }

    private void setRecyclerView(List<TaskEntity> taskEntities) {
        adapter = new RecyclerViewAdapter(taskEntities);
        recyclerView.setAdapter(adapter);
    }

//    private void addNewTask() {
//        TaskEntity task = new TaskEntity();
//        task.setTaskName("Task 1");
//        task.setTaskDescription("Description 1");
//        task.setTaskDate(LocalDate.now());
//        task.setTaskTime(OffsetTime.now());
//        homeViewModel.addTask(task);
//        Log.d(TAG, "addNewTask: ");
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}