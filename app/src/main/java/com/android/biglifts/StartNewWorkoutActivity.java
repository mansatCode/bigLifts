package com.android.biglifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StartNewWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    // Constants
    private static final String TAG = "StartNewWorkoutActivity";

    // UI Components
    private RecyclerView mRecyclerView;
    private ImageButton mAddTemplate;
    private Button mStartEmptyWorkoutButton;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_workout);

        initUI();
        setListeners();
    }

    private void initUI() {
        mRecyclerView = findViewById(R.id.activity_start_new_workout_rv);
        mAddTemplate = findViewById(R.id.activity_start_new_workout_imgbtn_addTemplate);
        mStartEmptyWorkoutButton = findViewById(R.id.activity_start_new_workout_btn_startEmptyWorkout);
        mToolbar = findViewById(R.id.activity_start_new_workout_tb);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setListeners() {
        mAddTemplate.setOnClickListener(this);
        mStartEmptyWorkoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_start_new_workout_imgbtn_addTemplate:
                Intent addTemplateActivity = new Intent(this, AddTemplateActivity.class);
                startActivity(addTemplateActivity);
                break;
            case R.id.activity_start_new_workout_btn_startEmptyWorkout:
                Intent currentWorkoutActivity = new Intent(this, CurrentWorkoutActivity.class);
                startActivity(currentWorkoutActivity);
                break;
        }
    }
}