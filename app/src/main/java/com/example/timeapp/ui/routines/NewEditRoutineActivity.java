package com.example.timeapp.ui.routines;
/*
import static android.text.format.DateFormat.is24HourFormat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.timeapp.R;
import com.example.timeapp.db.RoutineEntity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class NewEditRoutineActivity extends AppCompatActivity {
    private static final String TAG = "NewEditRoutineActivity";
    private int day = -1, period = -1;
    private OffsetTime time = null;
    private Button finishButton;
    private String type;
    private TextView subheader;
    private TextInputEditText routineName, routineDescription, routineTime;
    private AutoCompleteTextView routineRepeat;
    private TextInputLayout routineTimeLayout;
    private RoutinesViewModel routineViewModel;
    private ChipGroup routineDayChips;
    private String[] PERIODS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_routine);

        type = getIntent().getStringExtra("type");
        int id = getIntent().getIntExtra("id", -1);

        PERIODS = getResources().getStringArray(R.array.periods);

        routineViewModel = new ViewModelProvider(this).get(RoutinesViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(type.equals("new") ? "New Routine" : "Edit Routine");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        if (type.equals("edit")) {
            routineViewModel.getRoutineById(id).observe(this, routine -> {
                routineName.setText(routine.getName());
                routineDescription.setText(routine.getDescription());
                routineTime.setText(routine.getTime());
                if (routine.getRepeatPeriod() == 0) {
                    hideSelectDay(true);
                } else {
                    routineDayChips.check(routine.getDay());
                }
                routineRepeat.setText(PERIODS[routine.getRepeatPeriod()]);
                setRepeatAdapter();

                period = routine.getRepeatPeriod();
                day = routine.getDay();
                finishButton.setText(R.string.routine_edit);

                Snackbar.make(routineRepeat, "Routine loaded" + routine.getRepeatPeriod(), Snackbar.LENGTH_SHORT).show();
            });
        }
        if (type.equals("new")) {
            finishButton.setText(R.string.routine_new);
        }
    }

    private void initViews() {
        finishButton = findViewById(R.id.finish_button);
        routineName = findViewById(R.id.routine_name);
        routineDescription = findViewById(R.id.routine_description);
        routineTime = findViewById(R.id.routine_time);
        subheader = findViewById(R.id.subheader);
        routineTimeLayout = findViewById(R.id.routine_time_layout);
        routineDayChips = findViewById(R.id.chipGroupNew);

        routineRepeat = findViewById(R.id.routine_repeat);
        setRepeatAdapter();

        routineRepeat.setOnItemClickListener((parent, view, position, id) -> {
            period = position;
            Log.d(TAG, "initViews: " + period);
            if (period == 0) {
                day = 0;
                hideSelectDay(true);
            } else {
                hideSelectDay(false);
            }
            routineRepeat.setError(null);
        });


        routineDayChips.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d(TAG, "checkedID: " + checkedId);
            if (checkedId != -1) {
                Chip chip = (Chip) routineDayChips.getChildAt(checkedId - 1);
//                Chip chip = (Chip) routineDayChips.findViewById(checkedId);
                Log.d(TAG, "chip text" + chip.getText().toString().toUpperCase());
                day = DayOfWeek.valueOf(chip.getText().toString().toUpperCase()).getValue();
            }
        });

        routineTime.setOnClickListener(v -> onInputTime());
        finishButton.setOnClickListener(v -> onFinishClick());
    }

    private void setRepeatAdapter() {
        routineRepeat.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                PERIODS));
    }

    private void onInputTime() {
        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTimeFormat(is24HourFormat(this) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H);
        MaterialTimePicker timePicker = builder.build();
        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");

        timePicker.addOnPositiveButtonClickListener(selection -> {
            this.time = OffsetTime.of(timePicker.getHour(), timePicker.getMinute(), 0, 0, OffsetTime.now().getOffset());
            routineTime.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            routineTimeLayout.setError(null);
        });
    }

    private void onFinishClick() {
        if (type.equals("new")) {
            newRoutine();
        } else {
            editRoutine();
        }
    }

    private boolean validate() {
        boolean valid = true;
        if (routineTime.getText().toString().isEmpty()) {
            routineTimeLayout.setError("Routine time cannot be empty");
            valid = false;
        } else {
            routineTimeLayout.setError(null);
        }
        if (period == -1) {
//            Snackbar.make(findViewById(R.id.finish_button), "Select the repeat period of your routine task", Snackbar.LENGTH_SHORT).show();
            routineRepeat.setError("Select the repeat period of your routine task");
            valid = false;
        }
        if ((int) routineDayChips.getCheckedChipId() == -1 && period != 0) {
            Snackbar.make(findViewById(R.id.finish_button), "Select the day of your routine task", Snackbar.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }

    private void newRoutine() {
        if (validate()) {
            routineViewModel.addRoutine(new RoutineEntity(routineName.getText().toString(),
                    routineDescription.getText().toString(),
                    routineTime.getText().toString(), period, day));
            Snackbar.make(findViewById(R.id.finish_button), "Routine task added", Snackbar.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void editRoutine() {
        if (validate()) {
            routineViewModel.updateRoutine(new RoutineEntity(routineName.getText().toString(),
                    routineDescription.getText().toString(),
                    routineTime.getText().toString(), period, day));
            Snackbar.make(findViewById(R.id.finish_button), "Routine task edited", Snackbar.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void hideSelectDay(boolean hide) {
        if (hide) {
            routineDayChips.setVisibility(ChipGroup.GONE);
            subheader.setVisibility(TextView.GONE);
        } else {
            routineDayChips.setVisibility(ChipGroup.VISIBLE);
            subheader.setVisibility(TextView.VISIBLE);
        }
    }
}
 */