package com.android.biglifts.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
        ViewHolder vh = new ViewHolder(view, new MyCustomEditTextListener());
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
        holder.myCustomEditTextListener.updateLog(logEntry);

        holder.tv_setNumber.setText(String.valueOf(logEntry.getSetNumber()));
        holder.et_weight.setText(String.valueOf(logEntry.getWeight()));
        holder.et_reps.setText(String.valueOf(logEntry.getReps()));
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
        ((ViewHolder) holder).enableTextWatcher();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SetRecyclerAdapter.ViewHolder holder) {
        ((ViewHolder) holder).disableTextWatcher();
    }

    // This class is to initialize the Views present in the child RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_setNumber, tv_previousSetData;
        EditText et_weight, et_reps;
        CheckBox chk_confirmSet;

        public MyCustomEditTextListener myCustomEditTextListener;

        public ViewHolder(View itemView,  MyCustomEditTextListener myCustomEditTextListener)
        {
            super(itemView);
            tv_setNumber = itemView.findViewById(R.id.row_add_set_tv_setNumber);
            tv_previousSetData = itemView.findViewById(R.id.row_add_set_tv_previousSetData);
            et_weight = itemView.findViewById(R.id.row_add_set_et_weight);
            et_reps = itemView.findViewById(R.id.row_add_set_et_reps);
            chk_confirmSet = itemView.findViewById(R.id.row_add_set_chk_confirmSet);

            this.myCustomEditTextListener = myCustomEditTextListener;
        }

        void enableTextWatcher() {
            et_weight.addTextChangedListener(myCustomEditTextListener);
        }

        void disableTextWatcher() {
            et_weight.removeTextChangedListener(myCustomEditTextListener);
        }
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
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
//            log.setWeight(Integer.parseInt(charSequence.toString()));
//
//            for (LogEntryModel l : mLogEntriesList){
//                Log.d(TAG, "onTextChanged: " + l.toString());
//            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
            if (editable.toString().length() != 0) {
                log.setWeight(Integer.parseInt(editable.toString()));
            }
            else {
                log.setWeight(0);
            }

            for (LogEntryModel l : mLogEntriesList){
                Log.d(TAG, "onTextChanged: " + l.toString());
            }
        }
    }
}
