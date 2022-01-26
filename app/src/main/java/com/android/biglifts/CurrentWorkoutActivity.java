package com.android.biglifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.biglifts.adapters.CurrentWorkoutRecyclerAdapter;
import com.android.biglifts.models.LogEntry;
import com.android.biglifts.utility.VerticalSpacingItemDecorator;

import java.util.ArrayList;

public class CurrentWorkoutActivity extends AppCompatActivity implements CurrentWorkoutRecyclerAdapter.OnExerciseInWorkoutListener {

    private static final String TAG = "CurrentWorkoutActivity";

    // UI Components
    private RecyclerView mRecyclerView;

    // Variables
    private ArrayList<LogEntry> mLogEntries = new ArrayList<>();
    private CurrentWorkoutRecyclerAdapter mCurrentWorkoutRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        initUI();
        setSupportActionBar((Toolbar)findViewById(R.id.activity_current_workout_tb));
        initRecyclerView();
        insertDummyLogEntries();
    }

    private void initUI()
    {
        mRecyclerView = findViewById(R.id.activity_current_workout_rv);
    }

    private void initRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        mCurrentWorkoutRecyclerAdapter = new CurrentWorkoutRecyclerAdapter(mLogEntries, this);
        mRecyclerView.setAdapter(mCurrentWorkoutRecyclerAdapter);
    }

    private void insertDummyLogEntries()
    {
        for(int i = 0; i < 10; i++)
        {
            LogEntry logEntry = new LogEntry();
            logEntry.setExerciseName("Exercise #" + i);
            mLogEntries.add(logEntry);
        }
        mCurrentWorkoutRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onExerciseInWorkoutClick(int position) {
        LogEntry logEntry = mLogEntries.get(position);
        logEntry.setExpanded(!logEntry.isExpanded());
        mCurrentWorkoutRecyclerAdapter.notifyItemChanged(position);
    }
}