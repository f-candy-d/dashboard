package com.f_candy_d.dashboard.presentation.view;

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
import android.widget.Toast;

import com.f_candy_d.dashboard.R;
import com.f_candy_d.dashboard.data.model.Dashboard;
import com.f_candy_d.dashboard.presentation.contract.HomeContract;
import com.f_candy_d.dashboard.presentation.presenter.HomePresenter;
import com.f_candy_d.dashboard.presentation.utils.ItemClickHelper;
import com.f_candy_d.dashboard.presentation.component.DashboardAdapter;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private static final int SINGLE_SPAN_COUNT = 1;
    private static final int MULTIPLE_SPAN_COUNT = 2;

    private HomePresenter mPresenter;
    private boolean mIsUiAvailable = false;
    private DashboardAdapter mDashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        onCreateUI();
        mPresenter = new HomePresenter();
        mIsUiAvailable = true;
        mPresenter.onStart(this);
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

        // # Adapter

        mDashboardAdapter = new DashboardAdapter();
        recyclerView.setAdapter(mDashboardAdapter);

        // # ItemClickHelper

        ItemClickHelper<DashboardAdapter.ViewHolder> itemClickHelper =
                new ItemClickHelper<>(new ItemClickHelper.Callback<DashboardAdapter.ViewHolder>() {
            @Override
            public void onItemClick(DashboardAdapter.ViewHolder viewHolder) {
                Dashboard dashboard = mDashboardAdapter.getAt(viewHolder.getAdapterPosition());
                mPresenter.onOpenDashboardDetails(dashboard);
            }

            @Override
            public void onItemLongClick(DashboardAdapter.ViewHolder viewHolder) {}
        });

        itemClickHelper.attachToRecyclerView(recyclerView);

        // # Bottom Tools

        findViewById(R.id.add_new_board_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onCreateNewDashboard();
            }
        });
    }

    @Override
    public boolean isAvailable() {
        return mIsUiAvailable;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsUiAvailable = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsUiAvailable = false;
    }

    @Override
    public void appendDashboards(List<Dashboard> dashboards) {
        mDashboardAdapter.addDashboards(dashboards);
    }

    @Override
    public void replaceDashboards(List<Dashboard> newDashboards) {
        mDashboardAdapter.replaceDashboards(newDashboards);
    }

    @Override
    public void onDashboardArchived(Dashboard dashboard) {
        mDashboardAdapter.removeDashboard(dashboard);
    }

    @Override
    public void showArchivingDashboardError() {
        Toast.makeText(this, "Error occurred archiving a Dashboard...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeDashboard(Dashboard dashboard) {
        mDashboardAdapter.removeDashboard(dashboard);
    }

    @Override
    public void showLoadingDashboardsError() {
        Toast.makeText(this, "Error occurred loading dashboards...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDashboardDetailsUi(long targetDashboardId) {
        Intent intent = new Intent(this, DashboardDetailsActivity.class);
        intent.putExtra(DashboardDetailsActivity.KEY_TARGET_DASHBOARD_ID, targetDashboardId);
        intent.putExtra(DashboardDetailsActivity.KEY_START_WITH_EDIT_TITLE_DIALOG, false);
        startActivity(intent);
    }

    @Override
    public void showCreateNewDashboardUi() {
        Intent intent = new Intent(this, DashboardDetailsActivity.class);
        intent.putExtra(DashboardDetailsActivity.KEY_START_WITH_EDIT_TITLE_DIALOG, true);
        startActivity(intent);
    }
}
