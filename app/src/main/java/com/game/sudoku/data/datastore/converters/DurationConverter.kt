package com.game.sudoku.data.datastore.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Duration

class DurationConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDuration(value: Long): Duration {
        return Duration.ofSeconds(value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromDuration(duration: Duration): Long {
        return duration.seconds
    }
}
