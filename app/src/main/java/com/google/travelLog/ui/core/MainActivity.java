package com.google.travelLog.ui.core;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.google.travellog.R;
import com.google.travellog.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().
                        findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }

    @Override
    public void onBackPressed() {
        if (navController != null) {
            NavDestination navDestination = navController.getCurrentDestination();
            if (navDestination != null && navDestination.getId() == R.id.mapsFragment) {
                finish();
                return;
            }
        }
        super.onBackPressed();
    }
}