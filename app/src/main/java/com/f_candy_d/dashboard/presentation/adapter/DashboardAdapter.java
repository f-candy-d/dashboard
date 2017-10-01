package com.f_candy_d.dashboard.presentation.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.dashboard.R;
import com.f_candy_d.dashboard.domain.structure.Dashboard;
import com.f_candy_d.dashboard.domain.loader.DashboardLoader;
import com.f_candy_d.infra.sqlite.SqliteEntityLoader;

/**
 * Created by daichi on 9/30/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private DashboardLoader mLoader;

    public DashboardAdapter(DashboardLoader loader) {
        mLoader = loader;
        mLoader.setCallback(new SqliteEntityLoader.Callback() {
            @Override
            public void onChanged(int index, int count) {
                notifyItemRangeChanged(index, count);
            }

            @Override
            public void onLoaded(int index, int count) {
                notifyItemRangeInserted(index, count);
            }

            @Override
            public void onRelease(int index, int count) {
                notifyItemRangeRemoved(index, count);
            }

            @Override
            public void onMove(int fromIndex, int toIndex) {
                notifyItemMoved(fromIndex, toIndex);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dashboard dashboard = mLoader.get(position);
        holder.background.setCardBackgroundColor(dashboard.getThemeColor());
        holder.title.setText(dashboard.getTitle());
    }

    @Override
    public int getItemCount() {
        return mLoader.getLoadedItemCount();
    }

    /**
     * VIEW HOLDER
     * ----------------------------------------------------------------------------- */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView background;
        TextView title;

        ViewHolder(View view) {
            super(view);
            background = view.findViewById(R.id.background);
            title = view.findViewById(R.id.title);
        }
    }
}
