package com.android.biglifts.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblTemplate")
public class TemplateModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String templateName;

    @Ignore
    public TemplateModel() {
    }

    public TemplateModel(String templateName) {
        this.templateName = templateName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(@NonNull String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String toString() {
        return "TemplateModel{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                '}';
    }
}
