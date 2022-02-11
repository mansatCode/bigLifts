package com.android.biglifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddTemplateActivity extends AppCompatActivity implements View.OnClickListener {

    // Constants
    private static final String TAG = "AddTemplateActivity";

    // UI Components
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private EditText mTemplateNameEditText;
    private Button mAddExerciseButton;
    private FloatingActionButton mSaveTemplateFAB;

    // Variables
    private boolean mIsTemplateValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);

        initUI();
        setListeners();
        initRecyclerView();
        insertTemplate();
    }

    private void initUI() {
        mRecyclerView = findViewById(R.id.activity_add_template_rv);
        mSaveTemplateFAB = findViewById(R.id.activity_add_template_fab_saveTemplate);
        mTemplateNameEditText = findViewById(R.id.activity_add_template_et_templateName);
        mAddExerciseButton = findViewById(R.id.activity_add_template_btn_addExercise);

        mToolbar = findViewById(R.id.activity_add_template_tb);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setListeners() {
        mSaveTemplateFAB.setOnClickListener(this);
        mAddExerciseButton.setOnClickListener(this);
    }

    private void initRecyclerView() {

    }

    private void insertTemplate() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_add_template_fab_saveTemplate:
                Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_add_template_btn_addExercise:
                Toast.makeText(this, "addEx", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}