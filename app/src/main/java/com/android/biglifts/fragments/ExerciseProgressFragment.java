package com.android.biglifts.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.android.biglifts.R;
import com.android.biglifts.SpecificExerciseActivity;
import com.android.biglifts.models.LogDatePojo;
import com.android.biglifts.persistence.BigLiftsRepository;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExerciseProgressFragment extends Fragment {

    // Constants
    private static final String TAG = "ExerciseProgressFragmen";

    // UI Components
    private ScatterChart mScatterChart;

    // Variables
    private ArrayList<LogDatePojo> mLogList = new ArrayList<>();
    private BigLiftsRepository mBigLiftsRepository;
    private Context mContext;
    private int mExerciseID = -1;
    private HashMap<String, Integer> dateMap = new HashMap();

    public ExerciseProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpecificExerciseActivity specificExerciseActivity = (SpecificExerciseActivity) getActivity();
        mExerciseID = (int) specificExerciseActivity.getExerciseID();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        mBigLiftsRepository = new BigLiftsRepository(mContext);
        initUI(view);

        retrieveExerciseData(mExerciseID);

        customiseChart();
        initChart();
    }

    private void initUI(View view) {
        mScatterChart = view.findViewById(R.id.fragment_exercise_progress_mpsc_scatterChart);
    }

    private void initChart() {
        if (mLogList.isEmpty()) {
            return;
        }

        ScatterDataSet scatterDataSet;

        ArrayList<Entry> scatterList = new ArrayList<>();
        int i = 0;
        for (LogDatePojo logDatePojo : mLogList) {
            String logDate = logDatePojo.getSimpleDate();
            i = dateMap.get(logDate);
            scatterList.add(new Entry(i, logDatePojo.getWeight()));
        }

        scatterDataSet = new ScatterDataSet(scatterList, "Weight");
        scatterDataSet.setColor(ContextCompat.getColor(mContext, R.color.blue)); // Line colour
        scatterDataSet.setDrawValues(false); // Hides values written on graph

        if (mLogList.size() > 0) {
            ScatterData scatterData = new ScatterData(scatterDataSet);
            mScatterChart.setData(scatterData);
        } else {
            mScatterChart.invalidate();
            mScatterChart.clear();
        }
    }

    private void retrieveExerciseData(int exerciseID) {
        mBigLiftsRepository.getLogChartDataByExerciseID(exerciseID).observe(getViewLifecycleOwner(), new Observer<List<LogDatePojo>>() {
            @Override
            public void onChanged(List<LogDatePojo> logDatePojos) {
                if (mLogList.size() > 0) {
                    mLogList.clear();
                }
                if (logDatePojos != null) {
                    mLogList.addAll(logDatePojos);
                    setDate(mLogList);
                    List<String> uniqueDates = getUniqueDates(mLogList);
                    setHashMap(uniqueDates);
                    setXAxis(uniqueDates);
                    initChart();
                    mScatterChart.invalidate();
                }

            }
        });
    }

    private ArrayList<String> getUniqueDates(ArrayList<LogDatePojo> logList) {
        ArrayList<String> uniqueDates = new ArrayList<>();

        String currentDate = logList.get(0).getSimpleDate();
        for (LogDatePojo l : logList) {
            if (currentDate.equals(l.getSimpleDate())) {

            }
            else {
                uniqueDates.add(currentDate);
                currentDate = l.getSimpleDate();
            }
        }
        String finalDate = logList.get(logList.size()-1).getSimpleDate();

        if (!finalDate.equals(uniqueDates.get(uniqueDates.size()-1))) {
            uniqueDates.add(finalDate);
        }

        return uniqueDates;
    }

    private void setHashMap(List<String> uniqueDates) {
        dateMap.clear();
        int index = 0;

        for (String s : uniqueDates) {
            Log.d(TAG, "setHashMap: " + s);
            dateMap.put(s, index++);
        }
    }

    private void setDate(ArrayList<LogDatePojo> logList) {
        for (LogDatePojo l : logList) {
            int day = l.getLogDate().get(Calendar.DAY_OF_MONTH);
            int month = l.getLogDate().get(Calendar.MONTH);
            int year = l.getLogDate().get(Calendar.YEAR);
            l.setSimpleDate(day + "/" + month + "/" + year);
        }
    }

    private void customiseChart() {
        //Customise axes
        mScatterChart.getAxisRight().setEnabled(true);
        mScatterChart.getAxisRight().setTextColor(Color.WHITE);
        mScatterChart.getAxisLeft().setEnabled(false);
        mScatterChart.setExtraOffsets(0f, 0f, 0f, 20f); // Space between legend and graph

        mScatterChart.setDragEnabled(true);
        mScatterChart.setScaleEnabled(true);
        mScatterChart.getDescription().setEnabled(false);
        mScatterChart.getLegend().setEnabled(false);
        mScatterChart.setNoDataTextColor(ContextCompat.getColor(mContext, R.color.blue));
        mScatterChart.getLegend().setTextColor(Color.WHITE);
        mScatterChart.setNoDataText("No data available");

        // Customise background colour
        mScatterChart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primaryDarkColor));
        mScatterChart.setBorderColor(ContextCompat.getColor(mContext, R.color.primaryDarkColor));
        mScatterChart.setGridBackgroundColor(ContextCompat.getColor(mContext, R.color.primaryDarkColor));

        mScatterChart.setXAxisRenderer(new CustomXAxisRenderer(mScatterChart.getViewPortHandler(), mScatterChart.getXAxis(), mScatterChart.getTransformer(YAxis.AxisDependency.LEFT)));
    }

    public class CustomXAxisRenderer extends XAxisRenderer {
        public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
        }

        @Override
        protected void drawLabel(Canvas c, String formattedLabel, float x, float y,
                                 MPPointF anchor, float angleDegrees) {
            String line[] = formattedLabel.split("\n");
            Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
            for (int i = 1; i < line.length; i++) { // we've already processed 1st line
                Utils.drawXAxisValue(c, line[i], x, y + mAxisLabelPaint.getTextSize() * i,
                        mAxisLabelPaint, anchor, angleDegrees);
            }
        }

    }

    public class MyXAxisValueFormatter extends ValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            int val = (int) (value);
            String label = "";
            if (val >= 0 && val < mValues.length) {
                label = mValues[val];
            } else {
                label = "";
            }
            return label;
        }
    }

    private void setXAxis(List<String> uniqueDates) {
        ArrayList<String> datesList = new ArrayList<>();

        for (String date : uniqueDates) {
            datesList.add(date);
        }

        String[] xAxisValues = datesList.toArray(new String[0]);

        XAxis xAxis = mScatterChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(xAxisValues));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_progress, container, false);
    }
}