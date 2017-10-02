package com.f_candy_d.dashboard.data_store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.f_candy_d.infra.sqlite.SqliteTableUtils;

/**
 * Created by daichi on 17/08/30.
 */

public class SqliteOpenHelper extends SQLiteOpenHelper {

    public SqliteOpenHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SqliteTableUtils.TableSource[] tableSources = DbContract.getTableSources();
        for (SqliteTableUtils.TableSource source : tableSources) {
            SqliteTableUtils.createTable(sqLiteDatabase, source);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SqliteTableUtils.resetTable(sqLiteDatabase, DbContract.getTableSources());
    }
}
