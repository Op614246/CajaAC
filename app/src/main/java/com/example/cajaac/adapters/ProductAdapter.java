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
import com.example.cajaac.models.ProductItem;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductItem> items;
    private Context context;

    public ProductAdapter(Context context, List<ProductItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_chart, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView colorIndicator;
        TextView tvLabel;
        TextView tvValue;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
            tvLabel = itemView.findViewById(R.id.tvProductLabel);
            tvValue = itemView.findViewById(R.id.tvProductValue);
        }

        public void bind(ProductItem item) {
            colorIndicator.setColorFilter(context.getResources().getColor(item.getColorRes(), null));
            tvLabel.setText(item.getLabel());
            tvValue.setText(item.getValue());
        }
    }
}

