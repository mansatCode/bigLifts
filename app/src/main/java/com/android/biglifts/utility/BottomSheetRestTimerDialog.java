package com.android.biglifts.utility;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.android.biglifts.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetRestTimerDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "BottomSheetRestTimerDialog";

    // UI
    private LinearLayout ll_startTimerContainer;
    private ConstraintLayout cl_setTimeContainer;
    private Button btn_reset, btn_start, btn_setNewTime;
    private TextView tv_timer;
    private NumberPicker np_selectMinutes, np_selectSeconds;

    // Variables
    private BottomSheetRestTimerListener mListener;
    private String mRestTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRestTimer = this.getArguments().getString("Current rest time");
        Log.d(TAG, "onCreateView: " + mRestTimer);

        View view = inflater.inflate(R.layout.bottom_sheet_timer, container, false);

        initUI(view);
        setListeners();
        setNumberPickers();
        return view;
    }

    private void setNumberPickers() {
        np_selectMinutes.setMinValue(0);
        np_selectMinutes.setMaxValue(10);

        np_selectSeconds.setMinValue(0);
        np_selectSeconds.setMaxValue(12);
        np_selectSeconds.setDisplayedValues(new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60"});
    }

    private void initUI(View view) {
        ll_startTimerContainer = view.findViewById(R.id.bottom_sheet_timer_ll_mainBody);
        btn_reset = view.findViewById(R.id.bottom_sheet_timer_btn_reset);
        btn_start = view.findViewById(R.id.bottom_sheet_timer_btn_start);
        tv_timer = view.findViewById(R.id.bottom_sheet_timer_tv_timer);

        cl_setTimeContainer = view.findViewById(R.id.bottom_sheet_timer_cl_setTimeContainer);
        np_selectMinutes = view.findViewById(R.id.bottom_sheet_timer_np_minutes);
        np_selectSeconds = view.findViewById(R.id.bottom_sheet_timer_np_seconds);
        btn_setNewTime = view.findViewById(R.id.bottom_sheet_timer_btn_setTime);
    }

    private void setListeners() {
        tv_timer.setOnClickListener(this);
        btn_setNewTime.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_sheet_timer_tv_timer:
                ll_startTimerContainer.setVisibility(View.GONE);
                cl_setTimeContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.bottom_sheet_timer_btn_setTime:
                cl_setTimeContainer.setVisibility(View.GONE);
                ll_startTimerContainer.setVisibility(View.VISIBLE);
            case R.id.bottom_sheet_timer_btn_start:
                break;
            case R.id.bottom_sheet_timer_btn_reset:
                break;
        }
    }

    public interface BottomSheetRestTimerListener {
        void onButtonInBottomSheetRestTimerClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetRestTimerListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetRestTimerListener");
        }

    }
}
