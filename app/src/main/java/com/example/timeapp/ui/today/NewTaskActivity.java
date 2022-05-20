package com.example.timeapp.ui.today;

import static android.text.format.DateFormat.is24HourFormat;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.timeapp.R;
import com.example.timeapp.db.TaskEntity;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class NewTaskActivity extends AppCompatActivity {
    private static final String TAG = "NewTaskActivity";
    LocalDate date;
    OffsetTime time;
    Button addButton;
    TextInputEditText taskName, taskDescription, taskDate, taskTime;
    Chip chipToday, chipTomorrow;
    TextInputLayout taskNameLayout, taskDescriptionLayout, taskDateLayout, taskTimeLayout;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);


        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        date = null;
        time = null;

        addButton.setOnClickListener(v -> addTask());
        chipToday.setOnClickListener(v -> {
            date = LocalDate.now();
            taskDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        });
        chipTomorrow.setOnClickListener(v -> {
            date = LocalDate.now().plusDays(1);
            taskDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        });

        taskDate.setOnClickListener(v -> onInputDate());
        taskTime.setOnClickListener(v -> onInputTime());
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

        addButton = findViewById(R.id.add_button);
        chipToday = findViewById(R.id.chip_today);
        chipTomorrow = findViewById(R.id.chip_tomorrow);
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
            taskDateLayout.setError(null);

            if (date.toString().equals(LocalDate.now().toString())) {
                chipToday.setChecked(true);
                chipTomorrow.setChecked(false);
            } else if (date.toString().equals(LocalDate.now().plusDays(1).toString())) {
                chipTomorrow.setChecked(true);
                chipToday.setChecked(false);
            } else {
                chipToday.setChecked(false);
                chipTomorrow.setChecked(false);
            }
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
            taskTimeLayout.setError(null);
        });
    }

    public void addTask() {
        if (!validInput()) return;
        try {
            String name = taskName.getText().toString();
            String description = taskDescription.getText().toString();

            TaskEntity task = new TaskEntity(name, description, date.toString(), time, false,0, 0, false);
            taskViewModel.addTask(task);
            Snackbar.make(addButton, "Task added", Snackbar.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Snackbar.make(addButton, "Error", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean validInput() {
        if (date == null) {
            taskDateLayout.setError("Please select a date");
            return false;
        }
        if (time == null) {
            taskTimeLayout.setError("Please select a time");
            return false;
        }
        return true;
    }
}