package com.f_candy_d.dashboard.presentation.contract;

import android.os.Bundle;

/**
 * Created by daichi on 10/4/17.
 */

public interface EditDashboardContract {

    interface View extends BaseView {
        void showSaveSuccessfullyMessage();
        void showSaveFailedMessage();
        void showDashboardTitle(String title);
        void showDashboardThemeColor(int themeColor);
        void onCloseUi(Bundle resultData, boolean wasSuccessful);
    }

    interface Presenter extends BasePresenter<EditDashboardContract.View> {
        void onInputDashboardTitle(String title);
        void onInputDashboardThemeColor(int themeColor);
        String getDashboardTitle();
        int getDashboardThemeColor();
        void onSave();
    }
}
