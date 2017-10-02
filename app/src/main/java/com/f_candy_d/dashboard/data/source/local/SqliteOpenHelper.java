package com.f_candy_d.dashboard.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.f_candy_d.dashboard.data.source.local.sqlite_utils.SqliteTableUtils;

/**
 * Created by daichi on 10/2/17.
 */

class SqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dashboard_contents_db";
    private static final int DATABASE_VERSION = 1;

    public SqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SqliteTableUtils.createTable(sqLiteDatabase, DashboardTable.getTableSource());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SqliteTableUtils.resetTable(sqLiteDatabase, DashboardTable.getTableSource());
    }
}
