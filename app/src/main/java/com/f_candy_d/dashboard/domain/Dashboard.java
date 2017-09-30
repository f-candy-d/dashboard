package com.f_candy_d.dashboard.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.dashboard.data_store.DashboardTable;
import com.f_candy_d.infra.sql_utils.SqlEntity;
import com.f_candy_d.infra.sqlite.SqliteStreamEntity;

/**
 * Created by daichi on 9/30/17.
 */

public class Dashboard extends SqliteStreamEntity {

    private String mTitle;
    private boolean mIsArchived;
    private int mThemeColor;

    @Nullable
    public static Dashboard createIfPossible(long id) {
        Dashboard dashboard = new Dashboard();
        return (dashboard.fetch(id)) ? dashboard : null;
    }

    public Dashboard() {
        super(DashboardTable.TABLE_NAME);
    }

    public Dashboard(long id) {
        super(id, DashboardTable.TABLE_NAME);
    }

    public Dashboard(SqlEntity entity) {
        super(entity);
    }

    @Override
    protected SqlEntity toSqlEntity() {
        SqlEntity entity = new SqlEntity(DashboardTable.TABLE_NAME);
        entity.put(DashboardTable._TITLE, mTitle);
        entity.put(DashboardTable._IS_ARCHIVED, mIsArchived);
        entity.put(DashboardTable._THEME_COLOR, mThemeColor);
        return entity;
    }

    @Override
    protected void constructFromSqlEntity(@NonNull SqlEntity entity) {
        mTitle = entity.getStringOrDefault(DashboardTable._TITLE, mTitle);
        mIsArchived = entity.getBooleanOrDefault(DashboardTable._IS_ARCHIVED, mIsArchived);
        mThemeColor = entity.getIntOrDefault(DashboardTable._THEME_COLOR, mThemeColor);
    }

    @Override
    public void initializeWithDefaultColumnValues() {
        mTitle = DashboardTable.defaultTitle();
        mIsArchived = DashboardTable.defaultIsArchived();
        mThemeColor = DashboardTable.defaultThemeColor();
    }

    @Override
    public boolean isDefaultState() {
        return equals(new Dashboard());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dashboard dashboard = (Dashboard) o;

        if (mIsArchived != dashboard.mIsArchived) return false;
        if (mThemeColor != dashboard.mThemeColor) return false;
        return mTitle != null ? mTitle.equals(dashboard.mTitle) : dashboard.mTitle == null;

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mIsArchived ? 1 : 0);
        result = 31 * result + mThemeColor;
        return result;
    }

    /**
     * GETTER & SETTER
     * ----------------------------------------------------------------------------- */

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isArchived() {
        return mIsArchived;
    }

    public void setArchived(boolean archived) {
        mIsArchived = archived;
    }

    public int getThemeColor() {
        return mThemeColor;
    }

    public void setThemeColor(int themeColor) {
        mThemeColor = themeColor;
    }
}
