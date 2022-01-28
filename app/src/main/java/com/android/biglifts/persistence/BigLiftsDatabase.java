package com.android.biglifts.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.biglifts.models.ExerciseModel;

@Database(entities = {ExerciseModel.class}, version = 1)
public abstract class BigLiftsDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "BigLifts_db";

    private static BigLiftsDatabase instance;

    static BigLiftsDatabase getInstance(final Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BigLiftsDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract ExerciseDao getExerciseDao();
}
