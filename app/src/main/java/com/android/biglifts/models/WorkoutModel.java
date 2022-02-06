package com.android.biglifts.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.android.biglifts.utility.DateConverter;

import java.util.Calendar;

@Entity(tableName = "tblWorkout")
public class WorkoutModel {

    @PrimaryKey (autoGenerate = true)
    private long id;

    private String workoutName;

    private long workoutDuration;

    @NonNull
    @TypeConverters({DateConverter.class})
    private Calendar workoutDate;

    @Ignore
    public WorkoutModel() {
    }

    public WorkoutModel(String workoutName, long workoutDuration, @NonNull Calendar workoutDate) {
        this.workoutName = workoutName;
        this.workoutDuration = workoutDuration;
        this.workoutDate = workoutDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(@NonNull String workoutName) {
        this.workoutName = workoutName;
    }

    @NonNull
    public Calendar getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(@NonNull Calendar workoutDate) {
        this.workoutDate = workoutDate;
    }

    public long getWorkoutDuration() {
        return workoutDuration;
    }

    public void setWorkoutDuration(long workoutDuration) {
        this.workoutDuration = workoutDuration;
    }

    @Override
    public String toString() {
        return "WorkoutModel{" +
                "id=" + id +
                ", workoutName='" + workoutName + '\'' +
                ", workoutDuration=" + workoutDuration +
                ", workoutDate=" + workoutDate +
                '}';
    }
}

