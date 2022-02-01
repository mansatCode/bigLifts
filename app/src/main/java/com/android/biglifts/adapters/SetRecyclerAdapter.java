package com.android.biglifts.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.LogEntryModel;

import java.util.ArrayList;

public class SetRecyclerAdapter extends RecyclerView.Adapter<SetRecyclerAdapter.ViewHolder> {

    private static final String TAG = "SetRecyclerAdapter";

    private ArrayList<LogEntryModel> mLogEntriesList;

    // Constructor
    public SetRecyclerAdapter(ArrayList<LogEntryModel> logEntriesList)
    {
        this.mLogEntriesList = logEntriesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        // Here we inflate the corresponding layout of the child item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_add_set, viewGroup, false);
        // pass MyCustomEditTextListener to viewholder in onCreateViewHolder
        // so that we don't have to do this expensive allocation in onBindViewHolder
        ViewHolder vh = new ViewHolder(view, new WeightListener(), new RepsListener());
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SetRecyclerAdapter.ViewHolder holder, int position)
    {
        // Create an instance of the ChildItem class for the given position
        LogEntryModel logEntry = mLogEntriesList.get(position);
        logEntry.setSetNumber(holder.getBindingAdapterPosition()+1);

        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mLogEntriesList to update
        holder.weightListener.updateLog(logEntry);
        holder.repsListener.updateLog(logEntry);

        holder.chk_confirmSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                logEntry.setChecked(isChecked);
                Log.d(TAG, "onCheckedChanged: " + logEntry.toString());
            }
        });

        holder.tv_setNumber.setText(String.valueOf(logEntry.getSetNumber()));
        holder.et_weight.setText(String.valueOf(logEntry.getWeight()));
        holder.et_reps.setText(String.valueOf(logEntry.getReps()));
        holder.chk_confirmSet.setChecked(logEntry.isChecked());
    }

    @Override
    public int getItemCount()
    {
        // This method returns the number of items we have added in the mLogEntriesList
        // i.e. the number of instances of the mLogEntriesList that have been created
        return mLogEntriesList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SetRecyclerAdapter.ViewHolder holder) {
        ((ViewHolder) holder).enableListeners();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SetRecyclerAdapter.ViewHolder holder) {
        ((ViewHolder) holder).disableListeners();
    }

    // This class is to initialize the Views present in the child RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_setNumber, tv_previousSetData;
        EditText et_weight, et_reps;
        CheckBox chk_confirmSet;

        public WeightListener weightListener;
        public RepsListener repsListener;

        public ViewHolder(View itemView,  WeightListener weightListener, RepsListener repsListener)
        {
            super(itemView);
            tv_setNumber = itemView.findViewById(R.id.row_add_set_tv_setNumber);
            tv_previousSetData = itemView.findViewById(R.id.row_add_set_tv_previousSetData);
            et_weight = itemView.findViewById(R.id.row_add_set_et_weight);
            et_reps = itemView.findViewById(R.id.row_add_set_et_reps);
            chk_confirmSet = itemView.findViewById(R.id.row_add_set_chk_confirmSet);

//            chk_confirmSet.setClickable(false);

            this.weightListener = weightListener;
            this.repsListener = repsListener;
        }

        void enableListeners() {
            et_weight.addTextChangedListener(weightListener);
            et_reps.addTextChangedListener(repsListener);
        }

        void disableListeners() {
            et_weight.removeTextChangedListener(weightListener);
            et_reps.removeTextChangedListener(repsListener);
        }
    }

    // we make TextWatcher to be aware of the LogModel it currently works with.
    // In this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class WeightListener implements TextWatcher {
        private LogEntryModel log;

        public void updateLog(LogEntryModel log) {
            this.log = log;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // Should code go here or in afterTextChanged (?)
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() != 0) {
                log.setWeight(Integer.parseInt(editable.toString()));
            }
            else {
                log.setWeight(0);
            }

//            for (LogEntryModel l : mLogEntriesList) {
//                Log.d(TAG, "onTextChanged: " + l.toString());
//            }
        }
    }

    private class RepsListener implements TextWatcher {
        private LogEntryModel log;

        public void updateLog(LogEntryModel log) {
            this.log = log;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // Should code go here or in afterTextChanged (?)
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() != 0) {
                log.setReps(Integer.parseInt(editable.toString()));
            }
            else {
                log.setReps(0);
            }
        }
    }
}
