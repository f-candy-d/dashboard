package com.f_candy_d.infra.sqlite;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.f_candy_d.infra.Repository;
import com.f_candy_d.infra.SqliteRepository;
import com.f_candy_d.infra.sql_utils.SqlEntity;

/**
 * Created by daichi on 9/30/17.
 */

abstract public class SqliteStreamEntity {

    private static final long INVALID_ID = SqliteRepository.INVALID_ID;
    private static final String ID_COLUMN_NAME = BaseColumns._ID;

    @NonNull private final String mTableName;
    private long mId;

    public SqliteStreamEntity(long id, @NonNull String tableName) {
        mId = id;
        mTableName = tableName;
        initializeWithDefaultColumnValues();
        fetch(id);
    }

    /**
     * Create a blank entity object
     */
    public SqliteStreamEntity(@NonNull String tableName) {
        mId = INVALID_ID;
        mTableName = tableName;
        initializeWithDefaultColumnValues();
    }

    public SqliteStreamEntity(SqlEntity sqlEntity) {
        if (sqlEntity == null) {
            throw new NullPointerException("'sqlEntity' is a null object");
        }

        if (!sqlEntity.hasColumn(ID_COLUMN_NAME)) {
            throw new IllegalStateException("sqlEntity does not have a column -> " + ID_COLUMN_NAME);
        }

        if (sqlEntity.getTableName() == null) {
            throw new NullPointerException("sqlEntity does not have a table name");
        }

        mId = sqlEntity.getLong(ID_COLUMN_NAME);
        mTableName = sqlEntity.getTableName();
        initializeWithDefaultColumnValues();
        constructFromSqlEntity(sqlEntity);
    }

    public final void clear() {
        mId = INVALID_ID;
        initializeWithDefaultColumnValues();
    }

    /**
     * Fetch entity data from the database
     */
    public final boolean fetch(long id) {

        SqlEntity sqlEntity = Repository.getSqlite().selectRowById(mTableName, id);
        if (sqlEntity == null) {
            return false;
        }

        mId = id;
        initializeWithDefaultColumnValues();
        constructFromSqlEntity(sqlEntity);
        return true;
    }

    /**
     * Save data to the database
     */
    public final boolean commit() {
        SqlEntity sqlEntity = toSqlEntity();
        if (sqlEntity == null) {
            return false;
        }
        sqlEntity.put(ID_COLUMN_NAME, mId);

        if (mId == INVALID_ID ||
                Repository.getSqlite().selectRowById(mTableName, mId) == null) {
            // Insert
            sqlEntity.remove(ID_COLUMN_NAME);
            mId = Repository.getSqlite().insert(sqlEntity);
            return (mId != SqliteRepository.INVALID_ID);

        } else {
            // Update
            return Repository.getSqlite().update(sqlEntity);
        }
    }

    public final boolean delete() {
        if (mId == INVALID_ID) {
            return false;
        }

        boolean result = Repository.getSqlite().delete(mId, mTableName);
        if (result) {
            clear();
        }

        return result;
    }

    public final long getId() {
        return mId;
    }

    abstract protected SqlEntity toSqlEntity();
    abstract protected void constructFromSqlEntity(@NonNull SqlEntity entity);
    abstract public void initializeWithDefaultColumnValues();
    abstract public boolean isDefaultState();
    abstract @Override public boolean equals(Object obj);
    abstract @Override public int hashCode();
}
