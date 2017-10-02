package com.f_candy_d.sqliteutils;

import android.provider.BaseColumns;

/**
 * Created by daichi on 9/30/17.
 */

abstract public class BaseTable implements BaseColumns {

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    protected static TableUtils.TableSource getBaseTableSource(String table) {
        return new TableUtils.TableSource(table)
                .put(BaseColumns._ID, ColumnDataType.INTEGER_PK);
    }
}
