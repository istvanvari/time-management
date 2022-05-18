package com.example.timeapp.ui.today;

import static android.text.format.DateFormat.is24HourFormat;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.timeapp.R;
import com.example.timeapp.db.TaskEntity;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskActivity";
    TaskViewModel taskViewModel;
    TextInputEditText taskName, taskDescription, taskDate, taskTime;
    TextInputLayout taskNameLayout, taskDescriptionLayout, taskDateLayout, taskTimeLayout;
    Button saveButton, cancelButton;
    LocalDate date;
    OffsetTime time;
    private TaskEntity task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        date = null;
        time = null;

        int id = getIntent().getIntExtra("id", -1);
        task = taskViewModel.getTask(id).getValue();

        initViews();
        taskTime.setOnClickListener(v -> onInputTime());
        taskDate.setOnClickListener(v -> onInputDate());
        saveButton.setOnClickListener(v -> saveTask());
        cancelButton.setOnClickListener(v -> onBackPressed());
    }

    private void saveTask() {
        task.setTaskName(taskName.getText().toString());
        task.setTaskDescription(taskDescription.getText().toString());
        task.setTaskDate(date != null ? date : task.getTaskDate());
        task.setTaskTime(time != null ? time : task.getTaskTime());
        taskViewModel.updateTask(task);

        onBackPressed();
    }

    private void initViews() {
        taskNameLayout = findViewById(R.id.task_name_layout);
        taskDescriptionLayout = findViewById(R.id.task_description_layout);
        taskDateLayout = findViewById(R.id.task_date_layout);
        taskTimeLayout = findViewById(R.id.task_time_layout);

        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.task_description);
        taskDate = findViewById(R.id.task_date);
        taskTime = findViewById(R.id.task_time);

        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

        taskName.setText(task.getTaskName());
        taskDescription.setText(task.getTaskDescription());
        taskDate.setText(task.getTaskDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        taskTime.setText(task.getTaskTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    private void onInputDate() {
        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setCalendarConstraints(calendarConstraintBuilder.build());
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

        final MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");


        datePicker.addOnPositiveButtonClickListener(selection -> {
            this.date = Instant.ofEpochMilli(selection).atOffset(OffsetTime.now().getOffset()).toLocalDate();
            taskDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        });
    }

    private void onInputTime() {
        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTimeFormat(is24HourFormat(this) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H);
        MaterialTimePicker timePicker = builder.build();
        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");

        timePicker.addOnPositiveButtonClickListener(selection -> {
            this.time = OffsetTime.of(timePicker.getHour(), timePicker.getMinute(), 0, 0, OffsetTime.now().getOffset());
            taskTime.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
        });
    }
}