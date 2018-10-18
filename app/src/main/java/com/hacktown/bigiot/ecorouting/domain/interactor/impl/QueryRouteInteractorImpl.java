package com.hacktown.bigiot.ecorouting.domain.interactor.impl;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.domain.executor.Executor;
import com.hacktown.bigiot.ecorouting.domain.executor.MainThread;
import com.hacktown.bigiot.ecorouting.domain.interactor.QueryRouteInteractor;
import com.hacktown.bigiot.ecorouting.domain.interactor.base.AbstractInteractor;
import com.hacktown.bigiot.ecorouting.domain.repository.RouteRepository;
import com.hacktown.bigiot.ecorouting.storage.model.RouteData;
import java.util.List;

public class QueryRouteInteractorImpl extends AbstractInteractor implements QueryRouteInteractor, RouteRepository.Callback {

    private Callback callback;

    private RouteRepository routeRepository;

    private LatLng from;
    private LatLng to;

    public QueryRouteInteractorImpl(
        Executor threadExecutor,
        MainThread mainThread,
        Callback callback,
        RouteRepository routeRepository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.routeRepository = routeRepository;
    }

    @Override
    public void execute(LatLng from, LatLng to) {
        this.from = from;
        this.to = to;

        this.mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        try {
            this.routeRepository.getOptimalEcoRoute(from, to, this);
        }catch (Exception e){
            notifyError(40010);
            e.printStackTrace();
        }
    }

    private void postCode(final List<RouteData> routeList) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onRouteRetrieved(routeList);
            }
        });
    }

    private void notifyError(final int errorCode) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onRouteError(errorCode);
            }
        });
    }

    @Override
    public void onRouteRetrieved(List<RouteData> routeList) {
        postCode(routeList);
    }

    @Override
    public void onRouteError(Integer errorCode) {
        notifyError(errorCode);
    }
}
