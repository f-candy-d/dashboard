package com.f_candy_d.dashboard.presentation.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.data.source.DataSource;
import com.f_candy_d.dashboard.data.source.Repository;
import com.f_candy_d.dashboard.presentation.ItemClickHelper;
import com.f_candy_d.dashboard.presentation.adapter.DashboardAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int SINGLE_SPAN_COUNT = 1;
    private static final int MULTIPLE_SPAN_COUNT = 2;

    private DashboardAdapter mDashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialization
        Repository.getInstance().loadAllDashboards(
                new DataSource.LoadALotOfDataCallback<Dashboard>() {
                    @Override
                    public void onDataLoaded(@NonNull List<Dashboard> data) {
                        mDashboardAdapter = new DashboardAdapter(data);
                    }
                },
                new DataSource.OperationFailedCallback() {
                    @Override
                    public void onFailed() {
                        mDashboardAdapter = new DashboardAdapter(new ArrayList<Dashboard>());
                    }
                });

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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(MULTIPLE_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mDashboardAdapter);

        // # ItemClickHelper

        ItemClickHelper<DashboardAdapter.ViewHolder> itemClickHelper =
                new ItemClickHelper<>(new ItemClickHelper.Callback<DashboardAdapter.ViewHolder>() {
            @Override
            public void onItemClick(DashboardAdapter.ViewHolder viewHolder) {
                Dashboard dashboard = mDashboardAdapter.getAt(viewHolder.getAdapterPosition());
                launchDashboardEditor(dashboard.getId());
            }

            @Override
            public void onItemLongClick(DashboardAdapter.ViewHolder viewHolder) {}
        });

        itemClickHelper.attachToRecyclerView(recyclerView);

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
        intent.putExtras(DashboardEditorActivity.makeExtras(true));
        startActivity(intent);
    }

    private void launchDashboardEditor(long id) {
        Intent intent = new Intent(this, DashboardEditorActivity.class);
        intent.putExtras(DashboardEditorActivity.makeExtras(id, false));
        startActivity(intent);
    }
}
