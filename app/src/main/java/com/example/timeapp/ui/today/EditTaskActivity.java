package com.example.timeapp.ui.today;

import static android.text.format.DateFormat.is24HourFormat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import com.example.timeapp.R;
import com.example.timeapp.db.TaskEntity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
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
    private static String[] REMINDERS;
    private static String[] PERIODS;
    TaskViewModel taskViewModel;
    TextInputEditText taskName, taskDescription, taskDate, taskTime;
    TextInputLayout taskNameLayout, taskDescriptionLayout, taskDateLayout, taskTimeLayout;
    AutoCompleteTextView taskPeriod, taskReminder;
    Button saveButton, cancelButton;
    TextView subheader;
    Chip chipToday, chipTomorrow;
    ChipGroup chipGroupDate, chipGroupDay;
    SwitchMaterial taskRepeatSwitch, reminderSwitch;
    Group taskRepeatGroup, selectDayGroup, reminderGroup;
    boolean isRepeated = false, remindMe = false;
    int ACTION_TYPE, TASK_TYPE, day = -1, period = -1, reminder = -1, initialReminderType = -1;
    LocalDate date = null;
    OffsetTime time = null;
    private TaskEntity task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        int id = getIntent().getIntExtra("id", -1);
        ACTION_TYPE = getIntent().getIntExtra("action", -1);
        TASK_TYPE = getIntent().getIntExtra("task_type", -1);

        PERIODS = getResources().getStringArray(R.array.periods);
        REMINDERS = getResources().getStringArray(R.array.reminders);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        initViews();
        setListeners();

        if (ACTION_TYPE == 1) { // edit
            Log.d(TAG, "create action type " + ACTION_TYPE);
            task = taskViewModel.getTask(id).getValue();
            taskName.setText(task.getName());
            taskDescription.setText(task.getDescription());
            date = LocalDate.parse(task.getDate());
            taskDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            time = task.getTime();
            taskTime.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            checkChips();

            isRepeated = task.isRepeated();
            if (isRepeated) {
                taskRepeatSwitch.setChecked(true);
                period = task.getPeriod();
                day = task.getDay();
                taskPeriod.setText(PERIODS[period], false);
                if (period != 0) {
                    chipGroupDay.check(chipGroupDay.getChildAt(day - 1).getId());
                }else {
                    hideSelectDay(true);
                }
            }
            if (task.getReminderType() != 0) {
                reminderSwitch.setChecked(true);
                initialReminderType = task.getReminderType();
                reminder = task.getReminderType();
                taskReminder.setText(REMINDERS[reminder - 1], false);
            }
            initialReminderType = task.getReminderType();

            saveButton.setText(R.string.save);
        } else { // new
            task = new TaskEntity();
            if (TASK_TYPE == 1) {
                taskRepeatSwitch.setChecked(true);
            }
        }
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

        taskPeriod = findViewById(R.id.routine_repeat);
        taskReminder = findViewById(R.id.reminder);

        taskRepeatGroup = findViewById(R.id.repeat_group);
        selectDayGroup = findViewById(R.id.selectDayGroup);
        selectDayGroup.setReferencedIds(new int[]{R.id.subheader, R.id.routines_horizontal_scroll_view});
        reminderGroup = findViewById(R.id.reminder_group);

        taskRepeatSwitch = findViewById(R.id.repeated_switch);
        reminderSwitch = findViewById(R.id.reminder_switch);

        subheader = findViewById(R.id.subheader);

        chipGroupDay = findViewById(R.id.chipGroupNew);
        chipGroupDate = findViewById(R.id.chipGroup);
        chipToday = findViewById(R.id.chip_today);
        chipTomorrow = findViewById(R.id.chip_tomorrow);

        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
    }

    private void setListeners() {
        taskTime.setOnClickListener(v -> onInputTime());
        taskDate.setOnClickListener(v -> onInputDate());
        saveButton.setOnClickListener(v -> onFinishAction());
        cancelButton.setOnClickListener(v -> onBackPressed());

        setPeriodAdapter();
        setReminderAdapter();

        taskRepeatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskRepeatGroup.setVisibility(Group.VISIBLE);
                taskDate.setEnabled(false);
                chipToday.setChecked(false);
                chipTomorrow.setChecked(false);
                chipToday.setEnabled(false);
                chipTomorrow.setEnabled(false);
                isRepeated = true;
            } else {
                taskRepeatGroup.setVisibility(Group.GONE);
                taskDate.setEnabled(true);
                chipToday.setEnabled(true);
                chipTomorrow.setEnabled(true);
                isRepeated = false;
                checkChips();
            }
        });
        reminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reminderGroup.setVisibility(Group.VISIBLE);
                remindMe = true;
            } else {
                reminderGroup.setVisibility(Group.GONE);
                remindMe = false;
            }
        });
        chipToday.setOnClickListener(v -> {
            date = LocalDate.now();
            taskDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            taskDateLayout.setError(null);

        });
        chipTomorrow.setOnClickListener(v -> {
            date = LocalDate.now().plusDays(1);
            taskDate.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            taskDateLayout.setError(null);
        });
        taskPeriod.setOnItemClickListener((parent, view, position, id) -> {
            period = position;
            if (period == 0) {
                day = 0;
                hideSelectDay(true);
            } else {
                hideSelectDay(false);
            }
        });
        taskReminder.setOnItemClickListener((parent, view, position, id) -> {
            reminder = position + 1;
            Log.d(TAG, "setListeners: reminder " + reminder);
        });
        chipGroupDay.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                for (int i = 0; i < chipGroupDay.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroupDay.getChildAt(i);
                    if (chip.isChecked()) {
                        day = i + 1;
                        break;
                    }
                }
            } else {
                day = -1;
            }
            Log.d(TAG, "setListeners: day " + day);
        });
    }


    private void onFinishAction() {
        if (!validate()) return;
        Log.d(TAG, "addTask: validate passed");
        task.setName(taskName.getText().toString());
        task.setDescription(taskDescription.getText().toString());
        task.setDate(date != null ? date.toString() : task.getDate());
        task.setTime(time != null ? time : task.getTime());
        task.setCompleted(false);
        task.setRepeated(isRepeated);

        if (isRepeated) {
            task.setPeriod(period);
            task.setDay(day);
            task.setDate(LocalDate.now().toString());
        } else {
            task.setPeriod(0);
            task.setDay(0);
        }
        if (remindMe) {
            task.setReminderType(reminder);
            taskViewModel.setAlarm(task, getApplicationContext());
        } else if (initialReminderType != 0 && initialReminderType != reminder) {
            taskViewModel.deleteAlarm(task, getApplicationContext());
            task.setReminderType(0);
        } else {
            task.setReminderType(0);
        }

        if (ACTION_TYPE == 0) {
            taskViewModel.addTask(task);
        } else {
            taskViewModel.updateTask(task);
        }

        finish();
    }

    private boolean validate() {
        boolean isvalid = true;
        if (time == null) {
            taskTimeLayout.setError("Select a time");
            isvalid = false;
        }
        if (isRepeated) {
            if (day == -1 && period != 0) {
                Snackbar.make(saveButton, "Please select a day", Snackbar.LENGTH_SHORT).show();
                isvalid = false;
            }
            if (period == -1) {
                Snackbar.make(saveButton, "Please select a repeat period", Snackbar.LENGTH_SHORT).show();
                isvalid = false;
            }

        } else {
            if (date == null) {
                taskDateLayout.setError("Select a date");
                isvalid = false;
            }
        }
        if (remindMe && reminder == -1) {
            Snackbar.make(saveButton, "Please select a time for your reminder", Snackbar.LENGTH_SHORT).show();
            isvalid = false;
        }
        return isvalid;
    }

    private void setPeriodAdapter() {
        taskPeriod.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                PERIODS));
    }

    private void setReminderAdapter() {
        taskReminder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                REMINDERS));
    }

    private void hideSelectDay(boolean b) {
        selectDayGroup.setVisibility(b ? Group.GONE : Group.VISIBLE);
        selectDayGroup.requestLayout();
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
            checkChips();
        });
    }

    private void checkChips() {
        if (date != null && date.equals(LocalDate.now())) {
            chipToday.setChecked(true);
            chipTomorrow.setChecked(false);
        } else if (date != null && date.equals(LocalDate.now().plusDays(1))) {
            chipTomorrow.setChecked(true);
            chipToday.setChecked(false);
        } else {
            chipToday.setChecked(false);
            chipTomorrow.setChecked(false);
        }
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
}