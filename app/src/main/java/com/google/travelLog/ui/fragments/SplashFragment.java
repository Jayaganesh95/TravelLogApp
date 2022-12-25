package com.google.travelLog.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.travellog.databinding.FragmentSplashBinding;


public class SplashFragment extends Fragment {

    FragmentSplashBinding binding;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> {
            NavController navController = Navigation.findNavController(view);
            NavDirections action = SplashFragmentDirections.actionSplashFragmentToMapsFragment();
            navController.navigate(action);
        };
        long splashTimeout = 1500;
        handler.postDelayed(runnable, splashTimeout);

    }

}
