package com.android.biglifts.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.biglifts.R;
import com.android.biglifts.adapters.ExerciseRecyclerAdapter;
import com.android.biglifts.adapters.PastWorkoutRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.WorkoutModel;
import com.android.biglifts.persistence.BigLiftsRepository;

import java.util.ArrayList;
import java.util.List;

public class SummaryFragment extends Fragment {

    // Constants
    private static final String TAG = "SummaryFragment";

    // UI Components
    private RecyclerView mRecyclerView;

    // Variables
    private BigLiftsRepository mBigLiftsRepository;
    private Context mContext;
    private ArrayList<WorkoutModel> mPastWorkoutsList = new ArrayList<>();
    private PastWorkoutRecyclerAdapter mPastWorkoutRecyclerAdapter;

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        mBigLiftsRepository = new BigLiftsRepository(mContext);
        initRecyclerView();
        retrieveWorkouts();
        setListeners();
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.fragment_summary_rv);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPastWorkoutRecyclerAdapter = new PastWorkoutRecyclerAdapter(mContext, mPastWorkoutsList);
        mRecyclerView.setAdapter(mPastWorkoutRecyclerAdapter);
    }

    private void retrieveWorkouts() {
        mBigLiftsRepository.getAllWorkouts().observe(getViewLifecycleOwner(), new Observer<List<WorkoutModel>>() {
            @Override
            public void onChanged(List<WorkoutModel> workoutModels) {
                if (mPastWorkoutsList.size() > 0) {
                    mPastWorkoutsList.clear();
                }
                if (workoutModels != null) {
                    mPastWorkoutsList.addAll(workoutModels);
                }
                mPastWorkoutRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setListeners() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = container.getContext();

        return inflater.inflate(R.layout.fragment_summary, container, false);
    }
}