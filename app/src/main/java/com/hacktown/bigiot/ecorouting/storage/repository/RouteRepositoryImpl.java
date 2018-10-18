package com.hacktown.bigiot.ecorouting.storage.repository;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.domain.repository.RouteRepository;
import com.hacktown.bigiot.ecorouting.network.datasource.GoogleDirectionsAPI;
import com.hacktown.bigiot.ecorouting.network.datasource.RouteAPI;
import com.hacktown.bigiot.ecorouting.network.datasource.impl.GoogleDirectionsAPIImpl;
import com.hacktown.bigiot.ecorouting.network.datasource.impl.RouteAPIImpl;
import com.hacktown.bigiot.ecorouting.network.dto.response.PointResponse;
import com.hacktown.bigiot.ecorouting.network.dto.response.RouteQueryResponse;
import com.hacktown.bigiot.ecorouting.storage.model.RouteData;
import java.util.ArrayList;
import java.util.List;

public class RouteRepositoryImpl implements RouteRepository {

    @Override
    public void getOptimalEcoRoute(LatLng from, LatLng to, Callback callback) {
        RouteAPI routeAPI = new RouteAPIImpl();
        RouteQueryResponse route = routeAPI.getRoute(from, to);
        route.setInitPoint(new PointResponse(from.latitude, from.longitude));
        route.setEndPoint(new PointResponse(to.latitude, to.longitude));


        List<RouteData> routeList = new ArrayList<>();

        GoogleDirectionsAPI googleDirectionsAPI = new GoogleDirectionsAPIImpl();

        routeList.add(new RouteData(
            "walking",
            googleDirectionsAPI.getRouting(
                new LatLng(route.getInitPoint().getLatitude(), route.getInitPoint().getLongitude()),
                new LatLng(route.getIniBikeStation().getLatitude(), route.getIniBikeStation().getLongitude()),
                "walking")
        ));

        routeList.add(new RouteData(
            "cycling",
            googleDirectionsAPI.getRouting(
                new LatLng(route.getIniBikeStation().getLatitude(), route.getIniBikeStation().getLongitude()),
                new LatLng(route.getEndBikeStation().getLatitude(), route.getEndBikeStation().getLongitude()),
                "walking")
        ));

        routeList.add(new RouteData(
            "walking",
            googleDirectionsAPI.getRouting(
                new LatLng(route.getEndBikeStation().getLatitude(), route.getEndBikeStation().getLongitude()),
                new LatLng(route.getEndPoint().getLatitude(), route.getEndPoint().getLongitude()),
                "walking")
        ));

        callback.onRouteRetrieved(routeList);
    }
}
