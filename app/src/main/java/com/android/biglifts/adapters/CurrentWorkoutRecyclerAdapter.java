package com.android.biglifts.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.ExerciseModel;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CurrentWorkoutRecyclerAdapter extends RecyclerView.Adapter<CurrentWorkoutRecyclerAdapter.ViewHolder> {

    private static final String TAG = "CurrentWorkoutRecyclerAdapter";

    private ArrayList<ExerciseModel> mExercisesList;
    private OnExerciseInWorkoutListener mOnExerciseInWorkoutListener;
    private CurrentWorkoutRecyclerAdapter mCurrentWorkoutRecyclerAdapter;
    private Context mContext;

    public interface OnExerciseInWorkoutListener {
        void onExerciseInWorkoutClick(int position, View view);

        void deleteLogEntry(int logPosition, int exercisePosition);
    }

    public CurrentWorkoutRecyclerAdapter(ArrayList<ExerciseModel> exerciseModels, OnExerciseInWorkoutListener onExerciseInWorkoutListener, Context context) {
        this.mExercisesList = exerciseModels;
        this.mOnExerciseInWorkoutListener = onExerciseInWorkoutListener;
        mCurrentWorkoutRecyclerAdapter = this;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_current_workout_exercises, parent, false);
        return new ViewHolder(view, mOnExerciseInWorkoutListener, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentWorkoutRecyclerAdapter.ViewHolder holder, int position) {
        ExerciseModel exercise = mExercisesList.get(position);
        holder.tv_exerciseName.setText(exercise.getExerciseName());

        // Here we have assigned the layout as LinearLayout with vertical orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.rv_logEntries.getContext(), LinearLayoutManager.VERTICAL, false);

        // Since this is a nested layout, to define how many child items should be prefetched when the
        // child RecyclerView is nested inside the parent RecyclerView, we use the following method
        layoutManager.setInitialPrefetchItemCount(exercise.getLogEntriesList().size());

        // Create an instance of the child item view adapter and set its adapter and layout manager
        SetRecyclerAdapter setRecyclerAdapter = new SetRecyclerAdapter(exercise.getLogEntriesList(), mContext, (SetRecyclerAdapter.OnSetInWorkoutListener) mContext, exercise);
        holder.rv_logEntries.setLayoutManager(layoutManager);
        holder.rv_logEntries.setAdapter(setRecyclerAdapter);

        boolean isExpanded = mExercisesList.get(position).isExpanded();
        holder.cl_expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mExercisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_expand, iv_options;
        TextView tv_exerciseName;
        Button btn_addSet;
        ConstraintLayout cl_expandableLayout;
        RecyclerView rv_logEntries;

        private OnExerciseInWorkoutListener onExerciseInWorkoutListener;
        private Context context;

        public ViewHolder(@NonNull View itemView, OnExerciseInWorkoutListener onExerciseInWorkoutListener, Context context) {
            super(itemView);
            iv_expand = itemView.findViewById(R.id.row_current_workout_exercises_iv_tripleLines);
            iv_options = itemView.findViewById(R.id.row_current_workout_exercises_iv_options);
            tv_exerciseName = itemView.findViewById(R.id.row_current_workout_exercises_tv_exerciseName);
            btn_addSet = itemView.findViewById(R.id.row_current_workout_exercises_btn_addSet);
            cl_expandableLayout = itemView.findViewById(R.id.row_current_workout_exercises_cl_expandableLayout);
            rv_logEntries = itemView.findViewById(R.id.row_current_workout_exercises_rv);

            this.context = context;
            this.onExerciseInWorkoutListener = onExerciseInWorkoutListener;

            iv_expand.setOnClickListener(this);
            iv_options.setOnClickListener(this);
            btn_addSet.setOnClickListener(this);

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    onExerciseInWorkoutListener.deleteLogEntry(viewHolder.getBindingAdapterPosition(), getBindingAdapterPosition());
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addBackgroundColor(ContextCompat.getColor(mContext, R.color.red))
                            .addActionIcon(R.drawable.ic_baseline_delete_24)
                            .create()
                            .decorate();

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(rv_logEntries);
        }

        @Override
        public void onClick(View view) {
            onExerciseInWorkoutListener.onExerciseInWorkoutClick(getBindingAdapterPosition(), view);
        }
    }
}
