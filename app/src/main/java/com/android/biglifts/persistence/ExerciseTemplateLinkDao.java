package com.android.biglifts.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.biglifts.models.ExerciseTemplateLinkModel;

import java.util.List;

@Dao
public interface ExerciseTemplateLinkDao {
    @Insert
    void insertExerciseTemplateLink(ExerciseTemplateLinkModel exerciseTemplateLinkModel);

    @Update
    void updateExerciseTemplateLinks(List<ExerciseTemplateLinkModel> exerciseTemplateLinkModel);

    @Delete
    void deleteExerciseTemplateLinks(List<ExerciseTemplateLinkModel> exerciseTemplateLinkModel);

    @Query("SELECT * FROM tblExericseTemplateLink WHERE templateID = :templateID")
    LiveData<List<ExerciseTemplateLinkModel>> getExerciseTemplateLinkByTemplateID(long templateID);

    @Query("DELETE FROM tblExericseTemplateLink WHERE templateID = :templateID")
    void deleteExerciseTemplateLinksByTemplateID(long templateID);

    @Query("DELETE FROM tblExericseTemplateLink WHERE templateID = :templateID AND exerciseID = :exerciseID")
    void deleteExerciseTemplateLinksByTemplateIDAndExerciseID(long templateID, int exerciseID);
}
