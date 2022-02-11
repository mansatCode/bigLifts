package com.android.biglifts.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.biglifts.models.TemplateModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface TemplateDao {
    @Insert
    Single<Long> insertTemplate(TemplateModel templateModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTemplate(TemplateModel templateModel);

    @Delete
    void deleteTemplate(TemplateModel templateModel);

    @Query("SELECT * FROM tblTemplate")
    LiveData<List<TemplateModel>> getAllTemplates();

    @Query("SELECT * FROM tblTemplate WHERE id = :id")
    LiveData<TemplateModel> getTemplateById(int id);
}
