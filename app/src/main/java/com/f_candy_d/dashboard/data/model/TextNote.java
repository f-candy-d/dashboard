package com.f_candy_d.dashboard.data.model;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 10/8/17.
 */

public class TextNote extends BaseNote<TextNote> {

    public static final String DEFAULT_TITLE = null;
    public static final String DEFAULT_BODY = null;

    private String mTitle;
    private String mBody;

    public static TextNote createAsDefault() {
        return new TextNote(
                DEFAULT_ID,
                DEFAULT_PARENT_BOARD_ID,
                DEFAULT_TITLE,
                DEFAULT_BODY);
    }

    public TextNote(long id, long parentBoardId, String title, String body) {
        super(id, parentBoardId);
        mTitle = title;
        mBody = body;
    }

    public TextNote(@NonNull TextNote source) {
        super(source);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextNote textNote = (TextNote) o;

        if (mTitle != null ? !mTitle.equals(textNote.mTitle) : textNote.mTitle != null)
            return false;
        return mBody != null ? mBody.equals(textNote.mBody) : textNote.mBody == null;

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mBody != null ? mBody.hashCode() : 0);
        return result;
    }

    /**
     * MODIFIER
     * ----------------------------------------------------------------------------- */

    public static class Modifier extends BaseNoteModifier<TextNote, Modifier> {

        public Modifier() {}

        public Modifier(@NonNull TextNote textNote) {
            super(textNote);
        }

        public Modifier title(String title) {
            getTarget().mTitle = title;
            return this;
        }

        public Modifier body(String body) {
            getTarget().mBody = body;
            return this;
        }
    }
}
