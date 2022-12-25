package com.google.travelLog.data.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoriesDataResponse implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("log_id")
    @Expose
    public String logId;

    @SerializedName("parent")
    @Expose
    public String parent;

}
