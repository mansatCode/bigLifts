package com.android.biglifts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.adapters.CreateTemplateRecyclerAdapter;
import com.android.biglifts.models.ExerciseModel;
import com.android.biglifts.models.ExerciseTemplateLinkModel;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.android.biglifts.utility.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AddTemplateActivity extends AppCompatActivity implements
        View.OnClickListener,
        CreateTemplateRecyclerAdapter.OnExerciseInTemplateListener,
        PopupMenu.OnMenuItemClickListener {

    // Constants
    private static final String TAG = "AddTemplateActivity";
    public static final String EXTRA_EXERCISE = "com.android.biglifts.EXTRA_EXERCISE";

    // UI Components
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private EditText mTemplateTitleEditText;
    private Button mAddExerciseButton;
    private FloatingActionButton mSaveTemplateFAB;

    // Variables
    private ArrayList<ExerciseModel> mExercisesList = new ArrayList<>();
    private ArrayList<ExerciseTemplateLinkModel> mExerciseTemplateLinksList = new ArrayList<>();
    private boolean mIsTemplateValid = false;
    private CreateTemplateRecyclerAdapter mCreateTemplateRecyclerAdapter;
    private BigLiftsRepository mBigLiftsRepository;
    private ExerciseModel mExerciseSelected;

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
        mTemplateTitleEditText = findViewById(R.id.activity_add_template_et_templateName);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(15);
        mRecyclerView.addItemDecoration(itemDecorator);
        mCreateTemplateRecyclerAdapter = new CreateTemplateRecyclerAdapter(mExercisesList, (CreateTemplateRecyclerAdapter.OnExerciseInTemplateListener) this, AddTemplateActivity.this);
        mRecyclerView.setAdapter(mCreateTemplateRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(AddTemplateActivityItemTouchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void insertTemplate() {

    }

    private void clearTemplateTitleFocus() {
        mTemplateTitleEditText.clearFocus();
    }

    @Override
    public void onClick(View view) {
        clearTemplateTitleFocus();
        switch (view.getId()) {
            case R.id.activity_add_template_fab_saveTemplate:
                Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_add_template_btn_addExercise:
                Intent intent = new Intent(AddTemplateActivity.this, SelectExerciseActivity.class);
                mSelectExerciseActivity.launch(intent);
                break;
        }
    }

    private final ActivityResultLauncher<Intent> mSelectExerciseActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent.hasExtra(EXTRA_EXERCISE)) {
                            ExerciseModel newExercise = intent.getParcelableExtra(EXTRA_EXERCISE);
                            int newExerciseID = newExercise.getId();
                            for (ExerciseModel exerciseModel : mExercisesList) {
                                if (newExerciseID == exerciseModel.getId()) {
                                    Snackbar sb_DuplicateExerciseError = Snackbar.make(mTemplateTitleEditText, "Exercise already present in template.", Snackbar.LENGTH_LONG);
                                    sb_DuplicateExerciseError.setBackgroundTint(ContextCompat.getColor(AddTemplateActivity.this, R.color.primaryLightColor));
                                    sb_DuplicateExerciseError.setTextColor(ContextCompat.getColor(AddTemplateActivity.this, R.color.white));
                                    sb_DuplicateExerciseError.show();
                                    return;
                                }
                            }
                            mExercisesList.add(newExercise);
                            mCreateTemplateRecyclerAdapter.notifyItemInserted(mExercisesList.size());
                        } else {
                            Log.e(TAG, "onActivityResult: ERROR HAS OCCURRED");
                        }
                    }
                }
            });

    @Override
    public void onExerciseInTemplateClick(int position, View view) {
        clearTemplateTitleFocus();
        ExerciseModel exercise = mExercisesList.get(position);
        mExerciseSelected = exercise;
        switch (view.getId()) {
            case R.id.row_exercise_in_template_iv_options:
                showOptionsPopupMenu(view);
                break;
        }
    }

    private void showOptionsPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setGravity(Gravity.END);
        popupMenu.inflate(R.menu.pop_up_menu_exercise_in_template);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop_up_menu_exercise_in_template_itm_remove:
                int index = mExercisesList.indexOf(mExerciseSelected);
                mExercisesList.remove(mExerciseSelected);
                mCreateTemplateRecyclerAdapter.notifyItemRemoved(index);
                return true;
            default:
                return false;
        }
    }

    //Move exercise in workout around
    ItemTouchHelper.SimpleCallback AddTemplateActivityItemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getBindingAdapterPosition();
            int toPosition = target.getBindingAdapterPosition();

            Collections.swap(mExercisesList, fromPosition, toPosition);
            mCreateTemplateRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.findViewById(R.id.row_exercise_in_template_cl_container).setBackground(ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_highlighted_exercise_in_workout));
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.findViewById(R.id.row_exercise_in_template_cl_container).setBackground(ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_row_exercise));
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            ExerciseModel removedExercise = mExercisesList.get(position);
            mExercisesList.remove(removedExercise);
            mCreateTemplateRecyclerAdapter.notifyItemRemoved(position);

            Snackbar sb_UndoRemoveExercise = Snackbar.make(mRecyclerView, removedExercise.getExerciseName() + " was removed", Snackbar.LENGTH_LONG);
            sb_UndoRemoveExercise.setBackgroundTint(ContextCompat.getColor(AddTemplateActivity.this, R.color.primaryLightColor));
            sb_UndoRemoveExercise.setTextColor(ContextCompat.getColor(AddTemplateActivity.this, R.color.white));
            sb_UndoRemoveExercise.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mExercisesList.add(position, removedExercise);
                    mCreateTemplateRecyclerAdapter.notifyItemInserted(position);
                }
            }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(AddTemplateActivity.this, R.color.red))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}