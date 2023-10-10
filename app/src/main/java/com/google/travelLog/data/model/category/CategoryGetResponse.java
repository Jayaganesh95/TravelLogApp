package com.google.travelLog.data.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryGetResponse implements Serializable {

    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("error")
    @Expose
    public Object error;

    @SerializedName("data")
    @Expose
    public ArrayList<CategoriesDataResponse> categoryResponsesList = null;

}
