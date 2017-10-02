package com.f_candy_d.dashboard.presentation.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.dashboard.R;
import com.f_candy_d.dashboard.data.model.Dashboard;

import java.util.List;

/**
 * Created by daichi on 9/30/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private SortedList<Dashboard> mDashboards;

    public DashboardAdapter(List<Dashboard> dashboards) {
        mDashboards = new SortedList<>(Dashboard.class,
                new SortedList.Callback<Dashboard>() {
                    @Override
                    public int compare(Dashboard o1, Dashboard o2) {
                        return 0;
                    }

                    @Override
                    public void onChanged(int position, int count) {
                        notifyItemRangeChanged(position, count);
                    }

                    @Override
                    public boolean areContentsTheSame(Dashboard oldItem, Dashboard newItem) {
                        return newItem.equals(oldItem);
                    }

                    @Override
                    public boolean areItemsTheSame(Dashboard item1, Dashboard item2) {
                        return item1.getId() == item2.getId();
                    }

                    @Override
                    public void onInserted(int position, int count) {
                        notifyItemRangeInserted(position, count);
                    }

                    @Override
                    public void onRemoved(int position, int count) {
                        notifyItemRangeRemoved(position, count);
                    }

                    @Override
                    public void onMoved(int fromPosition, int toPosition) {
                        notifyItemMoved(fromPosition, toPosition);
                    }
                });

        mDashboards.addAll(dashboards);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dashboard dashboard = mDashboards.get(position);
        holder.background.setCardBackgroundColor(dashboard.getThemeColor());
        holder.title.setText(dashboard.getTitle());
    }

    @Override
    public int getItemCount() {
        return mDashboards.size();
    }

    public Dashboard getAt(int position) {
        return mDashboards.get(position);
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
