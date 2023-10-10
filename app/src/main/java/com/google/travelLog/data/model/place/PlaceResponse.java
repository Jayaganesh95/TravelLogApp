package com.google.travelLog.data.model.place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceResponse {

    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("error")
    @Expose
    public Object error;

    @SerializedName("data")
    @Expose
    public ArrayList<PlaceGetResponse> placeResponseList;


}
