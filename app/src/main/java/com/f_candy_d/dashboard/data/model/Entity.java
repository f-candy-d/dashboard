package com.f_candy_d.dashboard.data.model;

import com.f_candy_d.dashboard.data.source.DataSource;

/**
 * Created by daichi on 10/1/17.
 */

abstract public class Entity {

    public static long DEFAULT_ID = DataSource.INVALID_ID;

    private long mId;

    public Entity(long id) {
        mId = id;
    }

    public Entity() {
        this(DataSource.INVALID_ID);
    }

    public long getId() {
        return mId;
    }

    protected void setId(long id) {
        mId = id;
    }

    @Override
    abstract public int hashCode();

    @Override
    abstract public boolean equals(Object obj);
}
