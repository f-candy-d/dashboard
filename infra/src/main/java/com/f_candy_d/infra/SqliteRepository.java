package com.f_candy_d.infra;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.infra.sql_utils.SqlCondExpr;
import com.f_candy_d.infra.sql_utils.SqlEntity;
import com.f_candy_d.infra.sql_utils.SqlWhere;
import com.f_candy_d.sqliteutils.QueryBuilder;

import java.util.ArrayList;

/**
 * Created by daichi on 17/08/30.
 */

public class SqliteRepository {

    /**
     * Constants
     */
    public static final long INVALID_ID = -1;
    // This site (https://stackoverflow.com/questions/843780/store-boolean-value-in-sqlite) says
    // SQLite saves a boolean value as an integer value (true=1, false=0) into the database.
    public static final int BOOL_FALSE = 0;
    public static final int BOOL_TRUE = 1;

    private SQLiteOpenHelper mOpenHelper;

    public SqliteRepository(@NonNull SQLiteOpenHelper openHelper) {
        mOpenHelper = openHelper;
    }

    /**
     * Insert a row into the database.
     * @param entity Initial column values for the row
     * @return generalInsert()
     */
    public long insert(@NonNull SqlEntity entity) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        long result = generalInsert(sqLiteDatabase, entity);
        sqLiteDatabase.close();

