package com.example.cajaac.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.ui.ExpandableCardView.ExpandableItem;

import java.util.ArrayList;
import java.util.List;

public class ExpandableItemAdapter extends RecyclerView.Adapter<ExpandableItemAdapter.ViewHolder> {

    private List<ExpandableItem> items = new ArrayList<>();

    public void submitList(List<ExpandableItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expandable_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLabel;
        private final TextView tvRetention;
        private final TextView tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvRetention = itemView.findViewById(R.id.tvRetention);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(ExpandableItem item) {
            tvLabel.setText(item.label);
            tvRetention.setText(item.retention);
            tvAmount.setText(item.amount);
        }
    }
}

