package com.android.biglifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.biglifts.adapters.SpecificExerciseFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class SpecificExerciseActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = "SpecificExerciseActivity";
    public static final String EXTRA_EXERCISE_NAME = "com.android.biglifts.EXTRA_EXERCISE_NAME";
    public static final String EXTRA_EXERCISE_ID = "com.android.biglifts.EXTRA_EXERCISE_ID";

    // UI Components
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private SpecificExerciseFragmentAdapter mFragmentAdapter;
    private static long mExerciseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_exercise);

        Intent intent = getIntent();
        String exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME );
        mExerciseID = intent.getLongExtra(EXTRA_EXERCISE_ID, -1);

        initUI(exerciseName);
        setListeners();
        initTabLayout();
    }

    private void initUI(String exerciseName) {
        mTabLayout = findViewById(R.id.activity_specific_exercise_tbl);
        mViewPager = findViewById(R.id.activity_specific_exercise_vp);

        mToolbar = (Toolbar) findViewById(R.id.activity_specific_exercise_tb);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(exerciseName);
    }

    private void setListeners() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initTabLayout() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragmentAdapter = new SpecificExerciseFragmentAdapter(fragmentManager, getLifecycle());
        mViewPager.setAdapter(mFragmentAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("History"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Progress"));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position));
            }
        });
    }
}