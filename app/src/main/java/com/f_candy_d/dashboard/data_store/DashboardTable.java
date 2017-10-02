package com.f_candy_d.dashboard.data_store;

import android.graphics.Color;

import com.f_candy_d.infra.sqlite.SqliteBaseTable;
import com.f_candy_d.infra.sqlite.SqliteColumnDataType;
import com.f_candy_d.infra.sqlite.SqliteTableUtils;

/**
 * Created by daichi on 9/30/17.
 */

public class DashboardTable extends SqliteBaseTable {

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

    /**
     * DEFAULT COLUMN VALUES
     * ----------------------------------------------------------------------------- */

    public static String defaultTitle() {
        return null;
    }

    public static boolean defaultIsArchived() {
        return false;
    }

    public static int defaultThemeColor() {
        // @color/task_theme_color_indigo
        return Color.parseColor("#3F51B5");
    }
}
