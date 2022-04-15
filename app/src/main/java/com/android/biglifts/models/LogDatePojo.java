package com.android.biglifts.models;

import androidx.room.TypeConverters;

import com.android.biglifts.utility.DateConverter;

import java.util.Calendar;

public class LogDatePojo {

    private int weight;
    private int reps;
    private int setDetails;

    @TypeConverters({DateConverter.class})
    private Calendar logDate;

    private String simpleDate;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSetDetails() {
        return setDetails;
    }

    public void setSetDetails(int setDetails) {
        this.setDetails = setDetails;
    }

    public Calendar getLogDate() {
        return logDate;
    }

    public void setLogDate(Calendar logDate) {
        this.logDate = logDate;
    }


    public String getSimpleDate() {
        return simpleDate;
    }

    public void setSimpleDate(String simpleDate) {
        this.simpleDate = simpleDate;
    }

}
