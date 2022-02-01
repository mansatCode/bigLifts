package com.android.biglifts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.adapters.CurrentWorkoutRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.LogEntryModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.android.biglifts.utility.VerticalSpacingItemDecorator;

import java.util.ArrayList;

public class CurrentWorkoutActivity extends AppCompatActivity implements
        CurrentWorkoutRecyclerAdapter.OnExerciseInWorkoutListener,
        View.OnClickListener {

    private static final String TAG = "CurrentWorkoutActivity";
    public static final String EXTRA_EXERCISE = "com.android.biglifts.EXTRA_EXERCISE";

    // UI Components
    private RecyclerView mParentRecyclerView;
    private ImageView mViewNotes, mRestTimer, mDiscardWorkout;
    private Button mAddExercise, mFinishWorkout;

    // Variables
    private ArrayList<ExerciseModel> mExercisesList = new ArrayList<>();
    private CurrentWorkoutRecyclerAdapter mCurrentWorkoutRecyclerAdapter;
    private BigLiftsRepository mBigLiftsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        initUI();
        mBigLiftsRepository = new BigLiftsRepository(this);
        setListeners();
        setSupportActionBar((Toolbar)findViewById(R.id.activity_current_workout_tb));
        initRecyclerView();
    }

    private void initUI()
    {
        mParentRecyclerView = findViewById(R.id.activity_current_workout_rv);
        mViewNotes = findViewById(R.id.toolbar_current_workout_iv_viewNote);
        mRestTimer = findViewById(R.id.toolbar_current_workout_iv_timer);
        mDiscardWorkout = findViewById(R.id.toolbar_current_workout_iv_discardWorkout);
        mAddExercise = findViewById(R.id.activity_current_workout_btn_addExercise);
        mFinishWorkout = findViewById(R.id.activity_current_workout_btn_finishWorkout);
    }

    private void setListeners()
    {
        mViewNotes.setOnClickListener(this);
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
        mCurrentWorkoutRecyclerAdapter = new CurrentWorkoutRecyclerAdapter(mExercisesList, this);
        mParentRecyclerView.setAdapter(mCurrentWorkoutRecyclerAdapter);
    }

    @Override
    public void onExerciseInWorkoutClick(int position, View view) {
        ExerciseModel exercise = mExercisesList.get(position);
        switch(view.getId()) {
            case R.id.row_current_workout_exercises_iv_tripleLines:
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
                exercise.setExpanded(!exercise.isExpanded());
                mCurrentWorkoutRecyclerAdapter.notifyItemChanged(position);
                break;
            case R.id.row_current_workout_exercises_iv_options:
                Toast.makeText(this, "Options", Toast.LENGTH_SHORT).show();
                break;
            case R.id.row_current_workout_exercises_btn_addSet:
                LogEntryModel log = new LogEntryModel();
                log.setExerciseID(exercise.getId());
                exercise.getLogEntriesList().add(log);
                mCurrentWorkoutRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Add set", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.activity_current_workout_btn_addExercise:
                Intent intent = new Intent(CurrentWorkoutActivity.this, SelectExerciseActivity.class);
                mSelectExerciseActivity.launch(intent);
                break;
            case R.id.toolbar_current_workout_iv_viewNote:
                Toast.makeText(this, "Notes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_current_workout_iv_timer:
                Toast.makeText(this, "Rest timer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_current_workout_iv_discardWorkout:
                Toast.makeText(this, "Discard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_current_workout_btn_finishWorkout:
                //TODO - save workout to database. LogEntryModels only for now.
                for (ExerciseModel e : mExercisesList) {
                    for (LogEntryModel l : e.getLogEntriesList()){
                        Log.d(TAG, l.toString());
                    }
                }


//                // Loop through, check for unchecked LogEntryModels
//                for (ExerciseModel exerciseModel : mExercisesList) {
//                    // If unchecked, display message saying "You have unfinished sets"
//                    if (containsUnchecked(exerciseModel.getLogEntriesList())) {
//                        showUnsavedLogsDialog();
//                        return;
//                    }
//                }
//
//                insertWorkout();
//
//                Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void insertWorkout() {
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

    private void showUnsavedLogsDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you finished?");
        builder.setMessage("All invalid/empty sets will be discarded. All sets with valid data will be automatically marked as complete.");

        builder.setPositiveButton("Finish workout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO - cleanup unchecked logs



                insertWorkout();
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
}
