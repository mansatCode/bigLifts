package com.android.biglifts.utility;

import com.android.biglifts.models.ExerciseModel;

public class ExercisesData {

    public static ExerciseModel[] populateExercisesData() {
        return new ExerciseModel[] {
                new ExerciseModel("Bench Press - Barbell", "Chest", "Free weight", 1, null),
                new ExerciseModel("Lat Pulldown - Wide Grip", "Back", "Cable", 1, null),
                new ExerciseModel("Squat - Barbell", "Legs", "Free weight", 1, null),
                new ExerciseModel("Overhead Press - Barbell", "Shoulders", "Free weight", 1, null),
                new ExerciseModel("Sit Up", "Core", "Body weight", 1, null),
                new ExerciseModel("Deadlift - Barbell", "Back", "Free weight", 1, null),
                new ExerciseModel("Leg Press", "Legs", "Machine", 1, null)
        };
    }

}
