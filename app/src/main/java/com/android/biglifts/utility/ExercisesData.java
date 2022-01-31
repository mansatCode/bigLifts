package com.android.biglifts.utility;

import com.android.biglifts.models.ExerciseModel;

public class ExercisesData {

    public static ExerciseModel[] populateExercisesData() {
        return new ExerciseModel[] {
                new ExerciseModel("Bench Press - Barbell", "Chest", "Free weight"),
                new ExerciseModel("Lat Pulldown - Wide Grip", "Back", "Cable"),
                new ExerciseModel("Squat - Barbell", "Legs", "Free weight"),
                new ExerciseModel("Overhead Press - Barbell", "Shoulders", "Free weight"),
                new ExerciseModel("Sit Up", "Core", "Body weight"),
                new ExerciseModel("Deadlift - Barbell", "Back", "Free weight"),
                new ExerciseModel("Leg Press", "Legs", "Machine")
        };
    }

}
