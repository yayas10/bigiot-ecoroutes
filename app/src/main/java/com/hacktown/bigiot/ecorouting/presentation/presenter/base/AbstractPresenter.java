package com.hacktown.bigiot.ecorouting.presentation.presenter.base;


import com.hacktown.bigiot.ecorouting.domain.executor.Executor;
import com.hacktown.bigiot.ecorouting.domain.executor.MainThread;

public abstract class AbstractPresenter {
    protected Executor mExecutor;
    protected MainThread mMainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}
