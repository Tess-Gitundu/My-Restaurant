package com.example.gitundu.network;

import com.example.gitundu.models.YelpBusinessesSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YelpApi {
    @GET("businesses/search")
        //endpoint that we want to hit
    Call<YelpBusinessesSearchResponse> getRestaurants(
            @Query("location") String location,
            @Query("term") String term
    );
}
