package com.f_candy_d.dashboard;

import android.app.Application;

import com.f_candy_d.dashboard.data_store.SqliteOpenHelper;
import com.f_candy_d.infra.Repository;

/**
 * Created by daichi on 9/30/17.
 */

public class AppInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Repository.initializeSliteRepository(new SqliteOpenHelper(this));
    }
}
