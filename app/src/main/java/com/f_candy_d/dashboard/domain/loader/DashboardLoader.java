package com.f_candy_d.dashboard.domain.loader;

import android.support.annotation.Nullable;

import com.f_candy_d.dashboard.data_store.DashboardTable;
import com.f_candy_d.dashboard.domain.structure.Dashboard;
import com.f_candy_d.infra.Repository;
import com.f_candy_d.infra.sql_utils.SqlEntity;
import com.f_candy_d.infra.sql_utils.SqlQuery;
import com.f_candy_d.infra.sql_utils.SqlWhere;
import com.f_candy_d.infra.sqlite.SqliteEntityLoader;

/**
 * Created by daichi on 9/30/17.
 */

public class DashboardLoader extends SqliteEntityLoader<Dashboard> {

    public DashboardLoader() {
        super(Dashboard.class);
    }

    @Nullable
    @Override
    protected Dashboard onLoad(long id) {
        return Dashboard.createIfPossible(id);
    }

    @Override
    protected Dashboard[] onLoadIf(SqlWhere where) {
        SqlQuery query = new SqlQuery();
        query.setSelection(where);
        query.putTables(DashboardTable.TABLE_NAME);
//        SqlEntity[] results = Repository.getSqlite().select(query)

//        Dashboard[] dashboards = new Dashboard[results.length];
//        for (int i = 0; i < results.length; ++i) {
//            results[i].setTableName(DashboardTable.TABLE_NAME);
//            dashboards[i] = new Dashboard(results[i]);
//        }

//        return dashboards;
        return null;
    }
}
