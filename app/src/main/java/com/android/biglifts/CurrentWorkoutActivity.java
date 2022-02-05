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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.adapters.CurrentWorkoutRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.LogEntryModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.android.biglifts.utility.BottomSheetRestTimerDialog;
import com.android.biglifts.utility.VerticalSpacingItemDecorator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class CurrentWorkoutActivity extends AppCompatActivity implements
        CurrentWorkoutRecyclerAdapter.OnExerciseInWorkoutListener,
        View.OnClickListener,
        PopupMenu.OnMenuItemClickListener,
        BottomSheetRestTimerDialog.BottomSheetRestTimerListener {

    private static final String TAG = "CurrentWorkoutActivity";
    public static final String EXTRA_EXERCISE = "com.android.biglifts.EXTRA_EXERCISE";
    private static final String EXTRA_REST_TIME = "com.android.biglifts.EXTRA_REST_TIME";
    private static final String EXTRA_BOTTOMSHEET_REST_TIMER_TAG = "com.android.biglifts.EXTRA_BOTTOMSHEET_REST_TIMER_TAG";

    // UI Components
    private RecyclerView mParentRecyclerView;
    private ImageView mRestTimer, mDiscardWorkout;
    private Button mAddExercise, mFinishWorkout;
    private Chronometer mChronometer;
    private TextView mRestTimerDisplay;

    // Variables
    private ArrayList<ExerciseModel> mExercisesList = new ArrayList<>();
    private CurrentWorkoutRecyclerAdapter mCurrentWorkoutRecyclerAdapter;
    private BigLiftsRepository mBigLiftsRepository;
    private ExerciseModel mExerciseSelected;
    private long mRestTimeInMilliseconds = 0;
    private long mRestTimeInMillisecondsCopy;
    private CountDownTimer mCountDownTimer;
    private boolean mIsTimerRunning;

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
    }

    private void initUI()
    {
        mParentRecyclerView = findViewById(R.id.activity_current_workout_rv);
        mRestTimer = findViewById(R.id.toolbar_current_workout_iv_timer);
        mDiscardWorkout = findViewById(R.id.toolbar_current_workout_iv_discardWorkout);
        mAddExercise = findViewById(R.id.activity_current_workout_btn_addExercise);
        mFinishWorkout = findViewById(R.id.activity_current_workout_btn_finishWorkout);
        mChronometer = findViewById(R.id.toolbar_current_workout_chronometer);
        mRestTimerDisplay = findViewById(R.id.toolbar_current_workout_tv_restTimer);
    }

    private void startChronometer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    private void pauseChronometer() {
        mChronometer.stop();
    }

    private void setListeners()
    {
        mRestTimer.setOnClickListener(this);
        mDiscardWorkout.setOnClickListener(this);
        mAddExercise.setOnClickListener(this);
        mFinishWorkout.setOnClickListener(this);
    }

    private void initRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mParentRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mParentRecyclerView.addItemDecoration(itemDecorator);
        mCurrentWorkoutRecyclerAdapter = new CurrentWorkoutRecyclerAdapter(mExercisesList, this, CurrentWorkoutActivity.this);
        mParentRecyclerView.setAdapter(mCurrentWorkoutRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(CurrentWorkoutActivityItemTouchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(mParentRecyclerView);
    }

    @Override
    public void onExerciseInWorkoutClick(int position, View view) {
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
                log.setExerciseID(exercise.getId());
                exercise.getLogEntriesList().add(log);
                mCurrentWorkoutRecyclerAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void clearFocus() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop_up_menu_exercise_in_workout_itm_viewNotes:
                Toast.makeText(this, "View notes", Toast.LENGTH_SHORT).show();
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

    private void showOptionsPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setGravity(Gravity.END);
        popupMenu.inflate(R.menu.pop_up_menu_exercise_in_workout);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
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
        switch(v.getId()) {
            case R.id.activity_current_workout_btn_addExercise:
                Intent intent = new Intent(CurrentWorkoutActivity.this, SelectExerciseActivity.class);
                mSelectExerciseActivity.launch(intent);
                break;
            case R.id.toolbar_current_workout_iv_timer:
                BottomSheetRestTimerDialog restTimerBottomSheet = new BottomSheetRestTimerDialog();
                Bundle bundleRestTimer = new Bundle();
                bundleRestTimer.putLong(EXTRA_REST_TIME, mRestTimeInMillisecondsCopy);
                restTimerBottomSheet.setArguments(bundleRestTimer);
                restTimerBottomSheet.show(getSupportFragmentManager(), EXTRA_BOTTOMSHEET_REST_TIMER_TAG);
                break;
            case R.id.toolbar_current_workout_iv_discardWorkout:
                //TODO - Delete workout from database
                // -Finish activity
                Toast.makeText(this, "Discard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_current_workout_btn_finishWorkout:
                if (mExercisesList.isEmpty()) {
                    Snackbar sb_EmptyWorkoutError = Snackbar.make(v, "Add an exercise before proceeding.", Snackbar.LENGTH_LONG);
                    sb_EmptyWorkoutError.setBackgroundTint(ContextCompat.getColor(this, R.color.primaryDarkColor));
                    sb_EmptyWorkoutError.setTextColor(ContextCompat.getColor(this, R.color.white));
                    sb_EmptyWorkoutError.show();
                    return;
                }

                //TODO - save workout to database.
                for (ExerciseModel e : mExercisesList) {
                    for (LogEntryModel l : e.getLogEntriesList()){
                        Log.d(TAG, l.toString());
                    }
                }

                // Loop through, check for unchecked LogEntryModels
                for (ExerciseModel exerciseModel : mExercisesList) {
                    // If unchecked, display message saying "You have unfinished sets"
                    if (containsUnchecked(exerciseModel.getLogEntriesList())) {
                        showUnsavedLogsDialog();
                        return;
                    }
                }

                insertLogEntries();
                updateWorkout();
                Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show();
                endWorkout();
                break;
        }
    }

    @Override
    public void onButtonInBottomSheetRestTimerClicked(Boolean startTimer, long restTimeInMillis) {
        // Start rest timer
        if (startTimer) {
            if (mIsTimerRunning) {
                Toast.makeText(this, "Timer is already running!", Toast.LENGTH_SHORT).show();
                return;
            }
            mRestTimeInMilliseconds = restTimeInMillis;
            startRestTimer();
            return;
        }
        resetRestTimer();
    }

    private void startRestTimer() {
        mIsTimerRunning = true;
        mRestTimeInMillisecondsCopy = mRestTimeInMilliseconds;

        mCountDownTimer = new CountDownTimer(mRestTimeInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRestTimeInMilliseconds = millisUntilFinished;
                updateRestTimerText();
            }

            @Override
            public void onFinish() {
                mIsTimerRunning = false;
                //TODO - play sound to indicate rest timer is up.
            }
        }.start();
    }

    private void updateRestTimerText() {
        int minutes = (int) (mRestTimeInMilliseconds / 1000) / 60;
        int seconds = (int) (mRestTimeInMilliseconds / 1000) % 60;
        String timeRemainingFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mRestTimerDisplay.setText(timeRemainingFormatted);
    }

    private void resetRestTimer() {
        if (!mIsTimerRunning) {
            return;
        }

        mCountDownTimer.cancel();
        mIsTimerRunning = false;
        int minutes = (int) (mRestTimeInMillisecondsCopy / 1000) / 60;
        int seconds = (int) (mRestTimeInMillisecondsCopy / 1000) % 60;
        String timeRemainingFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mRestTimerDisplay.setText(timeRemainingFormatted);
    }

    private void updateWorkout() {
        pauseChronometer();
    }

    private void endWorkout() {
        // this.finish();
    }

    private void insertLogEntries() {
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
                                ExerciseModel exercise = intent.getParcelableExtra(EXTRA_EXERCISE);
                                exercise.setLogEntriesList(LogEntryModelList(exercise.getId()));
                                mExercisesList.add(exercise);
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
        log.setExerciseID(exerciseID);
        logEntryModelList.add(log);
        return logEntryModelList;
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
                endWorkout();
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
                Log.d(TAG, "onSelectedChanged: Change colour");
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
}
