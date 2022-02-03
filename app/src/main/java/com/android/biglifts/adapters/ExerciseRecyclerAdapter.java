package com.android.biglifts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.ExerciseModel;

import java.util.ArrayList;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ViewHolder> {

    public static final String EXTRA_EXERCISE_NAME = "com.android.biglifts.EXTRA_EXERCISE_NAME";
    public static final String EXTRA_EXERCISE_ID = "com.android.biglifts.EXTRA_EXERCISE_ID";

    private ArrayList<ExerciseModel> mExercisesList;
    private OnExerciseListener mOnExerciseListener;
    private Context mContext;

    public ExerciseRecyclerAdapter(Context context, ArrayList<ExerciseModel> exerciseList, OnExerciseListener onExerciseListener) {
        mContext = context;
        mExercisesList = exerciseList;
        mOnExerciseListener = onExerciseListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_exercise, parent, false);
        return new ViewHolder(view, mOnExerciseListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExerciseModel exercise = mExercisesList.get(position);

        holder.tv_exerciseName.setText(exercise.getExerciseName());
        holder.tv_exerciseBodyPart.setText(mExercisesList.get(position).getBodyPart());
        holder.tv_exerciseCategory.setText(mExercisesList.get(position).getCategory());
        holder.itemView.setTag(mExercisesList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mExercisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_exerciseName;
        TextView tv_exerciseBodyPart;
        TextView tv_exerciseCategory;

        OnExerciseListener onExerciseListener;

        public ViewHolder(View itemView, OnExerciseListener onExerciseListener) {
            super(itemView);
            tv_exerciseName = itemView.findViewById(R.id.row_exercise_tv_exerciseName);
            tv_exerciseBodyPart = itemView.findViewById(R.id.row_exercise_tv_bodyPart);
            tv_exerciseCategory = itemView.findViewById(R.id.row_exercise_tv_category);

            this.onExerciseListener = onExerciseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onExerciseListener.onClick(getBindingAdapterPosition());
        }
    }

    public interface OnExerciseListener {
        void onClick(int position);
    }
}
