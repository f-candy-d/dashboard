package com.f_candy_d.dashboard.data.model;

import android.graphics.Color;
import android.support.annotation.NonNull;

/**
 * Created by daichi on 10/1/17.
 */

public class Dashboard extends Entity<Dashboard> {

    public static final String DEFAULT_TITLE = null;
    public static final boolean DEFAULT_IS_ARCHIVED = false;
    // @color/task_theme_color_indigo
    public static final int DEFAULT_THEME_COLOR = Color.parseColor("#3F51B5");

    private String mTitle;
    private boolean mIsArchived;
    private int mThemeColor;

    public static Dashboard createAsDefault() {
        return new Dashboard(
                DEFAULT_ID,
                DEFAULT_TITLE,
                DEFAULT_IS_ARCHIVED,
                DEFAULT_THEME_COLOR);
    }

    public Dashboard(long id, String title, boolean isArchived, int themeColor) {
        super(id);
        mTitle = title;
        mIsArchived = isArchived;
        mThemeColor = themeColor;
    }

    public Dashboard(@NonNull Dashboard source) {
        super(source.getId());
        initialize(source);
    }

    @Override
    public void initialize(@NonNull Dashboard source) {
        super.initialize(source);
        mTitle = source.getTitle();
        mIsArchived = source.isArchived();
        mThemeColor = source.getThemeColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dashboard dashboard = (Dashboard) o;

        if (mIsArchived != dashboard.mIsArchived) return false;
        if (mThemeColor != dashboard.mThemeColor) return false;
        if (mTitle != null ? !mTitle.equals(dashboard.mTitle) : dashboard.mTitle != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mIsArchived ? 1 : 0);
        result = 31 * result + mThemeColor;
        return result;
    }

    /**
     * GETTER
     * ----------------------------------------------------------------------------- */

    public String getTitle() {
        return mTitle;
    }

    public boolean isArchived() {
        return mIsArchived;
    }

    public int getThemeColor() {
        return mThemeColor;
    }

    /**
     * EDITOR
     * ----------------------------------------------------------------------------- */

    public static class Modifier extends BaseModifier<Dashboard, Modifier> {

        public Modifier() {}

        public Modifier(@NonNull Dashboard dashboard) {
            super(dashboard);
        }

        public Modifier title(String title) {
            getTarget().mTitle = title;
            return this;
        }

        public Modifier isArchived(boolean isArchived) {
            getTarget().mIsArchived = isArchived;
            return this;
        }

        public Modifier themeColor(int themeColor) {
            getTarget().mThemeColor = themeColor;
            return this;
        }
    }
}
