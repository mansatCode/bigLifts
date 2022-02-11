package com.android.biglifts.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblExericseTemplateLink", foreignKeys = {@ForeignKey(entity = ExerciseModel.class,
        parentColumns = "id",
        childColumns = "exerciseID"),
        @ForeignKey(entity = TemplateModel.class,
                parentColumns = "id",
                childColumns = "templateID")
})
public class ExerciseTemplateLinkModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int exerciseID;

    @NonNull
    private int templateID;

    @Ignore
    public ExerciseTemplateLinkModel() { }

    public ExerciseTemplateLinkModel (int exerciseID, int templateID)
    {
        this.exerciseID = exerciseID;
        this.templateID = templateID;
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

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    @Override
    public String toString() {
        return "ExerciseTemplateLinkModel{" +
                "id=" + id +
                ", exerciseID=" + exerciseID +
                ", templateID=" + templateID +
                '}';
    }
}
