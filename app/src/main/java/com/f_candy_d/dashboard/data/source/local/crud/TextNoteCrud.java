package com.f_candy_d.dashboard.data.source.local.crud;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.f_candy_d.dashboard.data.model.TextNote;
import com.f_candy_d.dashboard.data.source.local.table.TextNoteTable;

/**
 * Created by daichi on 10/8/17.
 */

public final class TextNoteCrud {

    public static TextNoteCrud newInstance() {
        return new TextNoteCrud();
    }

    String getTableName() {
        return TextNoteTable.TABLE_NAME;
    }

    ContentValues createContentValues(TextNote entity, boolean containId) {
        ContentValues contentValues = new ContentValues();

        if (containId) {
            contentValues.put(TextNoteTable._ID, entity.getId());
        }

        contentValues.put(TextNoteTable._TITLE, entity.getTitle());
        contentValues.put(TextNoteTable._BODY, entity.getBody());
        contentValues.put(TextNoteTable._PARENT_BOARD_ID, entity.getParentBoardId());

        return contentValues;
    }

    TextNote createInstance(ContentValues contentValues) {
        return new TextNote.Modifier()
                .setTarget(TextNote.createAsDefault())
                .id(contentValues.getAsLong(TextNoteTable._ID))
                .title(contentValues.getAsString(TextNoteTable._TITLE))
                .body(contentValues.getAsString(TextNoteTable._BODY))
                .parentBoardId(contentValues.getAsLong(TextNoteTable._PARENT_BOARD_ID))
                .releaseTarget();
    }
}
