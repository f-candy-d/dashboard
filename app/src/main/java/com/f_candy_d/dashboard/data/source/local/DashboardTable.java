package com.f_candy_d.dashboard.data.source.local;

import com.f_candy_d.dashboard.data.source.local.sqlite_utils.SqliteBaseTable;
import com.f_candy_d.dashboard.data.source.local.sqlite_utils.SqliteColumnDataType;
import com.f_candy_d.dashboard.data.source.local.sqlite_utils.SqliteTableUtils;

/**
 * Created by daichi on 10/1/17.
 */

final class DashboardTable extends SqliteBaseTable {

    private DashboardTable() {}

    static final String TABLE_NAME = "dashboard";

    /**
     * COLUMNS
     * ----------------------------------------------------------------------------- */

    static final String _TITLE = "title";
    static final String _IS_ARCHIVED = "is_archived";
    static final String _THEME_COLOR = "theme_color";

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    static SqliteTableUtils.TableSource getTableSource() {
        return getBaseTableSource(TABLE_NAME)
                .put(_TITLE, SqliteColumnDataType.TEXT)
                .put(_IS_ARCHIVED, SqliteColumnDataType.INTEGER)
                .put(_THEME_COLOR, SqliteColumnDataType.INTEGER);
    }
}
