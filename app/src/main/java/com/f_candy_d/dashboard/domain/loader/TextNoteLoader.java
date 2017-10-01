package com.f_candy_d.dashboard.domain.loader;

import android.support.annotation.Nullable;

import com.f_candy_d.dashboard.data_store.TextNoteTable;
import com.f_candy_d.dashboard.domain.structure.TextNote;
import com.f_candy_d.infra.Repository;
import com.f_candy_d.infra.sql_utils.SqlEntity;
import com.f_candy_d.infra.sql_utils.SqlQuery;
import com.f_candy_d.infra.sql_utils.SqlWhere;
import com.f_candy_d.infra.sqlite.SqliteEntityLoader;

/**
 * Created by daichi on 9/30/17.
 */

public class TextNoteLoader extends SqliteEntityLoader<TextNote> {

    public TextNoteLoader() {
        super(TextNote.class);
    }

    @Nullable
    @Override
    protected TextNote onLoad(long id) {
        return TextNote.createIfPossible(id);
    }

    @Override
    protected TextNote[] onLoadIf(SqlWhere where) {
        SqlQuery query = new SqlQuery();
        query.setSelection(where);
        query.putTables(TextNoteTable.TABLE_NAME);
        SqlEntity[] results = Repository.getSqlite().select(query);

        TextNote[] textNotes = new TextNote[results.length];
        for (int i = 0; i < results.length; ++i) {
            results[i].setTableName(TextNoteTable.TABLE_NAME);
            textNotes[i] = new TextNote(results[i]);
        }

        return textNotes;
    }
}
