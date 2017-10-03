package com.f_candy_d.dashboard.data.model;

import android.support.annotation.NonNull;

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

    /**
     * EDITOR
     * ----------------------------------------------------------------------------- */

    abstract static class BaseEditor<T extends Entity, T2 extends BaseEditor<T, T2>> {

        public BaseEditor() {
            initializeAsDefault();
        }

        public BaseEditor(@NonNull T source) {
            importSource(source);
        }

        abstract public T2 id(long id);
        abstract public long id();
        abstract public T2 importSource(@NonNull T source);
        abstract public void initializeAsDefault();
        abstract public T export();
    }
}
