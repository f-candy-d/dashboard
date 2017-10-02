package com.f_candy_d.dashboard.data.model;

import android.graphics.Color;

/**
 * Created by daichi on 10/1/17.
 */

public class Dashboard extends Entity {

    public static final String DEFAULT_TITLE = null;
    public static final boolean DEFAULT_IS_ARCHIVED = false;
    // @color/task_theme_color_indigo
    public static final int DEFAULT_THEME_COLOR = Color.parseColor("#3F51B5");

    private final String mTitle;
    private final boolean mIsArchived;
    private final int mThemeColor;

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
     * BUILDER
     * ----------------------------------------------------------------------------- */

    public static class Builder {

        private long mId;
        private String mTitle;
        private boolean mIsArchived;
        private int mThemeColor;

        public Builder() {
            reset();
        }

        public Builder(Dashboard source) {
            if (source != null) {
                mId = source.getId();
                mTitle = source.getTitle();
                mIsArchived = source.isArchived();
                mThemeColor = source.getThemeColor();
            } else {
                reset();
            }
        }

        public void reset() {
            mId = DEFAULT_ID;
            mTitle = DEFAULT_TITLE;
            mIsArchived = DEFAULT_IS_ARCHIVED;
            mThemeColor = DEFAULT_THEME_COLOR;
        }

        public Builder id(long id) {
            mId = id;
            return this;
        }

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder isArchived(boolean isArchived) {
            mIsArchived = isArchived;
            return this;
        }

        public Builder themeColor(int themeColor) {
            mThemeColor = themeColor;
            return this;
        }

        public Dashboard create() {
            return new Dashboard(mId, mTitle, mIsArchived, mThemeColor);
        }
    }
}
