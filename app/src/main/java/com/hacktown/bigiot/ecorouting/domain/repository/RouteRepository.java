package com.hacktown.bigiot.ecorouting.domain.repository;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.storage.model.RouteData;
import java.util.List;

public interface RouteRepository {

    void getOptimalEcoRoute(LatLng from, LatLng to, Callback callback);

    interface Callback {
        void onRouteRetrieved(List<RouteData> routeList);
        void onRouteError(Integer errorCode);
    }

}
