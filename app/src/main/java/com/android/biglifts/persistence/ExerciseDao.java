package com.android.biglifts.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.biglifts.models.ExerciseModel;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    void insertExercises(List<ExerciseModel> exerciseModels);

    @Update
    void updateExercises(List<ExerciseModel> exerciseModels);

    @Delete
    void deleteExercises(List<ExerciseModel> exerciseModels);

    @Query("SELECT * FROM tblExercise")
    LiveData<List<ExerciseModel>> getAllExercises();

    @Query("SELECT * FROM tblExercise WHERE id = :id")
    LiveData<ExerciseModel> getExerciseById(int id);
    // Flowable<ExerciseModel> loadExerciseById(int id);

    @Query("DELETE FROM tblExercise")
    void deleteAllExercises();
}
