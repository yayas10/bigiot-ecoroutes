package com.hacktown.bigiot.ecorouting.presentation.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.presentation.ui.BaseView;
import com.hacktown.bigiot.ecorouting.storage.model.RouteData;
import java.util.List;

public interface QueryRoutePresenter {

    void getRoute(LatLng from, LatLng to);

    interface View extends BaseView {
        void onRouteRetrieved(List<RouteData> routeList);
        void onRouteError(Integer errorCode);
    }
}
