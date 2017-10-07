package com.f_candy_d.dashboard.presentation.contract;

import android.os.Bundle;

/**
 * Created by daichi on 10/4/17.
 */

public interface DashboardDetailsContract {

    interface View extends BaseView {
        void showSaveSuccessfullyMessage();
        void showSaveFailedMessage();
        void showTitle(String title);
        void showThemeColor(int themeColor);
        void showTitleEditor(String currentTitle);
        void showThemeColorEditor(int currentThemeColor);
        void onCloseUi(Bundle resultData, boolean wasSuccessful);
    }

    interface Presenter extends BasePresenter<DashboardDetailsContract.View> {
        void onInputTitle(String title);
        void onInputThemeColor(int themeColor);
        void onEditTitle();
        void onEditThemeColor();
        void onSave();
    }
}
