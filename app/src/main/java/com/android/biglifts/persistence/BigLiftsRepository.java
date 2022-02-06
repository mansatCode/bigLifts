package com.android.biglifts.persistence;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.ExerciseWorkoutLinkModel;
import com.android.biglifts.models.LogEntryModel;
import com.android.biglifts.models.WorkoutModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
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

    public void insertExercises(List<ExerciseModel> exerciseModels) {
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

    public void updateExercises(List<ExerciseModel> exerciseModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseDao().updateExercises(exerciseModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteExercises(List<ExerciseModel> exerciseModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseDao().deleteExercises(exerciseModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<ExerciseModel>> getAllExercisesTask() {
        return mBigLiftsDatabase.getExerciseDao().getAllExercises();
    }

    //endregion

    //region tblWorkout Methods

    public void insertWorkout(WorkoutModel workoutModel) {
        mBigLiftsDatabase.getWorkoutDao().insertWorkout(workoutModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        workoutModel.setId(aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public void updateWorkout(WorkoutModel workoutModel) {
        Completable.fromAction(() -> mBigLiftsDatabase.getWorkoutDao().updateWorkout(workoutModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleleteWorkout(WorkoutModel workoutModel) {
        Completable.fromAction(() -> mBigLiftsDatabase.getWorkoutDao().deleteWorkout(workoutModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    //endregion

    //region tblExerciseWorkoutLink Methods

    public void insertExerciseWorkoutLink(ExerciseWorkoutLinkModel exerciseWorkoutLinkModel) {
            Completable.fromAction(() -> mBigLiftsDatabase.getExerciseWorkoutLinkDao().insertExerciseWorkoutLink(exerciseWorkoutLinkModel))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
    }

    public LiveData<List<ExerciseWorkoutLinkModel>> getExerciseWorkoutLinksByWorkoutID(long workoutID) {
        return mBigLiftsDatabase.getExerciseWorkoutLinkDao().getExerciseWorkoutLinkByWorkoutID(workoutID);
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
