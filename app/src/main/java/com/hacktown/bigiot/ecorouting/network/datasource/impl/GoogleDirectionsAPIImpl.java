package com.hacktown.bigiot.ecorouting.network.datasource.impl;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.network.datasource.GoogleDirectionsAPI;
import com.hacktown.bigiot.ecorouting.network.dto.response.DirectionsResponse;
import com.hacktown.bigiot.ecorouting.network.dto.response.StepResponse;
import com.hacktown.bigiot.ecorouting.network.interceptor.NonSecurityInterceptor;
import com.hacktown.bigiot.ecorouting.network.service.GoogleService;
import com.hacktown.bigiot.ecorouting.network.util.RetrofitClient;
import com.hacktown.bigiot.ecorouting.network.util.Servers;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class GoogleDirectionsAPIImpl implements GoogleDirectionsAPI {

    private GoogleService googleService;

    public GoogleDirectionsAPIImpl() {
        this.googleService = RetrofitClient.getGoogleService(Servers.BASE_URL_GOOGLE_LOCATION_API, new NonSecurityInterceptor());
    }

    @Override
    public List<LatLng> getRouting(LatLng from, LatLng to, String mode) {
        try {
            //SET parameters
            Call<DirectionsResponse> call = googleService.getRouting(
                parseLocationToString(from),
                parseLocationToString(to),
                mode,
                Servers.GOOGLE_API_KEY
            );
            Response<DirectionsResponse> response = call.execute();
            if(response == null){
                return null;
            }
            if(response.errorBody() != null) {
                return null;
            }
            if(response.body() == null){
                return null;
            }
            return parseDirections(response.body().getRouteList().get(0).getLegList().get(0).getStepList());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseLocationToString(LatLng location){
        return String.valueOf(location.latitude) + "," + String.valueOf(location.longitude);
    }

    private List<LatLng> parseDirections(List<StepResponse> stepList){
        List<LatLng> list = new ArrayList<>();
        for (StepResponse step :
            stepList) {
            list.add(new LatLng(step.getStartLocation().getLatitude(), step.getStartLocation().getLongitude()));
        }
        list.add(new LatLng(stepList.get(stepList.size()-1).getEndLocation().getLatitude(), stepList.get(stepList.size()-1).getEndLocation().getLongitude()));
        return list;
    }
}
