package com.f_candy_d.dashboard.data_store;

import android.support.annotation.NonNull;

import com.f_candy_d.infra.sqlite.SqliteTableUtils;

/**
 * Created by daichi on 17/08/30.
 */

final public class DbContract {

    public static final String DATABASE_NAME = "dashboard_contents_database";
    public static final int DATABASE_VERSION = 1;

    @NonNull
    public static SqliteTableUtils.TableSource[] getTableSources() {
        return new SqliteTableUtils.TableSource[] {
                DashboardTable.getTableSource(),
                TextNoteTable.getTableSource()
        };
    }
}
