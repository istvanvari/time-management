package com.example.timeapp.ui.today;

import android.content.Intent;
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

import com.example.timeapp.databinding.FragmentHomeBinding;
import com.example.timeapp.ui.EditTaskActivity;
import com.example.timeapp.ui.NewTaskActivity;

public class TodayFragment extends Fragment implements RecyclerViewAdapter.ClickListener {
    String TAG = "HomeFragment";
    private RecyclerViewAdapter adapter;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private TaskViewModel taskViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(view -> startActivity(new Intent(getContext(), NewTaskActivity.class)));

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);


        final TextView textView = binding.textHome;
        taskViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), list -> adapter.updateData(list));
//        taskViewModel.getTodaysTasks().observe(getViewLifecycleOwner(), this::setRecyclerView);

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