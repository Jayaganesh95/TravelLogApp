package com.google.travelLog.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.travelLog.data.model.place.PlaceGetResponse;
import com.google.travellog.R;

import java.util.ArrayList;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {


    List<PlaceGetResponse> placeResponseList = new ArrayList<>();

    public void insertData(List<PlaceGetResponse> placeList) {
        this.placeResponseList.addAll(placeList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_logs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setUiElements(holder, placeResponseList.get(position));
    }

    @Override
    public int getItemCount() {
        return getItemSize();
    }


    private int getItemSize() {
        return placeResponseList.size();
    }

    private void setUiElements(ViewHolder holder, PlaceGetResponse placeGetResponse) {
        holder.name.setText(placeGetResponse.name);
        holder.latitude.setText(placeGetResponse.lati);
        holder.longitude.setText(placeGetResponse.longi);
        holder.category.setText(placeGetResponse.categoryId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, latitude, longitude, category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            latitude = itemView.findViewById(R.id.tv_latitude);
            longitude = itemView.findViewById(R.id.tv_longitude);
            category = itemView.findViewById(R.id.tv_category);

        }
    }
}
