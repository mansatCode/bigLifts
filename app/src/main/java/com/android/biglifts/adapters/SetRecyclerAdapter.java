package com.android.biglifts.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.biglifts.R;
import com.android.biglifts.models.LogEntryModel;

import java.util.ArrayList;

public class SetRecyclerAdapter extends RecyclerView.Adapter<SetRecyclerAdapter.ViewHolder> {

    private static final String TAG = "SetRecyclerAdapter";

    private ArrayList<LogEntryModel> mLogEntriesList;
    private SetRecyclerAdapter mSetRecyclerAdapter;
    private Context mContext;

    // Constructor
    public SetRecyclerAdapter(ArrayList<LogEntryModel> logEntriesList, Context context) {
        this.mLogEntriesList = logEntriesList;
        mContext = context;
        mSetRecyclerAdapter = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Here we inflate the corresponding layout of the child item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_add_set, viewGroup, false);
        // pass MyCustomEditTextListener to viewholder in onCreateViewHolder
        // so that we don't have to do this expensive allocation in onBindViewHolder
        ViewHolder vh = new ViewHolder(view, new WeightListener(), new RepsListener(), mContext);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SetRecyclerAdapter.ViewHolder holder, int position) {
        // Create an instance of the ChildItem class for the given position
        LogEntryModel logEntry = mLogEntriesList.get(position);
        logEntry.setSetNumber(holder.getBindingAdapterPosition() + 1);

        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mLogEntriesList to update
        holder.weightListener.updateLog(logEntry);
        holder.repsListener.updateLog(logEntry, position);

        holder.chk_confirmSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (logEntry.getReps() == 0) {
                    holder.chk_confirmSet.setChecked(false);
                    isChecked = false;
                }
                logEntry.setChecked(isChecked);
            }
        });

        switch(logEntry.getSetDetails()){
            case LogEntryModel.NORMAL_SET:
                updateSetDetailsTextView(holder.tv_setNumber, String.valueOf(logEntry.getSetNumber()), ContextCompat.getColor(mContext, R.color.white));
                break;
            case LogEntryModel.WARM_UP_SET:
                updateSetDetailsTextView(holder.tv_setNumber, "W", ContextCompat.getColor(mContext, R.color.orange));
                break;
            case LogEntryModel.DROP_SET:
                updateSetDetailsTextView(holder.tv_setNumber, "D", ContextCompat.getColor(mContext, R.color.purple_700));
                break;
            case LogEntryModel.FAILURE_SET:
                updateSetDetailsTextView(holder.tv_setNumber, "F", ContextCompat.getColor(mContext, R.color.red));
                break;
            case LogEntryModel.BACK_OFF_SET:
                updateSetDetailsTextView(holder.tv_setNumber, "B", ContextCompat.getColor(mContext, R.color.blue));
                break;
        }


        holder.tv_setNumber.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                MenuPopupHelper setDetailsPopUpMenu;
                MenuBuilder menuBuilder = new MenuBuilder(mContext);
                MenuInflater menuInflater = new MenuInflater(mContext);
                menuInflater.inflate(R.menu.pop_up_menu_set_details, menuBuilder);
                setDetailsPopUpMenu = new MenuPopupHelper(mContext, menuBuilder, view);
                setDetailsPopUpMenu.setForceShowIcon(true);

                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        // TODO - on same one selected,
                        if (logEntry.getR_setDetail_ID() == item.getItemId()) {
                            updateSetDetailsTextView(holder.tv_setNumber,  String.valueOf(logEntry.getSetNumber()), ContextCompat.getColor(mContext, R.color.white));
                            logEntry.setSetDetails(LogEntryModel.NORMAL_SET);
                            return true;
                        }

                        switch(item.getItemId()) {
                            case R.id.pop_up_menu_itm_warmUp:
                                updateSetDetailsTextView(holder.tv_setNumber, "W", ContextCompat.getColor(mContext, R.color.orange));
                                logEntry.setSetDetails(LogEntryModel.WARM_UP_SET);
                                logEntry.setR_setDetail_ID(R.id.pop_up_menu_itm_warmUp);
                                return true;
                            case R.id.pop_up_menu_itm_dropSet:
                                updateSetDetailsTextView(holder.tv_setNumber, "D", ContextCompat.getColor(mContext, R.color.purple_700));
                                logEntry.setSetDetails(LogEntryModel.DROP_SET);
                                logEntry.setR_setDetail_ID(R.id.pop_up_menu_itm_dropSet);
                                return true;
                            case R.id.pop_up_menu_itm_failure:
                                updateSetDetailsTextView(holder.tv_setNumber, "F", ContextCompat.getColor(mContext, R.color.red));
                                logEntry.setSetDetails(LogEntryModel.FAILURE_SET);
                                logEntry.setR_setDetail_ID(R.id.pop_up_menu_itm_failure);
                                return true;
                            case R.id.pop_up_menu_itm_backOff:
                                updateSetDetailsTextView(holder.tv_setNumber, "B", ContextCompat.getColor(mContext, R.color.blue));
                                logEntry.setSetDetails(LogEntryModel.BACK_OFF_SET);
                                logEntry.setR_setDetail_ID(R.id.pop_up_menu_itm_backOff);
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });

                setDetailsPopUpMenu.show();
            }
        });

        if (logEntry.getWeight() == 0) {
            holder.et_weight.setText("");
            holder.et_weight.setHint("0");
        } else {
            holder.et_weight.setText(String.valueOf(logEntry.getWeight()));
        }

        if (logEntry.getReps() == 0) {
            holder.et_reps.setText("");
            holder.et_reps.setHint("0");
            holder.chk_confirmSet.setChecked(false);
        } else {
            holder.et_reps.setText(String.valueOf(logEntry.getReps()));
        }

        holder.chk_confirmSet.setChecked(logEntry.isChecked());
    }

    private void updateSetDetailsTextView (TextView tv_setNumber, String displayText, int colour) {
        tv_setNumber.setTextColor(colour);
        tv_setNumber.setText(displayText);
    }

    @Override
    public int getItemCount() {
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

        private Context context;
        public WeightListener weightListener;
        public RepsListener repsListener;

        @SuppressLint("RestrictedApi")
        public ViewHolder(View itemView, WeightListener weightListener, RepsListener repsListener, Context context) {
            super(itemView);
            tv_setNumber = itemView.findViewById(R.id.row_add_set_tv_setNumber);
            tv_previousSetData = itemView.findViewById(R.id.row_add_set_tv_previousSetData);
            et_weight = itemView.findViewById(R.id.row_add_set_et_weight);
            et_reps = itemView.findViewById(R.id.row_add_set_et_reps);
            chk_confirmSet = itemView.findViewById(R.id.row_add_set_chk_confirmSet);

            this.context = context;
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

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // Should code go here or in afterTextChanged (?)
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() != 0) {
                log.setWeight(Integer.parseInt(editable.toString()));
            } else {
                log.setWeight(0);
            }
        }
    }

    private class RepsListener implements TextWatcher {
        private LogEntryModel log;
        private int position;

        public void updateLog(LogEntryModel log, int position) {
            this.log = log;
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() != 0) {
                log.setReps(Integer.parseInt(editable.toString()));
            } else {
                log.setReps(0);
                log.setChecked(false);
                mSetRecyclerAdapter.notifyItemChanged(position);
            }
        }
    }
}
