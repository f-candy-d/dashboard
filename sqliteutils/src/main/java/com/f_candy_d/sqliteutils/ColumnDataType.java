package com.f_candy_d.sqliteutils;

/**
 * Created by daichi on 17/08/30.
 */

public enum ColumnDataType {

    INTEGER("INTEGER"),
    INTEGER_PK("INTEGER PRIMARY KEY"),
    TEXT("TEXT"),
    REAL("REAL"),
    BLOB("BLOB");

    private final String mString;

    ColumnDataType(String string) {
        mString = string;
    }

    @Override
    public String toString() {
        return mString;
    }
}
