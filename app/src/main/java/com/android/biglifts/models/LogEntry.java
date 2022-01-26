package com.android.biglifts.models;

public class LogEntry {

    private String exerciseName;
    private int setNumber;
    private int weight;
    private int reps;

    private boolean isExpanded;

    public LogEntry(String exerciseName, int setNumber, int weight, int reps) {
        this.exerciseName = exerciseName;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
        this.isExpanded = false;
    }

    //Empty constructor
    public LogEntry() {
        this.isExpanded = false;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

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

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public String toString() {
        return "Log{" +
                "exerciseName='" + exerciseName + '\'' +
                ", setNumber=" + setNumber +
                ", weight=" + weight +
                ", reps=" + reps +
                '}';
    }
}
