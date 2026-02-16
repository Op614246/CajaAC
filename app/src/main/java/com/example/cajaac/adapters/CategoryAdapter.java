package com.example.cajaac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.models.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryItem> items;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_chart, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView colorIndicator;
        TextView tvLabel;
        TextView tvValue;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
            tvLabel = itemView.findViewById(R.id.tvCategoryLabel);
            tvValue = itemView.findViewById(R.id.tvCategoryValue);
        }

        public void bind(CategoryItem item) {
            colorIndicator.setColorFilter(context.getResources().getColor(item.getColorRes(), null));
            tvLabel.setText(item.getLabel());
            tvValue.setText(item.getValue());
        }
    }
}

