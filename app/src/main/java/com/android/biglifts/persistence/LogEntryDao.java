package com.android.biglifts.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.android.biglifts.models.LogDatePojo;
import com.android.biglifts.models.LogEntryModel;

import java.util.List;

@Dao
public interface LogEntryDao {
    @Insert
    void insertLogEntries(List<LogEntryModel> logEntryModels);

    @Update
    void updateLogEntries(List<LogEntryModel> logEntryModels);

    @Delete
    void deleteLogEntries(List<LogEntryModel> logEntryModels);

    @Query("SELECT * FROM tblLogEntry")
    LiveData<List<LogEntryModel>> getAllLogEntries();

    @Query("SELECT * FROM tblLogEntry WHERE id = :id")
    LiveData<LogEntryModel> getLogEntryById(int id);

    @Query("SELECT tblLogEntry.* FROM tblLogEntry, tblWorkout, tblExerciseWorkoutLink " +
            "WHERE tblLogEntry.exerciseWorkoutLinkID = tblExerciseWorkoutLink.id " +
            "AND tblExerciseWorkoutLink.exerciseID = :exerciseID " +
            "AND tblExerciseWorkoutLink.workoutID = tblWorkout.id " +
            "AND tblLogEntry.setDetails = 0 " +
            "AND tblWorkout.id = (" +
                "SELECT tblWorkout.id FROM tblWorkout, tblExerciseWorkoutLink " +
                "WHERE tblWorkout.id = tblExerciseWorkoutLink.workoutID " +
                "AND tblExerciseWorkoutLink.exerciseID = :exerciseID " +
                "AND tblWorkout.workoutDuration > 0 " +
                "ORDER BY tblWorkout.workoutDate DESC)")
    LiveData<List<LogEntryModel>> getExerciseLogHistory (int exerciseID);

    @Query("SELECT tblLogEntry.weight as weight, tblLogEntry.reps as reps, tblLogEntry.setDetails as setDetails, tblWorkout.workoutDate as logDate FROM tblLogEntry, tblWorkout, tblExerciseWorkoutLink WHERE " +
            ":exerciseID = tblExerciseWorkoutLink.exerciseID AND " +
            "tblExerciseWorkoutLink.workoutID = tblWorkout.id AND " +
            "tblExerciseWorkoutLink.id = tblLogEntry.exerciseWorkoutLinkID " +
            "ORDER BY tblWorkout.workoutDate ASC")
    LiveData<List<LogDatePojo>> getLogChartDataByExerciseID(int exerciseID);
}
