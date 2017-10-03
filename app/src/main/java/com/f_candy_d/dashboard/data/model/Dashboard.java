package com.f_candy_d.dashboard.data.model;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

/**
 * Created by daichi on 10/1/17.
 */

public class Dashboard extends Entity {

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

    private Dashboard(long id, String title, boolean isArchived, int themeColor) {
        super(id);
        mTitle = title;
        mIsArchived = isArchived;
        mThemeColor = themeColor;
    }

    private Dashboard(@NonNull Dashboard source) {
        super(source.getId());
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
     * SETTER (THESE METHODS CAN BE USED FROM EDITOR CLASS)
     * ----------------------------------------------------------------------------- */

    private void setTitle(String title) {
        mTitle = title;
    }

    private void setArchived(boolean archived) {
        mIsArchived = archived;
    }

    private void setThemeColor(int themeColor) {
        mThemeColor = themeColor;
    }

    /**
     * BUILDER
     * ----------------------------------------------------------------------------- */

    public static class Editor extends BaseEditor<Dashboard, Editor> {

        private Dashboard mDashboard;

        public Editor() {
            super();
        }

        public Editor(@NonNull Dashboard source) {
            super(source);
        }

        @Override
        public Editor importSource(@NonNull Dashboard source) {
            mDashboard = new Dashboard(Preconditions.checkNotNull(source));
            return this;
        }

        @Override
        public void initializeAsDefault() {
            importSource(Dashboard.createAsDefault());
        }

        @Override
        public Dashboard export() {
            return new Dashboard(mDashboard);
        }

        @Override
        public Editor id(long id) {
            mDashboard.setId(id);
            return this;
        }

        @Override
        public long id() {
            return mDashboard.getId();
        }

        public Editor title(String title) {
            mDashboard.setTitle(title);
            return this;
        }

        public String title() {
            return mDashboard.getTitle();
        }

        public Editor isArchived(boolean isArchived) {
            mDashboard.setArchived(isArchived);
            return this;
        }

        public boolean isArchived() {
            return mDashboard.isArchived();
        }

        public Editor themeColor(int themeColor) {
            mDashboard.setThemeColor(themeColor);
            return this;
        }

        public int themeColor() {
            return mDashboard.getThemeColor();
        }
    }
}
