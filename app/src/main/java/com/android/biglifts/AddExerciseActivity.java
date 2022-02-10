package com.android.biglifts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    // Constants
    private static final String TAG = "AddExerciseActivity";

    // UI Components
    private FloatingActionButton mFloatingActionButton;
    private LinearLayout mBodyPartContainer, mCategoryContainer, mGripContainer;
    private TextView mBodyPartTextView, mCategoryTextView, mGripTextView;
    private EditText mExerciseNameEditText;
    private Toolbar mToolbar;

    private String mSelectedBodyPart, mSelectedCategory, mSelectedGrip;

    private BigLiftsRepository mBigLiftsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        initUI();
        setListeners();
        mBigLiftsRepository = new BigLiftsRepository(AddExerciseActivity.this);
    }

    private void initUI()
    {
        mBodyPartContainer = (LinearLayout) findViewById(R.id.activity_add_exercise_ll_bodyPartContainer);
        mCategoryContainer = (LinearLayout) findViewById(R.id.activity_add_exercise_ll_categoryContainer);
        mGripContainer = (LinearLayout) findViewById(R.id.activity_add_exercise_ll_gripContainer);
        mBodyPartTextView =  findViewById(R.id.activity_add_exercise_tv_bodyPart);
        mCategoryTextView = findViewById(R.id.activity_add_exercise_tv_category);
        mGripTextView = findViewById(R.id.activity_add_exercise_tv_grip);
        mExerciseNameEditText = (EditText) findViewById(R.id.activity_add_exercise_et_exerciseName);
        mFloatingActionButton = findViewById(R.id.activity_add_exercise_fab_addExercise);

        mToolbar = (Toolbar) findViewById(R.id.activity_add_exercise_tb);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListeners() {
        mBodyPartContainer.setOnClickListener(this);
        mCategoryContainer.setOnClickListener(this);
        mGripContainer.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_add_exercise_ll_bodyPartContainer:
                showOptionsDialog(ExerciseModel.EXERCISE_BODY_PARTS, "Select body part", 0);
                break;
            case R.id.activity_add_exercise_ll_categoryContainer:
                showOptionsDialog(ExerciseModel.EXERCISE_CATEGORIES, "Select category", 1);
                break;
            case R.id.activity_add_exercise_ll_gripContainer:
                showOptionsDialog(ExerciseModel.EXERCISE_CABLE_ATTACHMENTS, "Cable attachment", 2);
                break;
            case R.id.activity_add_exercise_fab_addExercise:
                if (validateData()) {
                    insertExercise();
                    finish();
                }
                else {
                    showInvalidDataDialog();
                }
                break;
        }
    }

    private boolean validateData() {
        boolean isValid = true;
        if(mExerciseNameEditText.getText().toString().trim().isEmpty())
        {
            isValid = false;
        }
        if(isNull(mSelectedBodyPart))
        {
            isValid = false;
        }
        if(isNull(mSelectedCategory))
        {
            isValid = false;
        }
        if(mSelectedCategory == ExerciseModel.CATEGORY_CABLE && isNull(mSelectedGrip))
        {
            isValid = false;
        }
        return isValid;
    }

    private void insertExercise() {
        ExerciseModel exercise = new ExerciseModel(mExerciseNameEditText.getText().toString(), mSelectedBodyPart, mSelectedCategory, ExerciseModel.VISIBLE_TRUE, null, mSelectedGrip);
        mBigLiftsRepository.insertExercises(exercise);
    }

    private Boolean isNull(String s)
    {
        return s == null;
    }

    private void showOptionsDialog(String[] array, String title, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddExerciseActivity.this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id == 0)
                {
                    mSelectedBodyPart = array[which];
                    mBodyPartTextView.setText(array[which]);
                }
                else if (id == 1)
                {
                    mSelectedCategory = array[which];
                    mCategoryTextView.setText(mSelectedCategory);

                    if (mSelectedCategory == ExerciseModel.CATEGORY_CABLE) {
                        mGripContainer.setVisibility(View.VISIBLE);
                    } else {
                        mGripContainer.setVisibility(View.GONE);
                        mSelectedGrip = null;
                        mGripTextView.setText("None");
                    }
                }
                else {
                    mSelectedGrip = array[which];
                    mGripTextView.setText(mSelectedGrip);
                }
            }
        });

        builder.setPositiveButtonIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_24));
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showInvalidDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid data");
        builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}