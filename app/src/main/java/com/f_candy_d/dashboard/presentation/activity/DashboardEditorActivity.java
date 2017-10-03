package com.f_candy_d.dashboard.presentation.activity;

import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.f_candy_d.dashboard.R;
import com.f_candy_d.dashboard.data.source.Repository;
import com.f_candy_d.dashboard.domain.DashboardEditor;
import com.f_candy_d.dashboard.presentation.dialog.EditTextDialog;
import com.f_candy_d.dashboard.utils.ColorUtils;

public class DashboardEditorActivity extends AppCompatActivity
        implements DashboardEditor.SaveResultListener {

    private static final String FRAGMENT_TAG_EDIT_TEXT_DIALOG = "edit_text_dialog";
    private static final String FRAGMENT_TAG_COLOR_PICKER_DIALOG = "color_picker_dialog";

    /**
     * For Extras
     */
    private static final String KEY_START_WITH_EDIT_TITLE_DIALOG = "start_with_edit_title_dialog";
    private static final String KEY_DASHBOARD_ID = "dashboard_id";

    private DashboardEditor mDashboardEditor;
    private boolean mIsActivityOnFirstRun;

    public static Bundle makeExtras(boolean startWithEditTitleDialog) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_START_WITH_EDIT_TITLE_DIALOG, startWithEditTitleDialog);
        return bundle;
    }

    public static Bundle makeExtras(long dashboardId, boolean startWithEditTitleDialog) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_DASHBOARD_ID, dashboardId);
        bundle.putBoolean(KEY_START_WITH_EDIT_TITLE_DIALOG, startWithEditTitleDialog);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_editor);

        // Initialization
        mIsActivityOnFirstRun = (savedInstanceState == null);
        long id = getIntent().getLongExtra(KEY_DASHBOARD_ID, Repository.INVALID_ID);
        if (id != Repository.INVALID_ID) {
            mDashboardEditor = new DashboardEditor(this, id);
        } else {
            mDashboardEditor = new DashboardEditor(this);
        }

        onCreateUI(savedInstanceState);
    }

    private void onCreateUI(Bundle savedInstanceState) {

        // # Toolbar & ToolbarLayout

        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(mDashboardEditor.getTitle());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // # FAB

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // # Restore Dialog's listeners

        if (savedInstanceState != null) {
            EditTextDialog editTextDialog = (EditTextDialog) getSupportFragmentManager()
                    .findFragmentByTag(FRAGMENT_TAG_EDIT_TEXT_DIALOG);
            if (editTextDialog != null) {
                editTextDialog.setButtonClickListener(EDIT_TEXT_DIALOG_BUTTON_CLICK_LISTENER);
            }

            ColorPickerDialog colorPickerDialog = (ColorPickerDialog) getFragmentManager()
                    .findFragmentByTag(FRAGMENT_TAG_COLOR_PICKER_DIALOG);
            if (colorPickerDialog != null) {
                colorPickerDialog.setOnColorSelectedListener(COLOR_SELECTED_LISTENER);
            }
        }

        // # Theme Color

        invalidateThemeColor(mDashboardEditor.getThemeColor());
    }

    private final EditTextDialog.ButtonClickListener EDIT_TEXT_DIALOG_BUTTON_CLICK_LISTENER =
            new EditTextDialog.ButtonClickListener() {
                @Override
                public void onPositiveButtonClick(int tag, String text) {
                    mDashboardEditor.onInputTitle(text);
                    ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(text);
                }

                @Override
                public void onNegativeButtonClick(int tag, String text) {}
            };

    private void showEditTitleDialog(String defaultTitle, String cancelMessage) {
        new EditTextDialog.Builder()
                .negativeButton(cancelMessage)
                .editText(defaultTitle)
                .positiveButton("OK")
                .title("What is the Dashboard's Title?")
                .editTextHint("Title")
                .buttonClickListener(EDIT_TEXT_DIALOG_BUTTON_CLICK_LISTENER)
                .create()
                .show(getSupportFragmentManager(), FRAGMENT_TAG_EDIT_TEXT_DIALOG);
    }

    private void invalidateThemeColor(int color) {
        findViewById(R.id.root_view).setBackgroundColor(color);
        findViewById(R.id.app_bar).setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ColorUtils.manipulateBrightness(color, 0.7f));
        }
    }

    private final ColorPickerSwatch.OnColorSelectedListener COLOR_SELECTED_LISTENER
            = new ColorPickerSwatch.OnColorSelectedListener() {
        @Override
        public void onColorSelected(int color) {
            mDashboardEditor.onInputThemeColor(color);
            invalidateThemeColor(color);
        }
    };

    private void showColorPickerDialog(int selectedColor) {
        TypedArray a = getResources().obtainTypedArray(R.array.task_theme_colors);
        int[] colors = new int[a.length()];
        for (int i = 0; i < a.length(); ++i) {
            colors[i] = a.getColor(i, 0);
        }
        a.recycle();

        int paletteColumnCount = 4;
        ColorPickerDialog dialog = new ColorPickerDialog();
        dialog.initialize(
                R.string.theme_color_picker_dialog_title,
                colors,
                selectedColor,
                paletteColumnCount,
                colors.length);

        dialog.setOnColorSelectedListener(COLOR_SELECTED_LISTENER);
        dialog.show(getFragmentManager(), FRAGMENT_TAG_COLOR_PICKER_DIALOG);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean startWithEditTitleDialog = getIntent().getBooleanExtra(KEY_START_WITH_EDIT_TITLE_DIALOG, false);
        if (mIsActivityOnFirstRun && startWithEditTitleDialog) {
            showEditTitleDialog(mDashboardEditor.getTitle(), "SKIP");
            mIsActivityOnFirstRun = false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        mDashboardEditor.onSave();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_title) {
            showEditTitleDialog(mDashboardEditor.getTitle(), "CANCEL");
        } else if (id == R.id.action_select_theme_color) {
            showColorPickerDialog(mDashboardEditor.getThemeColor());
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * BaseEditor.SaveResultListener implementation
     * ----------------------------------------------------------------------------- */

    @Override
    public void onSaveSuccessful() {
        Toast.makeText(this, "Save was successful!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveFailed() {
        Toast.makeText(this, "Sorry, failed to save...", Toast.LENGTH_SHORT).show();
    }
}
