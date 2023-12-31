package com.google.travelLog.util.api;

import com.google.travelLog.data.model.category.CategoryGetResponse;
import com.google.travelLog.data.model.place.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("")
    Call<CategoryGetResponse> getCategoryResponse();

    @FormUrlEncoded
    @POST("")
    Call<PlaceResponse> postPlaceResponse(
            @Field("name") String placeName,
            @Field("lati") Double lati,
            @Field("longi") Double longi,
            @Field("category_id") String categoryId);

    @GET("")
    Call<PlaceResponse> getPlaceResponse();
}
