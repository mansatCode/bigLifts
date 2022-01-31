package com.android.biglifts.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.LogEntryModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class BigLiftsRepository {

    private static final String TAG = "BigLiftsRepository";

    private BigLiftsDatabase mBigLiftsDatabase;
    private Context mContext;

    public BigLiftsRepository(Context context) {
        mBigLiftsDatabase = BigLiftsDatabase.getInstance(context);
        mContext = context;
    }

    //region tblExercise Methods

    public void insertExerciseTask(List<ExerciseModel> exerciseModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseDao().insertExercises(exerciseModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
                /*
                To show a toast message indicating whether the insertion was successful:
                .subscribe(() -> Toast.makeText(mContext, "Completed!", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show());
                 */
    }

    public void updateExercise(List<ExerciseModel> exerciseModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseDao().updateExercises(exerciseModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteExercise(List<ExerciseModel> exerciseModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseDao().deleteExercises(exerciseModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<ExerciseModel>> retrieveAllExercisesTask() {
        return mBigLiftsDatabase.getExerciseDao().getAllExercises();
    }

    //endregion

    //region tblLogEntry Methods

    public void insertLogEntries(List<LogEntryModel> logEntryModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getLogEntryDao().insertLogEntries(logEntryModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    //endregion

}