        return result;
    }

    /**
     * Update the row of a specific entity in the database.
     * @param entity New column values.
     *               This object must has a pair of the following column & value:
     *               COLUMN   : android.provider.BaseColumns._ID
     *               VALUE    : Row's ID of a specific entity
     *
     * @return True if updating is successful, or false if an error occur
     */
    public boolean update(@NonNull SqlEntity entity) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        boolean result = generalUpdate(sqLiteDatabase, entity);
        sqLiteDatabase.close();

        return result;
    }

    /**
     * Update rows which meet a passed sql WHERE clause in the database.
     * @param entity New column values
     * @param where Sql WHERE clause
     * @return SQLiteDatabase.update()
     */
    public int updateIf(@NonNull String table, @NonNull SqlEntity entity, @NonNull SqlWhere where) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        final int affected = sqLiteDatabase.update(table, entity.getValueMap(), where.formalize(), null);
        sqLiteDatabase.close();

        return affected;
    }

    /**
     * Delete the row of a specific entity in the database.
     * @param entity An entity which will be deleted.
     *               This object must has a pair of the following column & value:
     *               COLUMN  : android.provider.BaseColumns._ID
     *               VALUE   : Row's ID of a specific entity
     *
     * @return True if deleting is successful, false otherwise
     */
    public boolean delete(@NonNull SqlEntity entity) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        boolean result = generalDelete(sqLiteDatabase, entity);
        sqLiteDatabase.close();

        return result;
    }

    public boolean delete(long id, @NonNull String table) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        boolean result = generalDelete(sqLiteDatabase, id, table);
        sqLiteDatabase.close();

        return result;
    }

    /**
     * Delete rows which meet a passed sql WHERE clause in the database.
     * @param where Sql WHERE clause
     * @return SQLiteDatabase.delete()
     */
    public int deleteIf(@NonNull String table, @NonNull SqlWhere where) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        final int affected = sqLiteDatabase.delete(table, where.formalize(), null);
        sqLiteDatabase.close();

        return affected;
    }

    /**
     * Find rows for a passed query.
     * @param query Conditions for finding rows
     * @return An array which contains results of finding, or an empty array
     */
    @NonNull
    public SqlEntity[] select(@NonNull QueryBuilder queryBuilder) {
        ArrayList<SqlEntity> results = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.query(
//                query.distinct(),
//                query.tables(),
//                query.columns(),
//                query.selection(),
//                query.selectionArgs(),
//                query.groupBy(),
//                query.having(),
//                query.orderBy(),
//                query.limit());

        Cursor cursor = queryBuilder.query(sqLiteDatabase);

        boolean isEOF = cursor.moveToFirst();
        ContentValues contentValues;
        SqlEntity entity;
        while (isEOF) {
            contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            entity = new SqlEntity(contentValues);
            results.add(entity);
            isEOF = cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();

        return results.toArray(new SqlEntity[]{});
    }

    /**
     * Find the row for a passed ID.
     * @param table Target table name
     * @param id ID of a specific row
     * @return Null if a specific row is not found, or ID is not an unique value
     */
    @Nullable
    public SqlEntity selectRowById(@NonNull String table, long id) {
        if (id == INVALID_ID) {
            return null;
        }

        SqlCondExpr idIs = new SqlCondExpr(BaseColumns._ID).equalTo(id);
//        SqlQuery query = new SqlQuery();
//        query.putTables(table);
//        query.setSelection(idIs);

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.table(table).whereColumnEquals(BaseColumns._ID, id);

//        SqlEntity[] results = select(query);
        SqlEntity[] results = select(queryBuilder);
        if (results.length == 1) {
            return results[0];
        }

        return null;
    }

    /**
     * region; Transaction
     */

    public boolean doTransaction(Transaction transaction) {
        boolean isSuccessful = false;
        transaction.setDatabase(mOpenHelper.getReadableDatabase());
        transaction.getDatabase().beginTransaction();
        try {
             isSuccessful = transaction.onProcess();
            if (isSuccessful) {
                transaction.getDatabase().setTransactionSuccessful();
            }

        } finally {
            transaction.getDatabase().endTransaction();
            transaction.getDatabase().close();
            transaction.setDatabase(null);
        }

        return isSuccessful;
    }

    abstract static public class Transaction {

        private SQLiteDatabase mDatabase;

        // Return true if transaction is successful, false otherwise
        abstract protected boolean onProcess();

        final void setDatabase(SQLiteDatabase database) {
            mDatabase = database;
        }

        final SQLiteDatabase getDatabase() {
            return mDatabase;
        }

        final protected long insert(@NonNull SqlEntity entity) {
            return generalInsert(mDatabase, entity);
        }

        final protected boolean update(@NonNull SqlEntity entity) {
            return generalUpdate(mDatabase, entity);
        }

        final protected boolean delete(@NonNull SqlEntity entity) {
            return generalDelete(mDatabase, entity);
        }

        final protected boolean delete(long id, @NonNull String table) {
            return generalDelete(mDatabase, id, table);
        }
    }

    /**
     * region; General methods for inserting, deleting, updating a row.
     * The following methods will not close the database.
     */

    private static long generalInsert(SQLiteDatabase database, SqlEntity entity) {
        if (entity.getTableName() == null) {
            return INVALID_ID;
        }

        final long id = database.insert(entity.getTableName(), null, entity.getValueMap());

        // SQLiteDatabase#insert() method returns the row ID of the newly inserted row, or -1 if an error occurred.
        // See document -> https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String, java.lang.String, android.content.ContentValues)
        return (id != -1) ? id : INVALID_ID;
    }

    private static boolean generalDelete(SQLiteDatabase database, SqlEntity entity) {
        long id;
        if (entity.getTableName() == null ||
                !entity.hasColumn(BaseColumns._ID) ||
                (id = entity.getLongOrDefault(BaseColumns._ID, -1)) == -1) {
            return false;
        }

        return generalDelete(database, id, entity.getTableName());
    }

    private static boolean generalDelete(SQLiteDatabase database, long id, String table) {
        SqlCondExpr idIs = new SqlCondExpr(BaseColumns._ID).equalTo(id);
        boolean isError = true;

        database.beginTransaction();
        try {
            // Return true if the number of affected rows is 1, otherwise rollback and return false.
            final int affected = database.delete(table, idIs.formalize(), null);
            if (affected == 1) {
                database.setTransactionSuccessful();
                isError = false;
            }
        } finally {
            database.endTransaction();
        }

        return !isError;
    }

    private static boolean generalUpdate(SQLiteDatabase database, SqlEntity entity) {
        final long id;
        if (entity.getTableName() == null ||
                !entity.hasColumn(BaseColumns._ID) ||
                (id = entity.getLongOrDefault(BaseColumns._ID, INVALID_ID)) == INVALID_ID) {

            return false;
        }

        SqlCondExpr idIs = new SqlCondExpr(BaseColumns._ID).equalTo(id);
        boolean isError = true;

        database.beginTransaction();
        try {
            final int affected = database.update(entity.getTableName(), entity.getValueMap(), idIs.formalize(), null);
            // Return true if the number of affected rows is 1, otherwise rollback and return false.
            if (affected == 1) {
                database.setTransactionSuccessful();
                isError = false;
            }

        } finally {
            database.endTransaction();
        }

        return !isError;
    }
}
