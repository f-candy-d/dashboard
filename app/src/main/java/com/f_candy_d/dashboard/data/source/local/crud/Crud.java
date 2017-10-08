package com.f_candy_d.dashboard.data.source.local.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.f_candy_d.dashboard.data.model.Entity;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.sqliteutils.BaseTable;
import com.f_candy_d.sqliteutils.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/8/17.
 */

public class Crud {

    public static <T extends Entity<T>> long create(SQLiteDatabase db, T entity, String table, ValueMapper<T> mapper) {
        long id = db.insert(table, null, mapper.map(entity, false));
        // SQLiteDatabase#insert() method returns the row ID of the newly inserted row, or -1 if an error occurred.
        // See document -> https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String, java.lang.String, android.content.ContentValues)
        return (id != -1) ? id : DataSource.INVALID_ID;
    }

    public static <T extends Entity<T>> List<T>
    read(SQLiteDatabase db, QueryBuilder queryBuilder, ModelCreator<T> creator) {
        Cursor cursor = queryBuilder.query(db);
        ContentValues valueMap = new ContentValues();
        ArrayList<T> entities = new ArrayList<>(cursor.getCount());
        boolean hasNext = cursor.moveToFirst();

        while (hasNext) {
            valueMap.clear();
            DatabaseUtils.cursorRowToContentValues(cursor, valueMap);
            entities.add(creator.create(valueMap));
            hasNext = cursor.moveToNext();
        }

        return entities;
    }

    public static <T extends Entity<T>> boolean
    update(SQLiteDatabase db, T entity, String table, boolean doRollbackIfError, ValueMapper<T> mapper) {
        if (entity.getId() == DataSource.INVALID_ID) {
            return false;
        }

        String where = new QueryBuilder()
                .whereColumnEquals(BaseTable._ID, entity.getId())
                .getWhereClause();

        boolean isError = true;
        db.beginTransaction();
        try {
            final int affected = db.update(table, mapper.map(entity, true), where, null);
            // Return true if the number of affected rows is 1, otherwise rollback and return false.
            if (affected == 1 || !doRollbackIfError) {
                db.setTransactionSuccessful();
                isError = false;
            }

        } finally {
            db.endTransaction();
        }

        return !isError;
    }

    public static <T extends Entity<T>> boolean
    delete(SQLiteDatabase db, T entity, String table, boolean doRollbackIfError) {
        return delete(db, entity.getId(), table, doRollbackIfError);
    }

    public static  <T extends Entity<T>> boolean
    delete(SQLiteDatabase db, long id, String table, boolean doRollbackIfError) {
        if (id == DataSource.INVALID_ID) {
            return false;
        }

        String where = new QueryBuilder()
                .whereColumnEquals(BaseTable._ID, id)
                .getWhereClause();

        boolean isError = true;
        db.beginTransaction();
        try {
            // Return true if the number of affected rows is 1, otherwise rollback and return false.
            final int affected = db.delete(table, where, null);
            if (affected == 1 || !doRollbackIfError) {
                db.setTransactionSuccessful();
                isError = false;
            }
        } finally {
            db.endTransaction();
        }

        return !isError;
    }

    public interface ValueMapper<T extends Entity<T>> {
        ContentValues map(T entity, boolean containId);
    }

    public interface ModelCreator<T extends Entity<T>> {
        T create(ContentValues contentValues);
    }
}
