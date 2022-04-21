package com.android.biglifts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.WorkoutModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class PastWorkoutRecyclerAdapter extends RecyclerView.Adapter<PastWorkoutRecyclerAdapter.ViewHolder> {

    // Constants
    private static final String TAG = "PastWorkoutRecyclerAdap";

    // Variables
    private Context mContext;
    private ArrayList<WorkoutModel> mPastWorkoutsList;

    public PastWorkoutRecyclerAdapter(Context context, ArrayList<WorkoutModel> workoutsList) {
        mContext = context;
        mPastWorkoutsList = workoutsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_past_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastWorkoutRecyclerAdapter.ViewHolder holder, int position) {
        WorkoutModel workoutModel = mPastWorkoutsList.get(position);

        holder.tv_workoutName.setText(workoutModel.getWorkoutName());

        String workoutDuration;
        long workoutDurationInMS = workoutModel.getWorkoutDuration();
        if (workoutDurationInMS < 60000) {
            workoutDuration = "<1m";
        }
        else {
            int minutes = (int) ((workoutDurationInMS / (1000*60)) % 60);
            int hours   = (int) ((workoutDurationInMS / (1000*60*60)) % 24);

            if (workoutDurationInMS > 3600000) { // over an hour
                workoutDuration = hours + "h " + minutes + "m";
            }
            else {
                workoutDuration = minutes + "m";
            }
        }

        holder.tv_workoutDuration.setText(workoutDuration);


        Calendar workoutDate = workoutModel.getWorkoutDate();
        String month = getMonth(workoutDate.get(Calendar.MONTH));
        int day = workoutDate.get(Calendar.DAY_OF_MONTH);
        holder.tv_workoutDate.setText(month + " " + day);
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    @Override
    public int getItemCount() {
        return mPastWorkoutsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_workoutName, tv_workoutDate, tv_workoutDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_workoutName = itemView.findViewById(R.id.row_past_workout_tv_workoutName);
            tv_workoutDate = itemView.findViewById(R.id.row_past_workout_tv_date);
            tv_workoutDuration = itemView.findViewById(R.id.row_past_workout_tv_duration);
        }
    }

}
