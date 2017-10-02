package com.f_candy_d.dashboard;

import android.app.Application;
import android.content.Context;

import com.f_candy_d.dashboard.data_store.SqliteOpenHelper;
import com.f_candy_d.infra.Repository;

/**
 * Created by daichi on 9/30/17.
 */

public class MyApp extends Application {

    private static MyApp INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        // Initialization
        Repository.initializeSliteRepository(new SqliteOpenHelper(this));
        com.f_candy_d.dashboard.data.source.Repository.initializeRepository(this);
    }

    public static Context getAppContext() {
        return INSTANCE.getApplicationContext();
    }
}
