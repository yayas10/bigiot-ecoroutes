package com.hacktown.bigiot.ecorouting.network.datasource;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public interface GoogleDirectionsAPI {

    List<LatLng> getRouting(LatLng from, LatLng to, String mode);
}
