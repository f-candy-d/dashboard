package com.f_candy_d.sqliteutils;

/**
 * Created by daichi on 10/3/17.
 */

public class DataConverter {

    private DataConverter() {}

    // This site (https://stackoverflow.com/questions/843780/store-boolean-value-in-sqlite) says
    // SQLite saves a boolean value as an integer value (true=1, false=0) into the database.
    public static final int SQLITE_BOOL_TRUE = 1;
    public static final int SQLITE_BOOL_FALSE = 0;

    public static boolean toJavaBool(int sqliteBool) {
        return (sqliteBool != SQLITE_BOOL_FALSE);
    }

    public static int toSqliteBool(boolean javaBool) {
        return (javaBool) ? SQLITE_BOOL_TRUE : SQLITE_BOOL_FALSE;
    }
}
