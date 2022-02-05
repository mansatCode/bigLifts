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

import com.android.biglifts.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;
import java.util.Locale;

public class BottomSheetRestTimerDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "BottomSheetRestTimerDialog";
    private static final String EXTRA_REST_TIME = "com.android.biglifts.EXTRA_REST_TIME";
    private static final boolean START = true;
    private static final boolean STOP = false;

    // UI
    private LinearLayout ll_startTimerContainer;
    private ConstraintLayout cl_setTimeContainer;
    private Button btn_reset, btn_start, btn_setNewTime;
    private TextView tv_timer;
    private NumberPicker np_selectMinutes, np_selectSeconds;

    // Variables
    private BottomSheetRestTimerListener mListener;
    private String mRestTime;
    private String[] mSecondsArray;
    private long mRestTimeInMilliseconds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_timer, container, false);

        mRestTimeInMilliseconds = this.getArguments().getLong(EXTRA_REST_TIME);
        int minutes = (int) (mRestTimeInMilliseconds / 1000) / 60;
        int seconds = (int) (mRestTimeInMilliseconds / 1000) % 60;
        mRestTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        initUI(view);

        mSecondsArray = getResources().getStringArray(R.array.BottomSheetRestTimerDialog_SecondsArray);

        setListeners();
        setNumberPickers();

        return view;
    }

    private void setNumberPickers() {
        np_selectMinutes.setMinValue(00);
        np_selectMinutes.setMaxValue(10);

        np_selectSeconds.setMinValue(0);
        np_selectSeconds.setMaxValue(mSecondsArray.length-1);
        np_selectSeconds.setDisplayedValues(mSecondsArray);

        resetTimer();
    }

    private void resetTimer() {
        np_selectMinutes.setValue(Integer.parseInt(mRestTime.substring(0, 2)));
        int index = Arrays.asList(mSecondsArray).indexOf(String.valueOf((mRestTimeInMilliseconds / 1000) % 60));
        np_selectSeconds.setValue(index);
    }

    private void initUI(View view) {
        ll_startTimerContainer = view.findViewById(R.id.bottom_sheet_timer_ll_mainBody);
        btn_reset = view.findViewById(R.id.bottom_sheet_timer_btn_reset);
        btn_start = view.findViewById(R.id.bottom_sheet_timer_btn_start);
        tv_timer = view.findViewById(R.id.bottom_sheet_timer_tv_timer);
        tv_timer.setText(mRestTime);

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
                int minutes = (int) np_selectMinutes.getValue() * 60 * 1000;
                int seconds = Integer.parseInt(mSecondsArray[np_selectSeconds.getValue()]) * 1000;
                mRestTimeInMilliseconds = minutes + seconds;
                String min = String.format("%02d", np_selectMinutes.getValue());
                String sec = String.valueOf(mSecondsArray[np_selectSeconds.getValue()]);
                mRestTime = min + ":" + sec;
                tv_timer.setText(mRestTime);
                cl_setTimeContainer.setVisibility(View.GONE);
                ll_startTimerContainer.setVisibility(View.VISIBLE);
            case R.id.bottom_sheet_timer_btn_start:
                mListener.onButtonInBottomSheetRestTimerClicked(START, mRestTimeInMilliseconds);
                dismiss();
                break;
            case R.id.bottom_sheet_timer_btn_reset:
                mListener.onButtonInBottomSheetRestTimerClicked(STOP, mRestTimeInMilliseconds);
                resetTimer();
                break;
        }
    }

    public interface BottomSheetRestTimerListener {
        void onButtonInBottomSheetRestTimerClicked(Boolean startTimer, long mRestTimeInMillis);
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
