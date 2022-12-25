package com.google.travelLog.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.travelLog.data.adapters.TimelineAdapter;
import com.google.travelLog.data.model.place.PlaceGetResponse;
import com.google.travelLog.data.model.place.PlaceResponse;
import com.google.travelLog.util.api.ApiClient;
import com.google.travelLog.util.api.ApiInterface;
import com.google.travellog.R;
import com.google.travellog.databinding.FragmentTimelineBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineFragment extends Fragment {

    FragmentTimelineBinding binding;
    private static final String TAG = "TimelineFragment";
    ArrayList<PlaceGetResponse> placeGetResponses;
    TimelineAdapter timelineAdapter;

    public TimelineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTimelineBinding.inflate(inflater, container, false);
        binding.recyclerViewTimeLine.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        timelineAdapter = new TimelineAdapter();
        binding.recyclerViewTimeLine.setLayoutManager(layoutManager);
        binding.recyclerViewTimeLine.setAdapter(timelineAdapter);

        Toolbar toolbar = binding.toolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPlaces();
    }

    private void getPlaces() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<PlaceResponse> call = apiInterface.getPlaceResponse();
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                timelineAdapter.insertData(response.body().placeResponseList);
                timelineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}