package com.android.biglifts.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.biglifts.models.WorkoutModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface WorkoutDao {
    @Insert
    Single<Long> insertWorkout(WorkoutModel workoutModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWorkout(WorkoutModel workoutModel);

    @Delete
    void deleteWorkout(WorkoutModel workoutModel);

    @Query("SELECT * FROM tblWorkout")
    LiveData<List<WorkoutModel>> getAllWorkouts();

    @Query("SELECT * FROM tblWorkout WHERE id = :id")
    LiveData<WorkoutModel> getWorkoutById(int id);
}
