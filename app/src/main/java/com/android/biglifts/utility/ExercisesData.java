package com.android.biglifts.utility;

import androidx.annotation.NonNull;

import com.android.biglifts.models.ExerciseModel;

public class ExercisesData {

    public static ExerciseModel[] populateExercisesData() {
        return new ExerciseModel[] {
                new ExerciseModel("Bench Press - Barbell", "Chest", "Free weight", ExerciseModel.VISIBLE_TRUE, null, null),
                new ExerciseModel("Lat Pulldown - Wide Grip", "Back", "Cable", 1, null, ExerciseModel.HANDLE_LAT_BAR),
                new ExerciseModel("Squat - Barbell", "Legs", "Free weight", 1, null, null),
                new ExerciseModel("Overhead Press - Barbell", "Shoulders", "Free weight", 1, null, null),
                new ExerciseModel("Sit Up", "Core", "Body weight", 1, null, null),
                new ExerciseModel("Deadlift - Barbell", "Back", "Free weight", 1, null, null),
                new ExerciseModel("Leg Press", "Legs", "Machine", 1, null, null)
        };
    }

}
