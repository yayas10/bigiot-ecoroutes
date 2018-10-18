package com.hacktown.bigiot.ecorouting.domain.interactor.base;

public interface Interactor {

    /**
     * This is the main method that starts an interactor.
     * It will make sure that the interactor operation is done in a background thread.
     */
    void execute();
}
