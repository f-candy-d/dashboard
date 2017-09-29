package com.f_candy_d.dashboard.presentation.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.f_candy_d.dashboard.R;
import com.f_candy_d.dashboard.domain.DashboardLoader;
import com.f_candy_d.dashboard.presentation.adapter.DashboardAdapter;

public class HomeActivity extends AppCompatActivity {

    private static final int SINGLE_SPAN_COUNT = 1;
    private static final int MULTIPLE_SPAN_COUNT = 2;

    private DashboardLoader mDashboardLoader;
    private DashboardAdapter mDashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialization
        mDashboardLoader = new DashboardLoader();
        mDashboardLoader.loadIf(null);
        mDashboardAdapter = new DashboardAdapter(mDashboardLoader);

        onCreateUI();
    }

    private void onCreateUI() {

        // # StatusBar Color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.home_status_bar));
        }

        // # ToolBar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.home_toolbar_background));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.home_toolbar_title));
        setSupportActionBar(toolbar);

        // # Recycler View

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(SINGLE_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mDashboardAdapter);

        // # Bottom Tools

        findViewById(R.id.add_new_board_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDashboardEditor();
            }
        });
    }

    private void launchDashboardEditor() {
        Intent intent = new Intent(this, DashboardEditorActivity.class);
        startActivity(intent);
    }
}
