package com.android.biglifts.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tblExercise")
public class ExerciseModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String exerciseName;

    @NonNull
    private String bodyPart;

    @NonNull
    private String category;

    private String exerciseNote;

    @NonNull
    @ColumnInfo(defaultValue = "1")
    private int isVisible;

    private String cableGrip;

    @Ignore
    private boolean isExpanded;

    @Ignore
    private ArrayList<LogEntryModel> logEntriesList;

    @Ignore
    private List<LogEntryModel> logEntriesHistoryList;

    public ExerciseModel(@NonNull String exerciseName, @NonNull String bodyPart, @NonNull String category, int isVisible, String exerciseNote, String cableGrip) {
        this.exerciseName = exerciseName;
        this.bodyPart = bodyPart;
        this.category = category;
        this.exerciseNote = exerciseNote;
        this.isVisible = isVisible;
        this.isExpanded = false;
        this.cableGrip = cableGrip;
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
        dest.writeString(exerciseNote);
        dest.writeInt(isVisible);
        dest.writeString(cableGrip);
    }

    public void cleanLogEntries (int removedSet) {
        for (LogEntryModel logEntryModel : logEntriesList) {
            if (logEntryModel.getSetNumber() > removedSet) {
                logEntryModel.decrementSetNumber();
            }
        }
    }

    public String getCableGrip() {
        return cableGrip;
    }

    public void setCableGrip(String cableGrip) {
        this.cableGrip = cableGrip;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public String getExerciseNote() {
        return exerciseNote;
    }

    public void setExerciseNote(String exerciseNote) {
        this.exerciseNote = exerciseNote;
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

    public List<LogEntryModel> getLogEntriesHistoryList() {
        return logEntriesHistoryList;
    }

    public void setLogEntriesHistoryList(List<LogEntryModel> logEntriesHistoryList) {
        this.logEntriesHistoryList = logEntriesHistoryList;
    }

    protected ExerciseModel(Parcel in) {
        id = in.readInt();
        exerciseName = in.readString();
        bodyPart = in.readString();
        category = in.readString();
        exerciseNote = in.readString();
        isVisible = in.readInt();
        cableGrip = in.readString();
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
                ", exerciseNote='" + exerciseNote + '\'' +
                ", isVisible=" + isVisible +
                ", cableGrip='" + cableGrip + '\'' +
                ", isExpanded=" + isExpanded +
                ", logEntriesList=" + logEntriesList +
                ", logEntriesHistoryList=" + logEntriesHistoryList +
                '}';
    }

    public static final int VISIBLE_FALSE = 0;
    public static final int VISIBLE_TRUE = 1;

    public static final String BODY_PART_CHEST = "Chest";
    public static final String BODY_PART_BACK = "Back";
    public static final String BODY_PART_ARMS = "Arms";
    public static final String BODY_PART_LEGS = "Legs";
    public static final String BODY_PART_SHOULDERS = "Shoulders";
    public static final String BODY_PART_CORE = "Core";
    public static final String[] EXERCISE_BODY_PARTS = {BODY_PART_CHEST, BODY_PART_BACK, BODY_PART_ARMS, BODY_PART_LEGS, BODY_PART_SHOULDERS, BODY_PART_CORE};

    public static final String CATEGORY_FREE_WEIGHT = "Free weight";
    public static final String CATEGORY_CABLE = "Cable";
    public static final String CATEGORY_MACHINE = "Machine";
    public static final String CATEGORY_BODY_WEIGHT = "Body weight";
    public static final String CATEGORY_ASSISTED_BODY_WEIGHT = "Assisted body weight";
    public static final String[] EXERCISE_CATEGORIES = {CATEGORY_FREE_WEIGHT, CATEGORY_CABLE, CATEGORY_MACHINE, CATEGORY_BODY_WEIGHT, CATEGORY_ASSISTED_BODY_WEIGHT};

    public static final String HANDLE_SOFT_STIRRUP = "Soft stirrup handle";
    public static final String HANDLE_TRICEP_ROPE = "Tricep rope";
    public static final String HANDLE_V_HANDLE = "V handle";
    public static final String HANDLE_STRAIGHT_BAR = "Straight bar";
    public static final String HANDLE_ANGLED_METAL_BAR = "Angled metal bar";
    public static final String HANDLE_LAT_BAR = "Lat bar";
    public static final String HANDLE_OTHER = "Other";
    public static final String[] EXERCISE_CABLE_ATTACHMENTS = {HANDLE_SOFT_STIRRUP, HANDLE_TRICEP_ROPE, HANDLE_V_HANDLE, HANDLE_STRAIGHT_BAR, HANDLE_ANGLED_METAL_BAR, HANDLE_LAT_BAR, HANDLE_OTHER};
}
