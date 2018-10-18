package com.hacktown.bigiot.ecorouting.network.service;

import com.hacktown.bigiot.ecorouting.network.dto.request.RouteQueryModelRequest;
import com.hacktown.bigiot.ecorouting.network.dto.response.RouteQueryModelResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RouteService {

    @POST("routes")
    Call<RouteQueryModelResponse> getRoute(@Body RouteQueryModelRequest request);

}
