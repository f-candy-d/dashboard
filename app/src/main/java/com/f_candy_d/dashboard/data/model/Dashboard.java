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

        private long mId = DEFAULT_ID;
        private String mTitle = DEFAULT_TITLE;
        private boolean mIsArchived = DEFAULT_IS_ARCHIVED;
        private int mThemeColor = DEFAULT_THEME_COLOR;

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
