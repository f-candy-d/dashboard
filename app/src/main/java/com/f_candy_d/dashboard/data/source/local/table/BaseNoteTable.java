package com.f_candy_d.dashboard.data.source.local.table;

import com.f_candy_d.sqliteutils.BaseTable;
import com.f_candy_d.sqliteutils.ColumnDataType;
import com.f_candy_d.sqliteutils.TableUtils;

/**
 * Created by daichi on 10/8/17.
 */

abstract class BaseNoteTable extends BaseTable {

    /**
     * COLUMNS
     * ----------------------------------------------------------------------------- */

    public static final String _PARENT_BOARD_ID = "parent_board_id";

    /**
     * TABLE DEFINITION
     * ----------------------------------------------------------------------------- */

    static TableUtils.TableSource getBaseNoteTableSource(String table) {
        return new TableUtils.TableSource(table)
                .put(_PARENT_BOARD_ID, ColumnDataType.INTEGER);
    }
}
