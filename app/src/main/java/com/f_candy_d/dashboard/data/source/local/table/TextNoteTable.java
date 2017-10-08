package com.f_candy_d.dashboard.data.source.local.table;

import com.f_candy_d.sqliteutils.ColumnDataType;
import com.f_candy_d.sqliteutils.TableUtils;

/**
 * Created by daichi on 10/8/17.
 */

public final class TextNoteTable extends BaseNoteTable {

    private TextNoteTable() {}

    public static final String TABLE_NAME = "text_note";

    /**
     * COLUMNS
     * ----------------------------------------------------------------------------- */

    public static final String _TITLE = "title";
    public static final String _BODY = "body";

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    public static TableUtils.TableSource getTableSource() {
        return getBaseNoteTableSource(TABLE_NAME)
                .put(_TITLE, ColumnDataType.TEXT)
                .put(_BODY, ColumnDataType.TEXT);
    }
}
