package com.android.biglifts.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.android.biglifts.StartNewWorkoutActivity;
import com.android.biglifts.adapters.TemplateRecyclerAdapter;
import com.android.biglifts.persistence.BigLiftsRepository;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tblTemplate")
public class TemplateModel {

    @Ignore
    private static final String TAG = "TemplateModel";

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String templateName;

    @Ignore
    private ArrayList<ExerciseModel> exercisesInTemplateList = new ArrayList<>();

    @Ignore
    public TemplateModel() {
    }

    public TemplateModel(String templateName) {
        this.templateName = templateName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<ExerciseModel> getExercisesInTemplateList() {
        return exercisesInTemplateList;
    }

    public void setExercisesInTemplateList(ArrayList<ExerciseModel> exercisesInTemplateList) {
        this.exercisesInTemplateList = exercisesInTemplateList;
    }

    public void updateExercisesList(BigLiftsRepository bigLiftsRepository, StartNewWorkoutActivity activity, TemplateRecyclerAdapter adapter) {
        bigLiftsRepository.getExercisesInTemplate(id).observe(activity, new Observer<List<ExerciseModel>>() {
            @Override
            public void onChanged(List<ExerciseModel> exerciseModels) {
                if (exercisesInTemplateList.size() > 0) {
                    exercisesInTemplateList.clear();
                }
                if (exerciseModels != null) {
                    exercisesInTemplateList.addAll(exerciseModels);
                }
                adapter.notifyDataSetChanged();
            }
        });
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
                ", exercisesList=" + exercisesInTemplateList +
                '}';
    }
}
