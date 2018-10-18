package com.hacktown.bigiot.ecorouting.network.datasource;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.network.dto.response.RouteQueryResponse;
import java.util.List;

public interface RouteAPI {

    RouteQueryResponse getRoute(LatLng from, LatLng to);

}
