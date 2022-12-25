package com.google.travelLog.data.model.place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceGetResponse implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("lati")
    @Expose
    public String lati;

    @SerializedName("longi")
    @Expose
    public String longi;

    @SerializedName("category_id")
    @Expose
    public String categoryId;

    @SerializedName("log_id")
    @Expose
    public String logId;


}
