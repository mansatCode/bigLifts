package com.android.biglifts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.ExerciseModel;

import java.util.ArrayList;

public class CreateTemplateRecyclerAdapter extends RecyclerView.Adapter<CreateTemplateRecyclerAdapter.ViewHolder> {

    // Variables
    private ArrayList<ExerciseModel> mExercisesList;
    private OnExerciseInTemplateListener mOnExerciseInTemplateListener;
    private Context mContext;

    public interface OnExerciseInTemplateListener {
        void onExerciseInTemplateClick(int position, View view);
    }

    public CreateTemplateRecyclerAdapter(ArrayList<ExerciseModel> exerciseModels, OnExerciseInTemplateListener onExerciseInTemplateListener, Context context) {
        this.mExercisesList = exerciseModels;
        this.mOnExerciseInTemplateListener = onExerciseInTemplateListener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exercise_in_template, parent, false);
        return new ViewHolder(view, mOnExerciseInTemplateListener, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateTemplateRecyclerAdapter.ViewHolder holder, int position) {
        ExerciseModel exercise = mExercisesList.get(position);

        holder.tv_exerciseName.setText(exercise.getExerciseName());
        holder.tv_exerciseBodyPart.setText(exercise.getBodyPart());
        holder.itemView.setTag(exercise.getId());

        StringBuilder stringBuilder = new StringBuilder();
        if (exercise.getCategory().equals("Cable")) {
            stringBuilder.append("(");
            stringBuilder.append(exercise.getCategory());
            stringBuilder.append(" - ");
            stringBuilder.append(exercise.getCableGrip());
            stringBuilder.append(")");
            holder.tv_exerciseCategory.setText(stringBuilder.toString());
        }
        else {
            stringBuilder.append("(");
            stringBuilder.append(exercise.getCategory());
            stringBuilder.append(")");
            holder.tv_exerciseCategory.setText(stringBuilder.toString());
        }
    }

    @Override
    public int getItemCount() {
        return mExercisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_exerciseName;
        TextView tv_exerciseBodyPart;
        TextView tv_exerciseCategory;
        ImageView iv_options;

        private OnExerciseInTemplateListener onExerciseInTemplateListener;
        private Context context;

        public ViewHolder(View itemView, OnExerciseInTemplateListener onExerciseInTemplateListener, Context context) {
            super(itemView);
            tv_exerciseName = itemView.findViewById(R.id.row_exercise_in_template_tv_exerciseName);
            tv_exerciseBodyPart = itemView.findViewById(R.id.row_exercise_in_template_tv_bodyPart);
            tv_exerciseCategory = itemView.findViewById(R.id.row_exercise_in_template_tv_category);
            iv_options = itemView.findViewById(R.id.row_exercise_in_template_iv_options);

            this.context = context;
            this.onExerciseInTemplateListener = onExerciseInTemplateListener;

            iv_options.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onExerciseInTemplateListener.onExerciseInTemplateClick(getBindingAdapterPosition(), view);
        }
    }
}
