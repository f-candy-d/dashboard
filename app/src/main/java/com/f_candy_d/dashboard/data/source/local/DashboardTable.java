package com.f_candy_d.dashboard.data.source.local;

import com.f_candy_d.infra.sqlite.SqliteBaseTable;
import com.f_candy_d.infra.sqlite.SqliteColumnDataType;
import com.f_candy_d.infra.sqlite.SqliteTableUtils;

/**
 * Created by daichi on 10/1/17.
 */

public final class DashboardTable extends SqliteBaseTable {

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

    static SqliteTableUtils.TableSource getTableSource() {
        return getBaseTableSource(TABLE_NAME)
                .put(_TITLE, SqliteColumnDataType.TEXT)
                .put(_IS_ARCHIVED, SqliteColumnDataType.INTEGER)
                .put(_THEME_COLOR, SqliteColumnDataType.INTEGER);
    }
}
