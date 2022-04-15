package com.android.biglifts.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.ExerciseTemplateLinkModel;
import com.android.biglifts.models.ExerciseWorkoutLinkModel;
import com.android.biglifts.models.LogDatePojo;
import com.android.biglifts.models.LogEntryModel;
import com.android.biglifts.models.TemplateModel;
import com.android.biglifts.models.WorkoutModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
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

    public void insertExercises(ExerciseModel exerciseModel) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseDao().insertExercise(exerciseModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
                /*
                To show a toast message indicating whether the insertion was successful:
                .subscribe(() -> Toast.makeText(mContext, "Completed!", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show());
                 */
    }

    public void updateExercise(ExerciseModel exerciseModel) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseDao().updateExercise(exerciseModel))
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

    public LiveData<List<ExerciseModel>> getAllExercisesOrderedAlphabetically() {
        return mBigLiftsDatabase.getExerciseDao().getAllVisibleExercisesOrderedAlphabetically();
    }

    public LiveData<List<ExerciseModel>> getExercisesInTemplate(long templateID) {
        return mBigLiftsDatabase.getExerciseDao().getExercisesInTemplate(templateID);
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

    public void deleteExerciseWorkoutLinksByWorkoutID(long workoutID) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseWorkoutLinkDao().deleteExerciseWorkoutLinksByWorkoutID(workoutID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteExerciseWorkoutLinks(List<ExerciseWorkoutLinkModel> exerciseWorkoutLinkModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseWorkoutLinkDao().deleteExerciseWorkoutLinks(exerciseWorkoutLinkModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteExerciseWorkoutLinksByWorkoutIDAndExerciseID(long workoutID, int exerciseID) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseWorkoutLinkDao().deleteExerciseWorkoutLinksByWorkoutIDAndExerciseID(workoutID, exerciseID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    //endregion

    //region tblLogEntry Methods

    public void insertLogEntries(List<LogEntryModel> logEntryModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getLogEntryDao().insertLogEntries(logEntryModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<LogEntryModel>> getExerciseHistory(int exerciseID) {
        return mBigLiftsDatabase.getLogEntryDao().getExerciseLogHistory(exerciseID);
    }

    public LiveData<List<LogDatePojo>> getLogChartDataByExerciseID(int exerciseID) {
        return mBigLiftsDatabase.getLogEntryDao().getLogChartDataByExerciseID(exerciseID);
    }

    //endregion

    //region tblTemplate Methods

    public void insertTemplate(TemplateModel templateModel) {
        mBigLiftsDatabase.getTemplateDao().insertTemplate(templateModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        templateModel.setId(aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public LiveData<List<TemplateModel>> getAllTemplates() {
        return mBigLiftsDatabase.getTemplateDao().getAllTemplates();
    }

    public void updateTemplate(TemplateModel templateModel) {
        Completable.fromAction(() -> mBigLiftsDatabase.getTemplateDao().updateTemplate(templateModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteTemplate(TemplateModel templateModel) {
        Completable.fromAction(() -> mBigLiftsDatabase.getTemplateDao().deleteTemplate(templateModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    //endregion

    //region tblExerciseTemplateLink Methods

    public void insertExerciseTemplateLink(ExerciseTemplateLinkModel exerciseTemplateLinkModel) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseTemplateLinkDao().insertExerciseTemplateLink(exerciseTemplateLinkModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<ExerciseTemplateLinkModel>> getExerciseTemplateLinksByTemplateID(long templateID) {
        return mBigLiftsDatabase.getExerciseTemplateLinkDao().getExerciseTemplateLinkByTemplateID(templateID);
    }

    public void deleteExerciseTemplateLinksByTemplateIDAndExerciseID(long templateID, int exerciseID) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseTemplateLinkDao().deleteExerciseTemplateLinksByTemplateIDAndExerciseID(templateID, exerciseID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteExerciseTemplateLinks(List<ExerciseTemplateLinkModel> exerciseTemplateLinkModels) {
        Completable.fromAction(() -> mBigLiftsDatabase.getExerciseTemplateLinkDao().deleteExerciseTemplateLinks(exerciseTemplateLinkModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    //endregion

}
