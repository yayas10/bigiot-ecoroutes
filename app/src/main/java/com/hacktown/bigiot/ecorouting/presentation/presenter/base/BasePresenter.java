package com.hacktown.bigiot.ecorouting.presentation.presenter.base;

public interface BasePresenter {

    /**
     * Method that should signal the appropriate view to show the appropriate error with
     * the provided message.
     */
    void onError(Integer errorCode);
}