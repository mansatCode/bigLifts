package com.android.biglifts.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "tblExercise")
public class ExerciseModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "exerciseName")
    private String exerciseName;

    @NonNull
    @ColumnInfo(name = "bodyPart")
    private String bodyPart;

    @NonNull
    @ColumnInfo(name = "category")
    private String category;

    @Ignore
    private boolean isExpanded;

    @Ignore
    private ArrayList<LogEntryModel> logEntriesList;

    public ExerciseModel(@NonNull String exerciseName, @NonNull String bodyPart, @NonNull String category) {
        this.exerciseName = exerciseName;
        this.bodyPart = bodyPart;
        this.category = category;
        this.isExpanded = false;
    }

    @Ignore
    public ExerciseModel() {
        this.isExpanded = false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(exerciseName);
        dest.writeString(bodyPart);
        dest.writeString(category);
    }

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

    @NonNull
    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(@NonNull String bodyPart) {
        this.bodyPart = bodyPart;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public ArrayList<LogEntryModel> getLogEntriesList() {
        return logEntriesList;
    }

    public void setLogEntriesList(ArrayList<LogEntryModel> logEntriesList) {
        this.logEntriesList = logEntriesList;
    }

    protected ExerciseModel(Parcel in) {
        id = in.readInt();
        exerciseName = in.readString();
        bodyPart = in.readString();
        category = in.readString();
        isExpanded = in.readByte() != 0;
    }

    public static final Creator<ExerciseModel> CREATOR = new Creator<ExerciseModel>() {
        @Override
        public ExerciseModel createFromParcel(Parcel in) {
            return new ExerciseModel(in);
        }

        @Override
        public ExerciseModel[] newArray(int size) {
            return new ExerciseModel[size];
        }
    };

    @Override
    public String toString() {
        return "ExerciseModel{" +
                "id=" + id +
                ", exerciseName='" + exerciseName + '\'' +
                ", bodyPart='" + bodyPart + '\'' +
                ", category='" + category + '\'' +
                ", isExpanded=" + isExpanded +
                ", logEntriesList=" + logEntriesList +
                '}';
    }
}
