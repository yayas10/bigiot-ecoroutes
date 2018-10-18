package com.hacktown.bigiot.ecorouting.presentation.presenter.impl;

import com.google.android.gms.maps.model.LatLng;
import com.hacktown.bigiot.ecorouting.domain.executor.Executor;
import com.hacktown.bigiot.ecorouting.domain.executor.MainThread;
import com.hacktown.bigiot.ecorouting.domain.interactor.QueryRouteInteractor;
import com.hacktown.bigiot.ecorouting.domain.interactor.impl.QueryRouteInteractorImpl;
import com.hacktown.bigiot.ecorouting.domain.repository.RouteRepository;
import com.hacktown.bigiot.ecorouting.presentation.presenter.QueryRoutePresenter;
import com.hacktown.bigiot.ecorouting.presentation.presenter.base.AbstractPresenter;
import com.hacktown.bigiot.ecorouting.storage.model.RouteData;
import java.util.List;

public class QueryRoutePresenterImpl extends AbstractPresenter implements QueryRoutePresenter,
    QueryRouteInteractor.Callback {

    private View view;

    private RouteRepository routeRepository;


    public QueryRoutePresenterImpl(Executor executor,
        MainThread mainThread,
        View view, RouteRepository routeRepository) {
        super(executor, mainThread);
        this.view = view;
        this.routeRepository = routeRepository;
    }

    @Override
    public void getRoute(LatLng from, LatLng to) {
        QueryRouteInteractor interactor = new QueryRouteInteractorImpl(
            this.mExecutor,
            this.mMainThread,
            this,
            routeRepository
        );

        interactor.execute(from, to);
    }


    @Override
    public void onRouteRetrieved(List<RouteData> routeList) {
        view.onRouteRetrieved(routeList);
    }

    @Override
    public void onRouteError(Integer errorCode) {
        view.onRouteError(errorCode);
    }
}
