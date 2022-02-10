package com.android.biglifts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.biglifts.fragments.ExercisesFragment;
import com.android.biglifts.fragments.HistoryFragment;
import com.android.biglifts.fragments.SummaryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements
        View.OnClickListener,
        NavigationBarView.OnItemSelectedListener {

    // Constants
    private static final String TAG = "HomeActivity";
    
    // UI Components
    private BottomNavigationView mBottomNaviagationView;
    private FloatingActionButton mFloatingActionButton;

    // Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        setListeners();

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_fl_container, new SummaryFragment()).commit();
    }

    private void initUI() {
        mBottomNaviagationView = findViewById(R.id.activity_home_btmnav);
        mFloatingActionButton = findViewById(R.id.activity_home_fab);
    }

    private void setListeners() {
        mFloatingActionButton.setOnClickListener(this);
        mBottomNaviagationView.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_home_fab:
                Intent intent = new Intent(HomeActivity.this, StartNewWorkoutActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.summaryMenuIcon:
                fragment = new SummaryFragment();
                break;
            case R.id.historyMenuIcon:
                fragment = new HistoryFragment();
                break;
            case R.id.exercisesMenuIcon:
                fragment = new ExercisesFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_fl_container, fragment).commit();
        return false;
    }
}