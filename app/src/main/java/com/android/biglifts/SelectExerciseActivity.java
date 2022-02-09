package com.android.biglifts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.biglifts.fragments.ExercisesFragment;

public class SelectExerciseActivity extends AppCompatActivity {

    public static final String EXTRA_ADD_EXERCISE_MODE_ON = "com.android.biglifts.EXTRA_ADD_EXERCISE_MODE_ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise);

        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_ADD_EXERCISE_MODE_ON, true);
        Fragment fragment = new ExercisesFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_select_exercise_fl_fragmentContainer, fragment).commit();
    }
}