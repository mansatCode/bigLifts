package com.android.biglifts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.adapters.CurrentWorkoutRecyclerAdapter;
import com.android.biglifts.adapters.SetRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.ExerciseWorkoutLinkModel;
import com.android.biglifts.models.LogEntryModel;
import com.android.biglifts.models.WorkoutModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.android.biglifts.utility.BottomSheetExerciseNoteDialog;
import com.android.biglifts.utility.BottomSheetRestTimerDialog;
import com.android.biglifts.utility.VerticalSpacingItemDecorator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CurrentWorkoutActivity extends AppCompatActivity implements
        CurrentWorkoutRecyclerAdapter.OnExerciseInWorkoutListener,
        View.OnClickListener,
        PopupMenu.OnMenuItemClickListener,
        BottomSheetRestTimerDialog.BottomSheetRestTimerListener,
        BottomSheetExerciseNoteDialog.BottomSheetExerciseNoteListener,
        SetRecyclerAdapter.OnSetInWorkoutListener {

    // Constants
    private static final String TAG = "CurrentWorkoutActivity";
    public static final String EXTRA_EXERCISE = "com.android.biglifts.EXTRA_EXERCISE";
    private static final String EXTRA_CURRENT_REST_TIME = "com.android.biglifts.EXTRA_CURRENT_REST_TIME";
    private static final String EXTRA_DEFAULT_REST_TIME = "com.android.biglifts.EXTRA_DEFAULT_REST_TIME";
    private static final String EXTRA_IS_TIMER_RUNNING = "com.android.biglifts.EXTRA_IS_TIMER_RUNNING";
    private static final String EXTRA_BOTTOMSHEET_REST_TIMER_TAG = "com.android.biglifts.EXTRA_BOTTOMSHEET_REST_TIMER_TAG";
    private static final String EXTRA_BOTTOMSHEET_EXERCISE_NOTE_TAG = "com.android.biglifts.EXTRA_BOTTOMSHEET_EXERCISE_NOTE_TAG";
    private static final String EXTRA_EXERCISE_NOTE = "com.android.biglifts.EXTRA_EXERCISE_NOTE";

    private static final int START = 0;
    private static final int PAUSE = 1;
    private static final int RESET = 2;

    // UI Components
    private RecyclerView mParentRecyclerView;
    private ImageView mRestTimer, mDiscardWorkout;
    private Button mAddExerciseButton, mFinishWorkoutButton;
    private Chronometer mChronometer;
    private TextView mRestTimerDisplayTextView;
    private EditText mWorkoutTitleEditText;

    // Variables
    private ArrayList<ExerciseModel> mExercisesList = new ArrayList<>();
    private ArrayList<ExerciseWorkoutLinkModel> mExerciseWorkoutLinksList = new ArrayList<>();
    private CurrentWorkoutRecyclerAdapter mCurrentWorkoutRecyclerAdapter;
    private BigLiftsRepository mBigLiftsRepository;
    private ExerciseModel mExerciseSelected;
    private long mRestTimeInMilliseconds = 0;
    private long mRestTimeInMillisecondsDefault = 0;
    private CountDownTimer mCountDownTimer;
    private boolean mIsTimerRunning;
    private boolean mIsWorkoutValid = false;
    private WorkoutModel mCurrentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        initUI();
        mBigLiftsRepository = new BigLiftsRepository(this);
        mIsTimerRunning = false;
        setListeners();
        setSupportActionBar((Toolbar)findViewById(R.id.activity_current_workout_tb));
        initRecyclerView();
        startChronometer();
        insertWorkout();
    }

    private void initUI()
    {
        mParentRecyclerView = findViewById(R.id.activity_current_workout_rv);
        mRestTimer = findViewById(R.id.toolbar_current_workout_iv_timer);
        mDiscardWorkout = findViewById(R.id.toolbar_current_workout_iv_discardWorkout);
        mAddExerciseButton = findViewById(R.id.activity_current_workout_btn_addExercise);
        mFinishWorkoutButton = findViewById(R.id.activity_current_workout_btn_finishWorkout);
        mChronometer = findViewById(R.id.toolbar_current_workout_chronometer);
        mRestTimerDisplayTextView = findViewById(R.id.toolbar_current_workout_tv_restTimer);
        mWorkoutTitleEditText = findViewById(R.id.activity_current_workout_et_workoutTitle);
    }

    private void setListeners()
    {
        mRestTimer.setOnClickListener(this);
        mDiscardWorkout.setOnClickListener(this);
        mAddExerciseButton.setOnClickListener(this);
        mFinishWorkoutButton.setOnClickListener(this);
    }

    private void initRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mParentRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(15);
        mParentRecyclerView.addItemDecoration(itemDecorator);
        mCurrentWorkoutRecyclerAdapter = new CurrentWorkoutRecyclerAdapter(mExercisesList, this, CurrentWorkoutActivity.this);
        mParentRecyclerView.setAdapter(mCurrentWorkoutRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(CurrentWorkoutActivityItemTouchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(mParentRecyclerView);
    }

    private void startChronometer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    private void pauseChronometer() {
        mChronometer.stop();
    }

    private void insertWorkout() {
        mCurrentWorkout = new WorkoutModel();
        mCurrentWorkout.setWorkoutName(mWorkoutTitleEditText.getText().toString());
        mCurrentWorkout.setWorkoutDate(Calendar.getInstance());
        mBigLiftsRepository.insertWorkout(mCurrentWorkout);
    }

    private boolean validateWorkout() {
        if (mExercisesList.isEmpty()) {
            Snackbar sb_EmptyWorkoutError = Snackbar.make(mWorkoutTitleEditText, "Add an exercise before proceeding.", Snackbar.LENGTH_LONG);
            sb_EmptyWorkoutError.setBackgroundTint(ContextCompat.getColor(this, R.color.primaryLightColor));
            sb_EmptyWorkoutError.setTextColor(ContextCompat.getColor(this, R.color.white));
            sb_EmptyWorkoutError.show();
            return false;
        }
        else if (mWorkoutTitleEditText.getText().toString().trim().isEmpty()) {
            Snackbar sb_EmptyWorkoutTitleError = Snackbar.make(mWorkoutTitleEditText, "Workout needs a title.", Snackbar.LENGTH_LONG);
            sb_EmptyWorkoutTitleError.setBackgroundTint(ContextCompat.getColor(this, R.color.primaryLightColor));
            sb_EmptyWorkoutTitleError.setTextColor(ContextCompat.getColor(this, R.color.white));
            sb_EmptyWorkoutTitleError.show();
            return false;
        }
        return true;
    }

    private void updateWorkout() {
        pauseChronometer();
        mIsWorkoutValid = true;
        long workoutDurationMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        mCurrentWorkout.setWorkoutName(mWorkoutTitleEditText.getText().toString());
        mCurrentWorkout.setWorkoutDuration(workoutDurationMillis);
        mBigLiftsRepository.updateWorkout(mCurrentWorkout);
    }

    private void finishWorkout() {
        finish();
    }

    private void discardWorkout() {
        // TODO - Delete any database entries with this workoutID too.
        mBigLiftsRepository.deleteExerciseWorkoutLinks(mExerciseWorkoutLinksList);
        mBigLiftsRepository.deleleteWorkout(mCurrentWorkout);
    }

    private void clearFocus() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
    }

    private void clearWorkoutTitleFocus() {
        mWorkoutTitleEditText.clearFocus();
    }

    private void showOptionsPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setGravity(Gravity.END);
        popupMenu.inflate(R.menu.pop_up_menu_exercise_in_workout);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public void onExerciseInWorkoutClick(int position, View view) {
        clearWorkoutTitleFocus();
        ExerciseModel exercise = mExercisesList.get(position);
        mExerciseSelected = exercise;
        switch(view.getId()) {
            case R.id.row_current_workout_exercises_iv_tripleLines:
                clearFocus();
                exercise.setExpanded(!exercise.isExpanded());
                mCurrentWorkoutRecyclerAdapter.notifyItemChanged(position);
                break;
            case R.id.row_current_workout_exercises_iv_options:
                showOptionsPopupMenu(view);
                break;
            case R.id.row_current_workout_exercises_btn_addSet:
                LogEntryModel log = new LogEntryModel();
                exercise.getLogEntriesList().add(log);
                int index = mExercisesList.indexOf(exercise);
                clearFocus();
                mCurrentWorkoutRecyclerAdapter.notifyItemChanged(index);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop_up_menu_exercise_in_workout_itm_viewNotes:
                BottomSheetExerciseNoteDialog exerciseNoteBottomSheet = new BottomSheetExerciseNoteDialog();
                Bundle bundleData = new Bundle();
                bundleData.putString(EXTRA_EXERCISE_NOTE, mExerciseSelected.getExerciseNote());
                exerciseNoteBottomSheet.setArguments(bundleData);
                exerciseNoteBottomSheet.show(getSupportFragmentManager(), EXTRA_BOTTOMSHEET_EXERCISE_NOTE_TAG);
                return true;
            case R.id.pop_up_menu_exercise_in_workout_itm_remove:
                clearFocus();
                int index = mExercisesList.indexOf(mExerciseSelected);
                mExercisesList.remove(mExerciseSelected);
                mCurrentWorkoutRecyclerAdapter.notifyItemRemoved(index);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onButtonInBottomSheetExerciseNoteListenerClicked(String updatedNote) {
        mExerciseSelected.setExerciseNote(updatedNote);
        clearFocus();
        int index = mExercisesList.indexOf(mExerciseSelected);
        // TODO - update exercise
        mBigLiftsRepository.updateExercise(mExerciseSelected);
        mCurrentWorkoutRecyclerAdapter.notifyItemChanged(index);
    }

    @Override
    public void onButtonInBottomSheetRestTimerClicked(int timerState, long restTimeInMillis) {
        switch (timerState) {
            case START:
                mRestTimeInMilliseconds = restTimeInMillis;
                startRestTimer();
                break;
            case PAUSE:
                mRestTimeInMilliseconds = restTimeInMillis;
                pauseRestTimer();
                break;
            case RESET:
                mRestTimeInMilliseconds = restTimeInMillis;
                mRestTimeInMillisecondsDefault = restTimeInMillis;
                resetRestTimer();
                break;
        }
    }

    @Override
    public void deleteLogEntry(int logPosition, int exercisePosition) {
        ExerciseModel exerciseModel = mExercisesList.get(exercisePosition);
        exerciseModel.getLogEntriesList().remove(logPosition);

        if (exerciseModel.getLogEntriesList().isEmpty()) {
            mExercisesList.remove(exerciseModel);
            mCurrentWorkoutRecyclerAdapter.notifyItemRemoved(exercisePosition);
        }
        else {
            mCurrentWorkoutRecyclerAdapter.notifyItemChanged(logPosition);
            mParentRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        clearWorkoutTitleFocus();
        switch(v.getId()) {
            case R.id.activity_current_workout_btn_addExercise:
                Intent intent = new Intent(CurrentWorkoutActivity.this, SelectExerciseActivity.class);
                mSelectExerciseActivity.launch(intent);
                break;
            case R.id.toolbar_current_workout_iv_timer:
                BottomSheetRestTimerDialog restTimerBottomSheet = new BottomSheetRestTimerDialog();
                Bundle bundleRestTimer = new Bundle();
                bundleRestTimer.putLong(EXTRA_CURRENT_REST_TIME, mRestTimeInMilliseconds);
                bundleRestTimer.putLong(EXTRA_DEFAULT_REST_TIME, mRestTimeInMillisecondsDefault);
                bundleRestTimer.putBoolean(EXTRA_IS_TIMER_RUNNING, mIsTimerRunning);
                restTimerBottomSheet.setArguments(bundleRestTimer);
                restTimerBottomSheet.show(getSupportFragmentManager(), EXTRA_BOTTOMSHEET_REST_TIMER_TAG);
                break;
            case R.id.toolbar_current_workout_iv_discardWorkout:
                discardWorkout();
                finishWorkout();
                break;
            case R.id.activity_current_workout_btn_finishWorkout:
                if (!validateWorkout()) {
                    return;
                }

                updateWorkout();

                // Loop through, check for unchecked LogEntryModels
                for (ExerciseModel exerciseModel : mExercisesList) {
                    // If unchecked, display message saying "You have unfinished sets"
                    if (containsUnchecked(exerciseModel.getLogEntriesList())) {
                        showUnsavedLogsDialog();
                        return;
                    }
                }

                insertLogEntries();
                finishWorkout();
                break;
        }
    }

    public void startRestTimer() {
        mIsTimerRunning = true;

        mCountDownTimer = new CountDownTimer(mRestTimeInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRestTimeInMilliseconds = millisUntilFinished;
                updateRestTimerText();
            }

            @Override
            public void onFinish() {
                mIsTimerRunning = false;
                mRestTimeInMilliseconds = mRestTimeInMillisecondsDefault;
                //TODO - play sound to indicate rest timer is up.
            }
        }.start();
    }

    private void updateRestTimerText() {
        int minutes = (int) (mRestTimeInMilliseconds / 1000) / 60;
        int seconds = (int) (mRestTimeInMilliseconds / 1000) % 60;
        mRestTimerDisplayTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    public void resetRestTimer() {
        if (!mIsTimerRunning) {
            updateRestTimerText();
            return;
        }
        pauseRestTimer();
    }

    public void clearRestTimer() {
        if (mIsTimerRunning) {
            mCountDownTimer.cancel();
            mIsTimerRunning = false;
            mRestTimerDisplayTextView.setText("00:00");
            mRestTimeInMilliseconds = mRestTimeInMillisecondsDefault;
        }
    }

    private void pauseRestTimer() {
        mCountDownTimer.cancel();
        mIsTimerRunning = false;
        updateRestTimerText();
    }

    private void assignExerciseWorkoutLinkIDs() {
        // Assign each LogEntryModel its ExerciseWorkoutLinkID
        for (ExerciseModel exerciseModel : mExercisesList) {
            for (LogEntryModel logEntryModel : exerciseModel.getLogEntriesList()) {

                for (ExerciseWorkoutLinkModel exerciseWorkoutLinkModel : mExerciseWorkoutLinksList) {
                    if (exerciseWorkoutLinkModel.getExerciseID() == exerciseModel.getId()) {
                        logEntryModel.setExerciseWorkoutLinkID(exerciseWorkoutLinkModel.getId());
                        break;
                    }
                }

            }
        }
    }

    private void insertLogEntries() {
        assignExerciseWorkoutLinkIDs();

        for (ExerciseModel exerciseModel : mExercisesList) {
            mBigLiftsRepository.insertLogEntries(exerciseModel.getLogEntriesList());
        }
    }

    private boolean containsUnchecked(ArrayList<LogEntryModel> logEntriesList) {
        for (LogEntryModel log : logEntriesList) {
            if (!log.isChecked()) {
                return true;
            }
        }
        return false;
    }

    private final ActivityResultLauncher<Intent> mSelectExerciseActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            if (intent.hasExtra(EXTRA_EXERCISE)) {
                                ExerciseModel newExercise = intent.getParcelableExtra(EXTRA_EXERCISE);
                                int newExerciseID = newExercise.getId();
                                for (ExerciseModel exerciseModel : mExercisesList) {
                                    if (newExerciseID == exerciseModel.getId()) {
                                        Snackbar sb_DuplicateExerciseError = Snackbar.make(mWorkoutTitleEditText, "Exercise already present in workout.", Snackbar.LENGTH_LONG);
                                        sb_DuplicateExerciseError.setBackgroundTint(ContextCompat.getColor(CurrentWorkoutActivity.this, R.color.primaryLightColor));
                                        sb_DuplicateExerciseError.setTextColor(ContextCompat.getColor(CurrentWorkoutActivity.this, R.color.white));
                                        sb_DuplicateExerciseError.show();
                                        return;
                                    }
                                }
                                newExercise.setLogEntriesList(LogEntryModelList(newExercise.getId()));
                                retrieveExerciseHistory(newExercise);
                                retrieveExerciseWorkoutLinks();
                                mExercisesList.add(newExercise);
                                mCurrentWorkoutRecyclerAdapter.notifyItemInserted(mExercisesList.size());
                            } else {
                                Log.e(TAG, "onActivityResult: ERROR HAS OCCURRED");
                            }
                        }
                    }
                }
            });

    // Method to pass the arguments for the elements of child RecyclerView
    private ArrayList<LogEntryModel> LogEntryModelList(int exerciseID) {
        ArrayList<LogEntryModel> logEntryModelList = new ArrayList<>();
        LogEntryModel log = new LogEntryModel();
        logEntryModelList.add(log);

        // Insert the ExerciseWorkoutLink
        ExerciseWorkoutLinkModel exerciseWorkoutLink = new ExerciseWorkoutLinkModel();
        exerciseWorkoutLink.setExerciseID(exerciseID);
        exerciseWorkoutLink.setWorkoutID(mCurrentWorkout.getId());
        mBigLiftsRepository.insertExerciseWorkoutLink(exerciseWorkoutLink);
        return logEntryModelList;
    }

    private void retrieveExerciseWorkoutLinks() {

        mBigLiftsRepository.getExerciseWorkoutLinksByWorkoutID(mCurrentWorkout.getId()).observe(this, new Observer<List<ExerciseWorkoutLinkModel>>() {
            @Override
            public void onChanged(List<ExerciseWorkoutLinkModel> exerciseWorkoutLinkModels) {
                if (mExerciseWorkoutLinksList.size() > 0) {
                    mExerciseWorkoutLinksList.clear();
                }
                if (exerciseWorkoutLinkModels != null) {
                    mExerciseWorkoutLinksList.addAll(exerciseWorkoutLinkModels);
                }
            }
        });
    }

    private void retrieveExerciseHistory(ExerciseModel exerciseModel) {
        mBigLiftsRepository.getExerciseHistory(exerciseModel.getId()).observe(this, new Observer<List<LogEntryModel>>() {
            @Override
            public void onChanged(List<LogEntryModel> logEntryModels) {
                Log.d(TAG, "Retrieving " + exerciseModel.getExerciseName() + " history...");
                Log.d(TAG, logEntryModels.toString());
                if (logEntryModels != null) {
                    exerciseModel.setLogEntriesHistoryList(logEntryModels);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showUnsavedWorkoutDialog();
    }

    private void cleanLogEntriesList() {
        for (ExerciseModel exerciseModel : mExercisesList) {
            ArrayList<LogEntryModel> logsToRemove = new ArrayList<>();
            for (LogEntryModel logEntryModel : exerciseModel.getLogEntriesList()) {
                if (!logEntryModel.isChecked()) {
                    if (logEntryModel.getReps() == 0) {
                        // Logs that are unchecked and have 0 reps can be removed.
                        logsToRemove.add(logEntryModel);
                    }
                    logEntryModel.setChecked(true); // Unnecessary this isChecked is not saved to the database
                }
            }
            for (LogEntryModel logEntryModel : logsToRemove) {
                exerciseModel.getLogEntriesList().remove(logEntryModel);
                int removedSet = logEntryModel.getSetNumber();
                exerciseModel.cleanLogEntries(removedSet);
            }
        }
    }

    private void showUnsavedLogsDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you finished?");
        builder.setMessage("All invalid/empty sets will be discarded. All sets with valid data will be automatically marked as complete.");

        builder.setPositiveButton("Finish workout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cleanLogEntriesList();
                mCurrentWorkoutRecyclerAdapter.notifyDataSetChanged();
                mParentRecyclerView.getAdapter().notifyDataSetChanged();
                insertLogEntries();
                finishWorkout();
            }
        });
        builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUnsavedWorkoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you finished?");
        builder.setMessage("Current workout will be discarded!");

        builder.setPositiveButton("Cancel workout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CurrentWorkoutActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Resume", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Move exercise in workout around
    ItemTouchHelper.SimpleCallback CurrentWorkoutActivityItemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getBindingAdapterPosition();
            int toPosition = target.getBindingAdapterPosition();

            Collections.swap(mExercisesList, fromPosition, toPosition);
            mCurrentWorkoutRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.findViewById(R.id.row_current_workout_exercises_cl_container).setBackground(ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_highlighted_exercise_in_workout));
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.findViewById(R.id.row_current_workout_exercises_cl_container).setBackground(ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_exercise_in_workout));
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };

    @Override
    public void onSetInWorkoutClick() {
        // TODO - add sound.
        Log.d(TAG, "TODO - play completed set sound.");
    }

    @Override
    protected void onDestroy() {
        if (!mIsWorkoutValid) {
            discardWorkout();
        }
        super.onDestroy();
    }
}
