package com.example.timeapp.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeapp.R;
import com.example.timeapp.databinding.FragmentCalendarBinding;
import com.example.timeapp.db.TaskEntity;
import com.example.timeapp.ui.today.EditTaskActivity;
import com.example.timeapp.ui.today.TaskViewModel;
import com.example.timeapp.util.ClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class CalendarFragment extends Fragment implements ClickListener {

    LocalDate date = null;
    TaskViewModel calendarViewModel;
    CalendarAdapter adapter;
    private FragmentCalendarBinding binding;
    private CalendarView calendar;
    private RecyclerView recyclerView;
    private TextView textView;
    private List<TaskEntity> tasks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(view -> openAddTaskActivity());
        binding.fab2.setOnClickListener(view -> openDayFragment());

        textView = binding.textView;

        calendar = binding.calendarView;
        calendar.setMinDate(System.currentTimeMillis());


        recyclerView = binding.recyclerView;
        adapter = new CalendarAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            date = LocalDate.of(year, month + 1, dayOfMonth);
            Log.d("date", "onCreateView: " + year + " " + month + " " + dayOfMonth);
            textView.setText(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
            adapter.updateData(tasks, date.toString());
        });

        calendarViewModel.getTasksSorted().observe(getViewLifecycleOwner(), list -> {
            tasks = list;
            adapter.updateData(list, date == null ? LocalDate.now().toString() : date.toString());
        });

        textView.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));

        return root;
    }

    private void openDayFragment() {
        calendarViewModel.setDay(date);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_calendar_to_nav_today);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openEditTaskActivity(int id) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra("id", id).putExtra("action", 1);
        startActivity(intent);
    }

    public void openAddTaskActivity() {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra("action", 0);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        this.date = calendarViewModel.getDay().getValue() == null ? LocalDate.now() : calendarViewModel.getDay().getValue();
        textView.setText(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        calendarViewModel.getTasksSorted().observe(getViewLifecycleOwner(), list -> {
            tasks = list;
            adapter.updateData(list, date.toString());
            calendar.setDate(Instant.ofEpochMilli(date.toEpochDay() * 86400000).toEpochMilli(), true, true);
        });
        super.onResume();
    }
}