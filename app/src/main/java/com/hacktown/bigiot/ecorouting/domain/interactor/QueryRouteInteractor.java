package com.hacktown.bigiot.ecorouting.domain.interactor;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.domain.interactor.base.Interactor;
import com.hacktown.bigiot.ecorouting.storage.model.RouteData;
import java.util.List;

public interface QueryRouteInteractor extends Interactor {

    void execute(LatLng from, LatLng to);

    interface Callback {
        void onRouteRetrieved(List<RouteData> routeList);
        void onRouteError(Integer errorCode);
    }

}
