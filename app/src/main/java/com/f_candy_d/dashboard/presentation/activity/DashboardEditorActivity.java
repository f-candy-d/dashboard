package com.f_candy_d.dashboard.presentation.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.f_candy_d.dashboard.R;
import com.f_candy_d.dashboard.presentation.dialog.EditTextDialog;

public class DashboardEditorActivity extends AppCompatActivity
        implements EditTextDialog.NoticeListener {

    private boolean mIsActivityOnFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_editor);

        mIsActivityOnFirstRun = (savedInstanceState == null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showEditTitleDialog(String cancelMessage) {
        new EditTextDialog.Builder()
                .positiveButton("OK")
                .negativeButton(cancelMessage)
                .title("TITLE?")
                .editTextHint("Title")
                .create()
                .show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsActivityOnFirstRun) {
            showEditTitleDialog("SKIP");
            mIsActivityOnFirstRun = false;
        }
    }

    /**
     * EditTextDialog.NoticeListener implementation
     * ----------------------------------------------------------------------------- */

    @Override
    public void onPositiveButtonClick(int tag, String text) {
    }

    @Override
    public void onNegativeButtonClick(int tag, String text) {

    }

    @Override
    public void onTextChange(String text) {
    }
}
