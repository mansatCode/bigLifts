package com.android.biglifts.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.AddExerciseActivity;
import com.android.biglifts.R;
import com.android.biglifts.SelectExerciseActivity;
import com.android.biglifts.SpecificExerciseActivity;
import com.android.biglifts.adapters.ExerciseRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ExercisesFragment extends Fragment implements
        ExerciseRecyclerAdapter.OnExerciseListener,
        ChipGroup.OnCheckedChangeListener,
        Toolbar.OnMenuItemClickListener {

    // Constants
    public static final String EXTRA_EXERCISE_NAME = "com.android.biglifts.EXTRA_EXERCISE_NAME";
    public static final String EXTRA_EXERCISE_ID = "com.android.biglifts.EXTRA_EXERCISE_ID";
    public static final String EXTRA_EXERCISE = "com.android.biglifts.EXTRA_EXERCISE";
    private static final String TAG = "ExercisesFragment";

    // UI Components
    private RecyclerView mRecyclerView;
    private ChipGroup mChipGroup;
    private Toolbar mToolbar;

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
        setListeners();

        if (mIsAddExerciseModeOn) {
            CoordinatorLayout cdl_container = getView().findViewById(R.id.fragment_exercises_cdl_container);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cdl_container.getLayoutParams();
            params.bottomMargin = 0;
            cdl_container.setLayoutParams(params);
        }
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.fragment_exercises_rv);
        mChipGroup = view.findViewById(R.id.fragment_exercises_cg);
        mToolbar = view.findViewById(R.id.fragment_exercises_tb);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    }

    private void setListeners() {
        mChipGroup.setOnCheckedChangeListener(this);
        mToolbar.setOnMenuItemClickListener(this);
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
                mExerciseRecyclerAdapter.updateLists();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mExerciseRecyclerAdapter = new ExerciseRecyclerAdapter(mContext, mExercisesList, this);
        mRecyclerView.setAdapter(mExerciseRecyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exercises, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_exercises_itm_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //Change search icon on keyboard from magnifying glass to tick
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mExerciseRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
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
            Intent intent = new Intent(mContext, SpecificExerciseActivity.class);
            intent.putExtra(EXTRA_EXERCISE_NAME, mExercisesList.get(position).getExerciseName());
            intent.putExtra(EXTRA_EXERCISE_ID, (long) mExercisesList.get(position).getId());
            mContext.startActivity(intent);
        }
    }


    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {
        if (checkedId == -1) {
            mExerciseRecyclerAdapter.getFilter().filter(null);
            return;
        }
        Chip chip = getView().findViewById(checkedId);
        mExerciseRecyclerAdapter.getFilter().filter(chip.getText());
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_exercises_itm_addExercise:
                Intent i = new Intent(mContext, AddExerciseActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            ExerciseModel hiddenExercise = mExercisesList.get(position);
            hiddenExercise.setIsVisible(ExerciseModel.VISIBLE_FALSE);
            mBigLiftsRepository.updateExercise(hiddenExercise);

            Snackbar sb_UndoHideExercise = Snackbar.make(mRecyclerView, mExercisesList.get(position).getExerciseName() + " is now hidden", Snackbar.LENGTH_LONG);
            sb_UndoHideExercise.setBackgroundTint(ContextCompat.getColor(mContext, R.color.primaryLightColor));
            sb_UndoHideExercise.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            sb_UndoHideExercise.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hiddenExercise.setIsVisible(ExerciseModel.VISIBLE_TRUE);
                    mBigLiftsRepository.updateExercise(hiddenExercise);
                }
            }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(mContext, R.color.orange))
                    .addActionIcon(R.drawable.ic_baseline_hide)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}

