package com.android.biglifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.biglifts.adapters.CurrentWorkoutRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.android.biglifts.persistence.ExerciseDao;
import com.android.biglifts.utility.VerticalSpacingItemDecorator;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CurrentWorkoutActivity extends AppCompatActivity implements
        CurrentWorkoutRecyclerAdapter.OnExerciseInWorkoutListener,
        View.OnClickListener {

    private static final String TAG = "CurrentWorkoutActivity";

    // UI Components
    private RecyclerView mRecyclerView;
    private ImageView mViewNotes, mRestTimer, mDiscardWorkout;
    private Button mAddExercise;

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
        retrieveAllExercises();
    }

    private void insertExercise() {
        List<ExerciseModel> exercises = new ArrayList<>();
        ExerciseModel e = new ExerciseModel("Test");
        exercises.add(e);
        mBigLiftsRepository.insertExerciseTask(exercises);
    }

    private void retrieveAllExercises(){
        mBigLiftsRepository.retrieveAllExercisesTask().observe(this, new Observer<List<ExerciseModel>>() {
            @Override
            public void onChanged(List<ExerciseModel> exerciseModels) {
                if (mExercisesList.size() > 0) {
                    mExercisesList.clear();
                }
                if (exerciseModels != null){
                    mExercisesList.addAll(exerciseModels);
                }
                mCurrentWorkoutRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initUI()
    {
        mRecyclerView = findViewById(R.id.activity_current_workout_rv);
        mViewNotes = findViewById(R.id.toolbar_current_workout_iv_viewNote);
        mRestTimer = findViewById(R.id.toolbar_current_workout_iv_timer);
        mDiscardWorkout = findViewById(R.id.toolbar_current_workout_iv_discardWorkout);
        mAddExercise = findViewById(R.id.activity_current_workout_btn_addExercise);
    }

    private void setListeners()
    {
        mViewNotes.setOnClickListener(this);
        mRestTimer.setOnClickListener(this);
        mDiscardWorkout.setOnClickListener(this);
        mAddExercise.setOnClickListener(this);
    }

    private void initRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        mCurrentWorkoutRecyclerAdapter = new CurrentWorkoutRecyclerAdapter(mExercisesList, this);
        mRecyclerView.setAdapter(mCurrentWorkoutRecyclerAdapter);
    }

    @Override
    public void onExerciseInWorkoutClick(int position) {
        ExerciseModel exerciseModel = mExercisesList.get(position);
        exerciseModel.setExpanded(!exerciseModel.isExpanded());
        mCurrentWorkoutRecyclerAdapter.notifyItemChanged(position);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.toolbar_current_workout_iv_viewNote:
                Toast.makeText(this, "Notes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_current_workout_iv_timer:
                Toast.makeText(this, "Rest timer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_current_workout_iv_discardWorkout:
                Toast.makeText(this, "Discard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_current_workout_btn_addExercise:
                insertExercise();
                Toast.makeText(this, "Exercise inserted", Toast.LENGTH_SHORT).show();
        }
    }
}