package com.f_candy_d.dashboard.data.model;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 10/8/17.
 */

abstract class BaseNote<T extends BaseNote<T>> extends Entity<T> {

    public static final long DEFAULT_PARENT_BOARD_ID = DEFAULT_ID;

    private long mParentBoardId;

    public BaseNote(long id, long parentBoardId) {
        super(id);
        mParentBoardId = parentBoardId;
    }

    public BaseNote(@NonNull T source) {
        super(source);
    }

    @Override
    public void initialize(@NonNull T source) {
        super.initialize(source);
        mParentBoardId = source.getParentBoardId();
    }

    public long getParentBoardId() {
        return mParentBoardId;
    }

    /**
     * This should be used only by BaseNoteModifier
     */
    void setParentBoardId(long parentBoardId) {
        mParentBoardId = parentBoardId;
    }

    /**
     * EDITOR
     * ----------------------------------------------------------------------------- */

    abstract static class BaseNoteModifier<T extends BaseNote<T>, T2 extends BaseNoteModifier<T, T2>>
            extends BaseModifier<T, T2> {

        public BaseNoteModifier() {}

        public BaseNoteModifier(@NonNull T target) {
            super(target);
        }

        public T2 parentBoardId(long id) {
            getTarget().setParentBoardId(id);
            return (T2) this;
        }
    }
}
