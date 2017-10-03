package com.f_candy_d.dashboard.data.source.local;

import com.f_candy_d.sqliteutils.BaseTable;
import com.f_candy_d.sqliteutils.ColumnDataType;
import com.f_candy_d.sqliteutils.TableUtils;

/**
 * Created by daichi on 10/1/17.
 */

public final class DashboardTable extends BaseTable {

    private DashboardTable() {}

    public static final String TABLE_NAME = "dashboard";

    /**
     * COLUMNS
     * ----------------------------------------------------------------------------- */

    public static final String _TITLE = "title";
    public static final String _IS_ARCHIVED = "is_archived";
    public static final String _THEME_COLOR = "theme_color";

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    static TableUtils.TableSource getTableSource() {
        return getBaseTableSource(TABLE_NAME)
                .put(_TITLE, ColumnDataType.TEXT)
                .put(_IS_ARCHIVED, ColumnDataType.INTEGER)
                .put(_THEME_COLOR, ColumnDataType.INTEGER);
    }
}
