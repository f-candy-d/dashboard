package com.f_candy_d.infra.sqlite;

import android.provider.BaseColumns;

/**
 * Created by daichi on 9/30/17.
 */

abstract public class SqliteBaseTable implements BaseColumns {

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    protected static SqliteTableUtils.TableSource getBaseTableSource(String table) {
        return new SqliteTableUtils.TableSource(table)
                .put(BaseColumns._ID, SqliteColumnDataType.INTEGER_PK);
    }
}
