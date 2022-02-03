package com.android.biglifts.utility;

import com.android.biglifts.models.ExerciseModel;

public class ExercisesData {

    public static ExerciseModel[] populateExercisesData() {
        return new ExerciseModel[] {
                new ExerciseModel("Bench Press - Barbell", "Chest", "Free weight", null),
                new ExerciseModel("Lat Pulldown - Wide Grip", "Back", "Cable", null),
                new ExerciseModel("Squat - Barbell", "Legs", "Free weight", null),
                new ExerciseModel("Overhead Press - Barbell", "Shoulders", "Free weight", null),
                new ExerciseModel("Sit Up", "Core", "Body weight", null),
                new ExerciseModel("Deadlift - Barbell", "Back", "Free weight", null),
                new ExerciseModel("Leg Press", "Legs", "Machine", null)
        };
    }

}
