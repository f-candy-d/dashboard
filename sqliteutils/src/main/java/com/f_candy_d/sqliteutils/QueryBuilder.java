package com.f_candy_d.sqliteutils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by daichi on 10/2/17.
 */

public class QueryBuilder {

    private boolean mDistinct;
    @NonNull private final HashSet<String> mTables;
    @NonNull private final List<String> mProjections;
    private String mSelection;

    public QueryBuilder() {
        mDistinct = false;
        mTables = new HashSet<>();
        mProjections = new ArrayList<>();
        mSelection = null;
    }

    public Cursor query(@NonNull SQLiteDatabase database) {
        return database.query(
                mDistinct,
                (mTables.size() != 0) ? TextUtils.join(",", mTables) : null,
                (mProjections.size() != 0) ? mProjections.toArray(new String[]{}) : null,
                mSelection,
                null,
                null,
                null,
                null,
                null);
    }

    @Nullable
    public String getWhereClause() {
        return mSelection;
    }

    /**
     * SET DISTINCT
     * ----------------------------------------------------------------------------- */

    public QueryBuilder distinct(boolean distinct) {
        mDistinct = distinct;
        return this;
    }

    /**
     * SET TABLE NAMES
     * ----------------------------------------------------------------------------- */

    public QueryBuilder table(String table) {
        mTables.add(table);
        return this;
    }

    public QueryBuilder tables(String... tables) {
        mTables.addAll(Arrays.asList(tables));
        return this;
    }

    /**
     * SET COLUMN NAMES WHICH WILL BE RETURNED
     * ----------------------------------------------------------------------------- */

    public QueryBuilder projection(String projection) {
        mProjections.add(projection);
        return this;
    }

    public QueryBuilder projections(String... projections) {
        mProjections.addAll(Arrays.asList(projections));
        return this;
    }

    /**
     * WHERE CLAUSE
     * ----------------------------------------------------------------------------- */

    public QueryBuilder whereColumnEquals(String column, Object arg) {
        uniteWhereClause(column, "=", arg);
        return this;
    }

    public QueryBuilder whereColumnNotEquals(String column, Object arg) {
        uniteWhereClause(column, "!=", arg);
        return this;
    }

    public QueryBuilder whereColumnLessThan(String column, Object arg) {
        uniteWhereClause(column, "<", arg);
        return this;
    }

    public QueryBuilder whereColumnLessThanOrEquals(String column, Object arg) {
        uniteWhereClause(column, "<=", arg);
        return this;
    }

    public QueryBuilder whereColumnGreaterThan(String column, Object arg) {
        uniteWhereClause(column, ">", arg);
        return this;
    }

    public QueryBuilder whereColumnGreaterThanOrEquals(String column, Object arg) {
        uniteWhereClause(column, ">=", arg);
        return this;
    }

    public QueryBuilder whereColumnLike(String column, String regex) {
        uniteWhereClause(column, "LIKE", regex);
        return this;
    }

    public QueryBuilder whereColumnBetween(String column, Object min, Object max) {
        String stringMinArg = formatArgumentForSqlite(min);
        String stringMaxArg = formatArgumentForSqlite(max);
        uniteWhereClause(column, "BETWEEN", stringMinArg + " AND " + stringMaxArg);
        return this;
    }

    public QueryBuilder whereColumnIn(String column, Object... args) {
        String[] stringArgs = new String[args.length];
        int index = 0;
        for (Object arg : args) {
            stringArgs[index] = formatArgumentForSqlite(arg);
            ++index;
        }

        String arg = "(" + TextUtils.join(",", stringArgs) + ")";
        uniteWhereClause(column, "IN", arg);

        return this;
    }

    /**
     * UTILS FOR WHERE CLAUSE
     * ----------------------------------------------------------------------------- */

    private static final String LOGICAL_AND_OPERATOR = "AND";
    private static final String LOGICAL_OR_OPERATOR = "AND";
    private String mSpecifiedLogicalOperator = LOGICAL_AND_OPERATOR;

    /**
     * Example:
     *
     * queryBuilder.whereXXX().and().whereXXX();
     */
    public QueryBuilder and() {
        mSpecifiedLogicalOperator = LOGICAL_AND_OPERATOR;
        return this;
    }

    public QueryBuilder or() {
        mSpecifiedLogicalOperator = LOGICAL_OR_OPERATOR;
        return this;
    }

    private void uniteWhereClause(String column, String operator, Object arg) {
        if (mSelection == null) {
            mSelection = column  + " " + operator + " " + formatArgumentForSqlite(arg);
        } else {
            mSelection = mSelection.concat(" " + mSpecifiedLogicalOperator + " column " + operator + " " + formatArgumentForSqlite(arg));
        }
    }

    private String formatArgumentForSqlite(Object arg) {
        if (arg instanceof Boolean) {
            return String.valueOf(DataConverter.toSqliteBool((Boolean) arg));
        } else if (arg instanceof String) {
            return "'" + arg + "'";
        } else {
            return arg.toString();
        }
    }
}
