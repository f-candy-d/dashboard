package com.f_candy_d.dashboard.data.model;

import com.f_candy_d.dashboard.data.source.DataSource;

/**
 * Created by daichi on 10/1/17.
 */

abstract class Entity {

    public static long DEFAULT_ID = DataSource.INVALID_ID;

    /**
     * This variable has package-private visibility.
     */
    private final long mId;

    public Entity(long id) {
        mId = id;
    }

    public Entity() {
        this(DataSource.INVALID_ID);
    }

    public long getId() {
        return mId;
    }
}
