package com.f_candy_d.dashboard.data_store;

import com.f_candy_d.infra.sqlite.SqliteBaseTable;
import com.f_candy_d.infra.sqlite.SqliteColumnDataType;
import com.f_candy_d.infra.sqlite.SqliteTableUtils;

/**
 * Created by daichi on 9/30/17.
 */

public class TextNoteTable extends SqliteBaseTable {

    private TextNoteTable() {}

    public static final String TABLE_NAME = "text_note";

    /**
     * COLUMNS
     * ----------------------------------------------------------------------------- */

    public static final String _PARENT_DASHBOARD_ID = "dashboard_id";
    public static final String _TITLE = "title";
    public static final String _BODY = "body";

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    static SqliteTableUtils.TableSource getTableSource() {
        return getBaseTableSource(TABLE_NAME)
                .put(_PARENT_DASHBOARD_ID, SqliteColumnDataType.INTEGER)
                .put(_TITLE, SqliteColumnDataType.TEXT)
                .put(_BODY, SqliteColumnDataType.TEXT);
    }

    /**
     * DEFAULT VALUES
     * ----------------------------------------------------------------------------- */

    public static long defaultParentDashboardId() {
        return DashboardTable.defaultId();
    }

    public static String defaultTitle() {
        return null;
    }

    public static String defaultBody() {
        return null;
    }
}
