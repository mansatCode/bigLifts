package com.android.biglifts.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblLogEntry", foreignKeys = {@ForeignKey(entity = ExerciseModel.class,
        parentColumns = "id",
        childColumns = "exerciseID")})
public class LogEntryModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int exerciseID;

    @NonNull
    private int setNumber;

    @NonNull
    private int weight;

    @NonNull
    private int reps;

    @Ignore
    public LogEntryModel() {
    }

    public LogEntryModel(int exerciseID, int setNumber, int weight, int reps) {
        this.exerciseID = exerciseID;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    @Override
    public String toString() {
        return "LogEntryModel{" +
                "id=" + id +
                ", exerciseID=" + exerciseID +
                ", setNumber=" + setNumber +
                ", weight=" + weight +
                ", reps=" + reps +
                '}';
    }
}
