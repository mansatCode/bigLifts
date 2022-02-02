package com.android.biglifts.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
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
    @ColumnInfo(defaultValue = "0")
    private int setDetails;

    @NonNull
    private int weight;

    @NonNull
    private int reps;

    @Ignore
    private boolean checked;

    @Ignore
    private int R_setDetail_ID = -1;

    @Ignore
    public LogEntryModel() {
        checked = false;
    }

    public LogEntryModel(int exerciseID, int setNumber, int weight, int reps, int setDetails) {
        this.exerciseID = exerciseID;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
        this.setDetails = setDetails;
        checked = false;
    }

    public int getR_setDetail_ID() {
        return R_setDetail_ID;
    }

    public void setR_setDetail_ID(int r_setDetail_ID) {
        R_setDetail_ID = r_setDetail_ID;
    }

    public int getSetDetails() {
        return setDetails;
    }

    public void setSetDetails(int setDetails) {
        this.setDetails = setDetails;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public void decrementSetNumber () {
        setNumber--;
    }

    public static final int NORMAL_SET = 0;
    public static final int WARM_UP_SET = 1;
    public static final int DROP_SET = 2;
    public static final int FAILURE_SET = 3;
    public static final int BACK_OFF_SET = 4;

    @Override
    public String toString() {
        return "LogEntryModel{" +
                "id=" + id +
                ", exerciseID=" + exerciseID +
                ", setNumber=" + setNumber +
                ", setDetails=" + setDetails +
                ", weight=" + weight +
                ", reps=" + reps +
                ", checked=" + checked +
                '}';
    }
}
