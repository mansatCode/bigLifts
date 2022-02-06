package com.android.biglifts.utility;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private static final String EXTRA_CURRENT_REST_TIME = "com.android.biglifts.EXTRA_CURRENT_REST_TIME";
    private static final String EXTRA_DEFAULT_REST_TIME = "com.android.biglifts.EXTRA_DEFAULT_REST_TIME";
    private static final String EXTRA_IS_TIMER_RUNNING = "com.android.biglifts.EXTRA_IS_TIMER_RUNNING";

    private static final int START = 0;
    private static final int PAUSE = 1;
    private static final int RESET = 2;

    // UI
    private LinearLayout ll_startTimerContainer;
    private ConstraintLayout cl_setTimeContainer;
    private Button btn_reset, btn_startPause, btn_setNewTime;
    private TextView tv_timer;
    private NumberPicker np_selectMinutes, np_selectSeconds;

    // Variables
    private BottomSheetRestTimerListener mListener;
    private String[] mSecondsArray;
    private long mRestTimeInMilliseconds;
    private long mRestTimeInMillisecondsDefault;
    private CountDownTimer mCountDownTimer;
    private boolean mIsTimerRunning;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_timer, container, false);

        initUI(view);
        mSecondsArray = getResources().getStringArray(R.array.BottomSheetRestTimerDialog_SecondsArray);
        setListeners();


        mIsTimerRunning = this.getArguments().getBoolean(EXTRA_IS_TIMER_RUNNING);
        mRestTimeInMilliseconds = this.getArguments().getLong(EXTRA_CURRENT_REST_TIME);
        mRestTimeInMillisecondsDefault = this.getArguments().getLong(EXTRA_DEFAULT_REST_TIME);

        if (mIsTimerRunning) {
            startTimer();
        }
        else {
            updateCountDownText();
        }

        setNumberPickers();
        return view;
    }

    private void setNumberPickers() {
        np_selectMinutes.setMinValue(00);
        np_selectMinutes.setMaxValue(10);

        np_selectSeconds.setMinValue(0);
        np_selectSeconds.setMaxValue(mSecondsArray.length-1);
        np_selectSeconds.setDisplayedValues(mSecondsArray);

        setNumberPickerStartValues();
    }

    private void setNumberPickerStartValues() {
        np_selectMinutes.setValue((int) (mRestTimeInMillisecondsDefault / 1000) / 60);
        int index = Arrays.asList(mSecondsArray).indexOf(String.valueOf((mRestTimeInMillisecondsDefault / 1000) % 60));
        np_selectSeconds.setValue(index);
    }

    private void initUI(View view) {
        ll_startTimerContainer = view.findViewById(R.id.bottom_sheet_timer_ll_mainBody);
        btn_reset = view.findViewById(R.id.bottom_sheet_timer_btn_reset);
        btn_startPause = view.findViewById(R.id.bottom_sheet_timer_btn_start);
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
        btn_startPause.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_sheet_timer_tv_timer:
                ll_startTimerContainer.setVisibility(View.GONE);
                cl_setTimeContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.bottom_sheet_timer_btn_setTime:
                int newMinutes = (int) np_selectMinutes.getValue() * 60 * 1000;
                int newSeconds = Integer.parseInt(mSecondsArray[np_selectSeconds.getValue()]) * 1000;
                mRestTimeInMilliseconds = newMinutes + newSeconds;
                mRestTimeInMillisecondsDefault = mRestTimeInMilliseconds;
                updateCountDownText();
                cl_setTimeContainer.setVisibility(View.GONE);
                ll_startTimerContainer.setVisibility(View.VISIBLE);
                mListener.onButtonInBottomSheetRestTimerClicked(RESET, mRestTimeInMilliseconds);
                setNumberPickerStartValues();
                break;
            case R.id.bottom_sheet_timer_btn_start:
                if (mRestTimeInMilliseconds == 0) {
                    return;
                }

                if (mIsTimerRunning) {
                    pauseTimer();
                    mListener.onButtonInBottomSheetRestTimerClicked(PAUSE, mRestTimeInMilliseconds);
                }
                else {
                    startTimer();
                    mListener.onButtonInBottomSheetRestTimerClicked(START, mRestTimeInMilliseconds);
                }
                // dismiss();
                break;
            case R.id.bottom_sheet_timer_btn_reset:
                resetTimer();
                mListener.onButtonInBottomSheetRestTimerClicked(RESET, mRestTimeInMilliseconds);
                break;
        }
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mRestTimeInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRestTimeInMilliseconds = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mIsTimerRunning = false;
                btn_startPause.setText("Start");
                btn_reset.setClickable(true);
            }
        }.start();
        mIsTimerRunning = true;
        btn_startPause.setText("Pause");
        tv_timer.setClickable(false);
        btn_reset.setClickable(true);
    }

    private void updateCountDownText() {
        int minutes = (int) (mRestTimeInMilliseconds / 1000) / 60;
        int seconds = (int) (mRestTimeInMilliseconds / 1000) % 60;
        tv_timer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void resetTimer() {
        btn_reset.setClickable(false);
        if (mIsTimerRunning) {
            pauseTimer();
        }
        mRestTimeInMilliseconds = mRestTimeInMillisecondsDefault;
        updateCountDownText();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mIsTimerRunning = false;
        btn_startPause.setText("Start");
        tv_timer.setClickable(true);
    }

    public interface BottomSheetRestTimerListener {
        void onButtonInBottomSheetRestTimerClicked(int timerState, long restTimeInMillis);
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
