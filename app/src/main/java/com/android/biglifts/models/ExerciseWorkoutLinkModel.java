package com.android.biglifts.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblExerciseWorkoutLink", foreignKeys = {@ForeignKey(entity = ExerciseModel.class,
        parentColumns = "id",
        childColumns = "exerciseID"),
        @ForeignKey(entity = WorkoutModel.class,
                parentColumns = "id",
                childColumns = "workoutID")
})
public class ExerciseWorkoutLinkModel {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private long workoutID;

    @NonNull
    private int exerciseID;

    @Ignore
    public ExerciseWorkoutLinkModel() {
    }

    public ExerciseWorkoutLinkModel(long workoutID, int exerciseID) {
        this.workoutID = workoutID;
        this.exerciseID = exerciseID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(long workoutID) {
        this.workoutID = workoutID;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    @Override
    public String toString() {
        return "ExerciseWorkoutLinkModel{" +
                "id=" + id +
                ", workoutID=" + workoutID +
                ", exerciseID=" + exerciseID +
                '}';
    }
}

