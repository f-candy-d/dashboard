package com.f_candy_d.dashboard.data.model;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.source.DataSource;

import javax.annotation.Nonnegative;

/**
 * Created by daichi on 10/1/17.
 */

abstract public class Entity<T extends Entity<T>> {

    public static long DEFAULT_ID = DataSource.INVALID_ID;

    private long mId;

    public Entity(long id) {
        mId = id;
    }

    public Entity() {
        this(DataSource.INVALID_ID);
    }

    public Entity(@NonNull T source) {
        initialize(source);
    }

    public long getId() {
        return mId;
    }

    /**
     * This should be used only by BaseModifier class
     */
    void setId(long id) {
        mId = id;
    }

    @Override
    abstract public int hashCode();

    @Override
    abstract public boolean equals(Object obj);

    /**
     * Override this method in a Child class.
     * Do not forget to call super.initialize(T).
     */
    public void initialize(@NonNull T source) {
        mId = source.getId();
    }

    /**
     * MODIFIER
     * ----------------------------------------------------------------------------- */

    static class BaseModifier<T extends Entity<T>, T2 extends BaseModifier<T, T2>> {

        private T mTarget = null;

        public BaseModifier() {}

        public BaseModifier(@NonNull T target) {
            setTarget(target);
        }

        T getTarget() {
            return mTarget;
        }

        @SuppressWarnings("unchecked")
        public T2 setTarget(@NonNull T target) {
            mTarget = target;
            return (T2) this;
        }

        public T releaseTarget() {
            T target = mTarget;
            mTarget = null;
            return target;
        }

        @SuppressWarnings("unchecked")
        public T2 id(long id) {
            mTarget.setId(id);
            return (T2) this;
        }
    }

    /**
     * MODIFIER FOR ENTITY
     * ----------------------------------------------------------------------------- */

    public final static class Modifier<T extends Entity<T>> extends BaseModifier<T, Entity.Modifier<T>> {

        public Modifier() {}

        public Modifier(@NonNull T target) {
            super(target);
        }
    }
}
