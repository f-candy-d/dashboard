package com.f_candy_d.infra.sqlite;

import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;

import com.f_candy_d.infra.sql_utils.SqlWhere;

/**
 * Created by daichi on 9/30/17.
 */

abstract public class SqliteEntityLoader<T extends SqliteStreamEntity> {

    public static final int INVALID_INDEX = SortedList.INVALID_POSITION;

    private Callback mCallback;
    private SortedList<T> mEntites;

    public SqliteEntityLoader(Class<T> klass) {
        mEntites = new SortedList<>(klass,
                new SortedList.Callback<T>() {
                    @Override
                    public int compare(T o1, T o2) {
                        return Long.valueOf(o1.getId()).compareTo(o2.getId());
                    }

                    @Override
                    public void onChanged(int position, int count) {
                        if (mCallback != null) {
                            mCallback.onChanged(position, count);
                        }
                    }

                    @Override
                    public boolean areContentsTheSame(T oldItem, T newItem) {
                        return areColumnsTheSame(oldItem, newItem);
                    }

                    @Override
                    public boolean areItemsTheSame(T item1, T item2) {
                        return areEntitesTheSame(item1, item2);
                    }

                    @Override
                    public void onInserted(int position, int count) {
                        if (mCallback != null) {
                            mCallback.onLoaded(position, count);
                        }
                    }

                    @Override
                    public void onRemoved(int position, int count) {
                        if (mCallback != null) {
                            mCallback.onRelease(position, count);
                        }
                    }

                    @Override
                    public void onMoved(int fromPosition, int toPosition) {
                        if (mCallback != null) {
                            mCallback.onMove(fromPosition, toPosition);
                        }
                    }
                });
    }

    private boolean areColumnsTheSame(T oldItem, T newItem) {
        return oldItem.equals(newItem);
    }

    private boolean areEntitesTheSame(T item1, T item2) {
        return item1.getId() == item2.getId();
    }

    final public void setCallback(Callback callback) {
        mCallback = callback;
    }

    /**
     * GET LOADED ENTITY
     * ----------------------------------------------------------------------------- */

    final public T get(int index) {
        return mEntites.get(index);
    }

    /**
     * LOAD ENTITY DATA FROM THE DATABASE
     * ----------------------------------------------------------------------------- */

    @Nullable abstract protected T onLoad(long id);

    final public int load(long id) {
        T entity = onLoad(id);
        if (entity != null) {
            return mEntites.add(entity);
        }

        return INVALID_INDEX;
    }

    abstract protected T[] onLoadIf(SqlWhere where);

    /**
     * @return The number of loaded entities
     */
    final public int loadIf(SqlWhere where) {
        T[] entities = onLoadIf(where);
        mEntites.addAll(entities);
        return entities.length;
    }

    /**
     * RELEASE ENTITY DATA FROM THE LIST (DO NOT DELETE IT FROM THE DATABASE)
     * ----------------------------------------------------------------------------- */

    final public T releaseAt(int index) {
        return mEntites.removeItemAt(index);
    }

    final public boolean release(T entity) {
        return mEntites.remove(entity);
    }

    /**
     * UTILS
     * ----------------------------------------------------------------------------- */

    final public boolean isLoaded(long id) {
        return (indexOf(id) != INVALID_INDEX);
    }

    final public int indexOf(long id) {
        for (int i = 0; i < mEntites.size(); ++i) {
            if (mEntites.get(i).getId() == id) {
                return i;
            }
        }

        return INVALID_INDEX;
    }

    final public int indexOf(T entity) {
        for (int i = 0; i < mEntites.size(); ++i) {
            if (areEntitesTheSame(mEntites.get(i), entity)) {
                return i;
            }
        }

        return INVALID_INDEX;
    }

    final public int getLoadedItemCount() {
        return mEntites.size();
    }

    /**
     * CALLBACK INTERFACE
     * ----------------------------------------------------------------------------- */

    public interface Callback {
        void onChanged(int index, int count);
        void onLoaded(int index, int count);
        void onRelease(int index, int count);
        void onMove(int fromIndex, int toIndex);
    }
}
