package com.f_candy_d.dashboard.domain.structure;

import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data_store.TextNoteTable;
import com.f_candy_d.infra.sql_utils.SqlEntity;
import com.f_candy_d.infra.sqlite.SqliteStreamEntity;

/**
 * Created by daichi on 9/30/17.
 */

public class TextNote extends SqliteStreamEntity {

    private long mParentDashboardId;
    private String mTitle;
    private String mBody;

    public static TextNote createIfPossible(long id) {
        TextNote textNote = new TextNote();
        return (textNote.fetch(id)) ? textNote : null;
    }

    public TextNote() {
        super(TextNoteTable.TABLE_NAME);
    }

    public TextNote(long id) {
        super(id, TextNoteTable.TABLE_NAME);
    }

    public TextNote(SqlEntity entity) {
        super(entity);
    }

    @Override
    protected SqlEntity toSqlEntity() {
        SqlEntity entity = new SqlEntity(TextNoteTable.TABLE_NAME);
        entity.put(TextNoteTable._PARENT_DASHBOARD_ID, mParentDashboardId);
        entity.put(TextNoteTable._TITLE, mTitle);
        entity.put(TextNoteTable._BODY, mBody);
        return entity;
    }

    @Override
    protected void constructFromSqlEntity(@NonNull SqlEntity entity) {
        mParentDashboardId = entity.getLongOrDefault(TextNoteTable._PARENT_DASHBOARD_ID, mParentDashboardId);
        mTitle = entity.getStringOrDefault(TextNoteTable._TITLE, mTitle);
        mBody = entity.getStringOrDefault(TextNoteTable._BODY, mBody);
    }

    @Override
    public void initializeWithDefaultColumnValues() {
        mParentDashboardId = TextNoteTable.defaultParentDashboardId();
        mTitle = TextNoteTable.defaultTitle();
        mBody = TextNoteTable.defaultBody();
    }

    @Override
    public boolean isDefaultState() {
        return equals(new TextNote());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextNote textNote = (TextNote) o;

        if (mParentDashboardId != textNote.mParentDashboardId) return false;
        if (mTitle != null ? !mTitle.equals(textNote.mTitle) : textNote.mTitle != null)
            return false;
        return mBody != null ? mBody.equals(textNote.mBody) : textNote.mBody == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (mParentDashboardId ^ (mParentDashboardId >>> 32));
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mBody != null ? mBody.hashCode() : 0);
        return result;
    }

    /**
     * GETTER & SETTER
     * ----------------------------------------------------------------------------- */

    public long getParentDashboardId() {
        return mParentDashboardId;
    }

    public void setParentDashboardId(long parentDashboardId) {
        mParentDashboardId = parentDashboardId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }
}
