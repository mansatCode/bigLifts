package com.android.biglifts.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
}
