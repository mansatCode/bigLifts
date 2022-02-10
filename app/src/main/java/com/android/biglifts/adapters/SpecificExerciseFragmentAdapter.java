package com.android.biglifts.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.biglifts.fragments.ExerciseHistoryFragment;
import com.android.biglifts.fragments.ExerciseProgressFragment;

public class SpecificExerciseFragmentAdapter extends FragmentStateAdapter {

    public SpecificExerciseFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1:
                return new ExerciseProgressFragment();

        }
        return new ExerciseHistoryFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
