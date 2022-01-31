package com.android.biglifts.adapters;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetRecyclerAdapter.ViewHolder holder, int position)
    {
        // Create an instance of the ChildItem class for the given position
        LogEntryModel logEntry = mLogEntriesList.get(position);
        logEntry.setSetNumber(holder.getBindingAdapterPosition()+1);

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


    // This class is to initialize the Views present in the child RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_setNumber, tv_previousSetData;
        EditText et_weight, et_reps;
        CheckBox chk_confirmSet;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tv_setNumber = itemView.findViewById(R.id.row_add_set_tv_setNumber);
            tv_previousSetData = itemView.findViewById(R.id.row_add_set_tv_previousSetData);
            et_weight = itemView.findViewById(R.id.row_add_set_et_weight);
            et_reps = itemView.findViewById(R.id.row_add_set_et_reps);
            chk_confirmSet = itemView.findViewById(R.id.row_add_set_chk_confirmSet);
        }
    }
}
