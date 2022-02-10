package com.android.biglifts.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.ExerciseModel;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "ExerciseRecyclerAdapter";
    public static final String EXTRA_EXERCISE_NAME = "com.android.biglifts.EXTRA_EXERCISE_NAME";
    public static final String EXTRA_EXERCISE_ID = "com.android.biglifts.EXTRA_EXERCISE_ID";

    private ArrayList<ExerciseModel> mExercisesList;
    private ArrayList<ExerciseModel> mExercisesListFull;
    private OnExerciseListener mOnExerciseListener;
    private Context mContext;

    public ExerciseRecyclerAdapter(Context context, ArrayList<ExerciseModel> exerciseList, OnExerciseListener onExerciseListener) {
        mContext = context;
        mExercisesList = exerciseList;
        mExercisesListFull = new ArrayList<>(mExercisesList);
        mOnExerciseListener = onExerciseListener;
    }

    public void updateLists() {
        mExercisesListFull = new ArrayList<>(mExercisesList);
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

        if (mExercisesList.get(position).getCategory().equals("Cable")) {
            holder.tv_exerciseGrip.setText(mExercisesList.get(position).getCableGrip());
            holder.tv_exerciseGrip.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tv_exerciseGrip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mExercisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_exerciseName, tv_exerciseBodyPart, tv_exerciseCategory, tv_exerciseGrip;

        OnExerciseListener onExerciseListener;

        public ViewHolder(View itemView, OnExerciseListener onExerciseListener) {
            super(itemView);
            tv_exerciseName = itemView.findViewById(R.id.row_exercise_tv_exerciseName);
            tv_exerciseBodyPart = itemView.findViewById(R.id.row_exercise_tv_bodyPart);
            tv_exerciseCategory = itemView.findViewById(R.id.row_exercise_tv_category);
            tv_exerciseGrip = itemView.findViewById(R.id.row_exercise_tv_grip);

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

    @Override
    public Filter getFilter() {
        return exerciseFilter;
    }

    private Filter exerciseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExerciseModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mExercisesListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ExerciseModel exercise : mExercisesListFull) {
                    //Doesn't work exactly as anticipated. If "BACK" is searched, back exercises will show up as well as exercises with "back" in the name.
                    if (exercise.getExerciseName().toLowerCase().contains(filterPattern) ||
                            exercise.getBodyPart().toLowerCase().contains(filterPattern)) {
                        filteredList.add(exercise);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            mExercisesList.clear();
            mExercisesList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
