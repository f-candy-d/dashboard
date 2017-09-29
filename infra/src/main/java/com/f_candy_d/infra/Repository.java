package com.f_candy_d.infra;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daichi on 9/10/17.
 */

final public class Repository {

    public static final long SQLITE_INVALID_ID = SqliteRepository.INVALID_ID;
    public static final int SQLITE_BOOL_FALSE = SqliteRepository.BOOL_FALSE;
    public static final int SQLITE_BOOL_TRUE = SqliteRepository.BOOL_TRUE;

    private static SqliteRepository mSqliteRepository = null;

    public static void initializeSliteRepository(SQLiteOpenHelper openHelper) {
        mSqliteRepository = new SqliteRepository(openHelper);
    }

    public static SqliteRepository getSqlite() {
        if (mSqliteRepository == null) {
            throw new IllegalStateException(
                    "Initialize SqliteRepository using Repository#initializeSliteRepository(SQLiteOpenHelper)");
        }

        return mSqliteRepository;
    }
}
