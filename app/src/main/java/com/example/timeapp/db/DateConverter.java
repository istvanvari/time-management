package com.example.timeapp.db;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.time.LocalDate;

public class DateConverter {
    @TypeConverter
    public static LocalDate toLocalDate(String date) {
        if (date != null) {
            return LocalDate.parse(date);
        } else {
            return null;
        }
    }

    @NonNull
    @TypeConverter
    public static String fromLocalDate(@NonNull LocalDate date) {
        return date.toString();
    }
}

