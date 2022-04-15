package com.android.biglifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.biglifts.adapters.ExerciseRecyclerAdapter;
import com.android.biglifts.adapters.TemplateRecyclerAdapter;
import com.android.biglifts.models.TemplateModel;
import com.android.biglifts.persistence.BigLiftsRepository;

import java.util.ArrayList;
import java.util.List;

public class StartNewWorkoutActivity extends AppCompatActivity implements
        View.OnClickListener,
        TemplateRecyclerAdapter.OnTemplateListener {

    // Constants
    private static final String TAG = "StartNewWorkoutActivity";

    // UI Components
    private RecyclerView mRecyclerView;
    private ImageButton mAddTemplate;
    private Button mStartEmptyWorkoutButton;
    private Toolbar mToolbar;

    // Variables
    private ArrayList<TemplateModel> mTemplateList = new ArrayList<>();
    private TemplateRecyclerAdapter mTemplateRecyclerAdapter;
    private BigLiftsRepository mBigLiftsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_workout);

        initUI();
        mBigLiftsRepository = new BigLiftsRepository(this);
        initRecyclerView();
        setListeners();
        retrieveTemplates();
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

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTemplateRecyclerAdapter = new TemplateRecyclerAdapter(this, mTemplateList, this);
        mRecyclerView.setAdapter(mTemplateRecyclerAdapter);
    }

    private void setListeners() {
        mAddTemplate.setOnClickListener(this);
        mStartEmptyWorkoutButton.setOnClickListener(this);
    }

    private void retrieveTemplates() {
        mBigLiftsRepository.getAllTemplates().observe(this, new Observer<List<TemplateModel>>() {
            @Override
            public void onChanged(List<TemplateModel> templateModels) {
                if (mTemplateList.size() > 0) {
                    mTemplateList.clear();
                }
                if (templateModels != null) {
                    for (TemplateModel templateModel : templateModels) {
                        templateModel.updateExercisesList(mBigLiftsRepository, StartNewWorkoutActivity.this, mTemplateRecyclerAdapter);
                    }
                    mTemplateList.addAll(templateModels);
                }
                mTemplateRecyclerAdapter.notifyDataSetChanged();
            }
        });
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

    @Override
    public void onTemplateClick(int position, View view) {
        switch (view.getId()) {
            case R.id.row_template_iv_options:
                Toast.makeText(this, "Options", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}