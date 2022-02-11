package com.android.biglifts.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.ExerciseWorkoutLinkModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


@Dao
public interface ExerciseWorkoutLinkDao {
    @Insert
    void insertExerciseWorkoutLink(ExerciseWorkoutLinkModel exerciseWorkoutLinkModel);

    @Update
    void updateExerciseWorkoutLinks(List<ExerciseWorkoutLinkModel> exerciseWorkoutLinkModels);

    @Delete
    void deleteExerciseWorkoutLinks(List<ExerciseWorkoutLinkModel> exerciseWorkoutLinkModel);

    @Query("SELECT * FROM tblExerciseWorkoutLink WHERE workoutID = :workoutID")
    LiveData<List<ExerciseWorkoutLinkModel>> getExerciseWorkoutLinkByWorkoutID(long workoutID);

    @Query("DELETE FROM tblExerciseWorkoutLink WHERE workoutID = :workoutID")
    void deleteExerciseWorkoutLinksByWorkoutID(long workoutID);

    @Query("DELETE FROM tblExerciseWorkoutLink WHERE workoutID = :workoutID AND exerciseID = :exerciseID")
    void deleteExerciseWorkoutLinksByWorkoutIDAndExerciseID(long workoutID, int exerciseID);
}
