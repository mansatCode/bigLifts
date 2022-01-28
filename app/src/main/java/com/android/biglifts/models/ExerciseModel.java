package com.android.biglifts.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblExercise")
public class ExerciseModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "exerciseName")
    private String exerciseName;

    @Ignore
    private boolean isExpanded;

    public ExerciseModel(@NonNull String exerciseName) {
        this.exerciseName = exerciseName;
        this.isExpanded = false;
    }

    @Ignore
    public ExerciseModel() {this.isExpanded = false;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(@NonNull String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
