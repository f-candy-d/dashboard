package com.f_candy_d.dashboard.presentation.contract;

/**
 * Created by daichi on 10/4/17.
 */

interface BasePresenter<T extends BaseView> {
    void onStart(T view);
}
