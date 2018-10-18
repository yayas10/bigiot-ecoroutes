package com.hacktown.bigiot.ecorouting.network.service;

import com.hacktown.bigiot.ecorouting.network.dto.response.DirectionsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleService {

    @GET("json")
    Call<DirectionsResponse> getRouting(
        @Query("origin") String origin,
        @Query("destination") String destination,
        @Query("mode") String mode,
        @Query("key") String apiKey
        );

}
