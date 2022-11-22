package com.google.travelLog.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.travellog.databinding.FragmentDetailBinding;


public class DetailFragment extends Fragment {
    FragmentDetailBinding fragmentDetailBinding;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false);
        return fragmentDetailBinding.getRoot();
    }
}