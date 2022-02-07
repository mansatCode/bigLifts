package com.android.biglifts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.adapters.ExerciseRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ExercisesFragment extends Fragment implements
        ExerciseRecyclerAdapter.OnExerciseListener {

    public static final String EXTRA_EXERCISE_NAME = "com.android.biglifts.EXTRA_EXERCISE_NAME";
    public static final String EXTRA_EXERCISE_ID = "com.android.biglifts.EXTRA_EXERCISE_ID";
    public static final String EXTRA_EXERCISE = "com.android.biglifts.EXTRA_EXERCISE";
    private static final String TAG = "ExercisesFragment";

    // UI Components
    private RecyclerView mRecyclerView;
    private ChipGroup mChipGroup;

    // Variables
    public Boolean mIsAddExerciseModeOn = false;
    private Context mContext;
    private BigLiftsRepository mBigLiftsRepository;
    private ExerciseRecyclerAdapter mExerciseRecyclerAdapter;

    private ArrayList<ExerciseModel> mExercisesList = new ArrayList<>();

    // Required empty public constructor
    public ExercisesFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        mBigLiftsRepository = new BigLiftsRepository(mContext);
        initRecyclerView();
        retrieveAllExercises();

        if (mIsAddExerciseModeOn) {
            CoordinatorLayout cl_container = getView().findViewById(R.id.fragment_exercises_cdl_container);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cl_container.getLayoutParams();
            params.bottomMargin = 0;
            cl_container.setLayoutParams(params);
        }
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.fragment_exercises_rv);
        mChipGroup = view.findViewById(R.id.chip_group);
    }

    private void retrieveAllExercises() {
        mBigLiftsRepository.getAllExercisesOrderedAlphabetically().observe(getViewLifecycleOwner(), new Observer<List<ExerciseModel>>() {
            @Override
            public void onChanged(List<ExerciseModel> exerciseModels) {
                if (mExercisesList.size() > 0) {
                    mExercisesList.clear();
                }
                if (exerciseModels != null) {
                    mExercisesList.addAll(exerciseModels);
                }
                mExerciseRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mExerciseRecyclerAdapter = new ExerciseRecyclerAdapter(mContext, mExercisesList, this);
        mRecyclerView.setAdapter(mExerciseRecyclerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();

        Bundle b = getArguments();
        if (b != null) {
            mIsAddExerciseModeOn = getArguments().getBoolean(SelectExerciseActivity.EXTRA_ADD_EXERCISE_MODE_ON);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercises, container, false);
    }

    @Override
    public void onClick(int position) {
        if (mIsAddExerciseModeOn) {
            // Send the ExerciseModel to the workout activity
            Intent result = new Intent();
            result.putExtra(EXTRA_EXERCISE, mExercisesList.get(position));
            getActivity().setResult(Activity.RESULT_OK, result);
            getActivity().finish();
        } else {
            // Fragment stuff... see mansatLifts
        }
    }
}

