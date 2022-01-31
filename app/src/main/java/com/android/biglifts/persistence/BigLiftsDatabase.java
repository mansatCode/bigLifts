package com.android.biglifts.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.LogEntryModel;
import com.android.biglifts.utility.AppExecutors;
import com.android.biglifts.utility.ExercisesData;

@Database(entities = {ExerciseModel.class, LogEntryModel.class}, version = 1)
public abstract class BigLiftsDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "BigLifts_db";

    private static BigLiftsDatabase instance;

    static BigLiftsDatabase getInstance(final Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BigLiftsDatabase.class,
                    DATABASE_NAME
            ).addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    AppExecutors.getInstance().diskIO()
                            .execute(new Runnable() {
                                @Override
                                public void run() {
                                    getInstance(context).getExerciseDao()
                                            .prePopulateExerciseTable(ExercisesData
                                                    .populateExercisesData());
                                }
                            });
                }
            }).build();
        }
        return instance;
    }

    public abstract ExerciseDao getExerciseDao();
    public abstract LogEntryDao getLogEntryDao();
}
