package com.example.timeapp.db.Converters;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.time.OffsetTime;

public class TimeConverter {
    @NonNull
    @TypeConverter
    public static String toString(@NonNull OffsetTime offsetTime) {
        if (offsetTime != null) {
            return offsetTime.toString();
        } else {
            return null;
        }
    }

    @TypeConverter
    public static OffsetTime toOffsetTime(String offset) {
        return OffsetTime.parse(offset);
    }
}
