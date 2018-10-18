package com.hacktown.bigiot.ecorouting.network.datasource.impl;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.network.datasource.RouteAPI;
import com.hacktown.bigiot.ecorouting.network.dto.request.PointRequest;
import com.hacktown.bigiot.ecorouting.network.dto.request.RouteQueryModelRequest;
import com.hacktown.bigiot.ecorouting.network.dto.response.RouteQueryModelResponse;
import com.hacktown.bigiot.ecorouting.network.dto.response.RouteQueryResponse;
import com.hacktown.bigiot.ecorouting.network.interceptor.NonSecurityInterceptor;
import com.hacktown.bigiot.ecorouting.network.service.RouteService;
import com.hacktown.bigiot.ecorouting.network.util.RetrofitClient;
import com.hacktown.bigiot.ecorouting.network.util.Servers;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class RouteAPIImpl implements RouteAPI {

    private RouteService routeService;

    public RouteAPIImpl() {
        this.routeService = RetrofitClient.getRoutingService(Servers.BASE_URL_ENDPOINT, new NonSecurityInterceptor());
    }

    @Override
    public RouteQueryResponse getRoute(LatLng from, LatLng to) {
        RouteQueryModelRequest request = new RouteQueryModelRequest();
        //SET parameters
        PointRequest iniPoint = new PointRequest();
        iniPoint.setLatitude(from.latitude);
        iniPoint.setLongitude(from.longitude);
        request.setIniPoint(iniPoint);

        PointRequest endPoint = new PointRequest();
        endPoint.setLatitude(to.latitude);
        endPoint.setLongitude(to.longitude);
        request.setEndPoint(endPoint);

        Call<RouteQueryModelResponse> call = routeService.getRoute(request);
        try {
            Response<RouteQueryModelResponse> response = call.execute();
            if(response == null){
                return null;
            }
            if(response.errorBody() != null) {
                return null;
            }
            if(response.body() == null){
                return null;
            }
            System.out.print(response.raw().toString());
            return response.body().getDataResponse();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
