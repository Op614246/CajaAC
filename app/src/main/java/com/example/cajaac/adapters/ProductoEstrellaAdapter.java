package com.example.cajaac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.models.ProductoEstrellaItem;

import java.util.List;

public class ProductoEstrellaAdapter extends RecyclerView.Adapter<ProductoEstrellaAdapter.ProductoEstrellaViewHolder> {
    private List<ProductoEstrellaItem> items;
    private Context context;

    public ProductoEstrellaAdapter(Context context, List<ProductoEstrellaItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ProductoEstrellaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_estrella, parent, false);
        return new ProductoEstrellaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoEstrellaViewHolder holder, int position) {
        ProductoEstrellaItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductoEstrellaViewHolder extends RecyclerView.ViewHolder {
        TextView tvRanking;
        TextView tvLetraInicial;
        TextView tvNombre;
        TextView tvCategoria;
        TextView tvVentas;
        TextView tvPorcentaje;

        public ProductoEstrellaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRanking = itemView.findViewById(R.id.tvRanking);
            tvLetraInicial = itemView.findViewById(R.id.tvLetraInicial);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaProducto);
            tvVentas = itemView.findViewById(R.id.tvVentasProducto);
            tvPorcentaje = itemView.findViewById(R.id.tvPorcentajeProducto);
        }

        public void bind(ProductoEstrellaItem item) {
            tvRanking.setText(item.getRanking());
            tvLetraInicial.setText(item.getLetra());
            tvNombre.setText(item.getNombre());
            tvCategoria.setText(item.getCategoria());
            tvVentas.setText(item.getVentas());
            tvPorcentaje.setText(item.getPorcentaje());
            tvPorcentaje.setTextColor(context.getResources().getColor(item.getColorPorcentaje(), null));

            // Configurar badge según el tipo (I, P, R)
            int badgeBackground;
            int badgeTextColor;

            switch (item.getLetra()) {
                case "I": // Insumo
                    badgeBackground = R.drawable.rectangular_badge_primary;
                    badgeTextColor = R.color.primary;
                    break;
                case "P": // Producto
                    badgeBackground = R.drawable.rectangular_badge_info;
                    badgeTextColor = R.color.info;
                    break;
                case "R": // Receta
                    badgeBackground = R.drawable.rectangular_badge_secondary;
                    badgeTextColor = R.color.secondary;
                    break;
                default: // Por defecto usar info
                    badgeBackground = R.drawable.rectangular_badge_info;
                    badgeTextColor = R.color.info;
                    break;
            }

            tvLetraInicial.setBackgroundResource(badgeBackground);
            tvLetraInicial.setTextColor(context.getResources().getColor(badgeTextColor, null));
        }
    }
}


