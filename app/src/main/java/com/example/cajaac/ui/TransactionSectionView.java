package com.example.cajaac.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.cajaac.R;
import com.example.cajaac.TransactionItem;
import com.example.cajaac.TransactionSection;

public class TransactionSectionView extends LinearLayout {

    private ImageView ivIcon;
    private TextView tvTitle;
    private LinearLayout itemsContainer;
    private LinearLayout secondaryTotalsContainer;
    private TextView tvTotalLabel;
    private TextView tvTotalQuantity;
    private TextView tvTotalAmount;
    private LinearLayout totalContainer;

    public TransactionSectionView(Context context) {
        super(context);
        init(context);
    }

    public TransactionSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionSectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.border_rounded);

        LayoutInflater.from(context).inflate(R.layout.view_transaction_section, this, true);

        ivIcon = findViewById(R.id.ivIcon);
        tvTitle = findViewById(R.id.tvTitle);
        itemsContainer = findViewById(R.id.itemsContainer);
        secondaryTotalsContainer = findViewById(R.id.secondaryTotalsContainer);
        tvTotalLabel = findViewById(R.id.tvTotalLabel);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        totalContainer = findViewById(R.id.totalContainer);
    }

    public void setData(TransactionSection section) {
        // Configurar encabezado
        ivIcon.setImageResource(section.getIconRes());
        ivIcon.setColorFilter(ContextCompat.getColor(getContext(), section.getTitleColor()));
        tvTitle.setText(section.getTitle());
        tvTitle.setTextColor(ContextCompat.getColor(getContext(), section.getTitleColor()));

        // Limpiar items anteriores
        itemsContainer.removeAllViews();

        // Agregar header si es tipo TABLE
        if (section.getColumnType() == TransactionSection.ColumnType.TABLE) {
            View headerView = LayoutInflater.from(getContext()).inflate(
                    R.layout.header_transaction_table, itemsContainer, false);
            itemsContainer.addView(headerView);
        }

        // Agregar items dinámicamente
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (TransactionItem item : section.getItems()) {
            View itemView;

            // Seleccionar el layout según el tipo de columnas
            if (section.getColumnType() == TransactionSection.ColumnType.TABLE) {
                itemView = inflater.inflate(R.layout.item_transaction_table, itemsContainer, false);

                TextView tvDate = itemView.findViewById(R.id.tvDate);
                TextView tvUser = itemView.findViewById(R.id.tvUser);
                TextView tvConcept = itemView.findViewById(R.id.tvConcept);
                TextView tvAmount = itemView.findViewById(R.id.tvAmount);

                tvDate.setText(item.getDate());
                tvUser.setText(item.getUser());
                tvConcept.setText(item.getConcept());
                tvAmount.setText(item.getAmount());
            } else if (section.getColumnType() == TransactionSection.ColumnType.THREE_COLUMNS) {
                itemView = inflater.inflate(R.layout.item_transaction_three_columns, itemsContainer, false);

                TextView tvLabel = itemView.findViewById(R.id.tvLabel);
                TextView tvQuantity = itemView.findViewById(R.id.tvQuantity);
                TextView tvAmount = itemView.findViewById(R.id.tvAmount);

                tvLabel.setText(item.getLabel());
                tvQuantity.setText(item.getQuantity());
                tvAmount.setText(item.getAmount());
            } else {
                // DEFAULT usamos el mismo layout genérico
                itemView = inflater.inflate(R.layout.item_transaction, itemsContainer, false);

                TextView tvLabel = itemView.findViewById(R.id.tvLabel);
                TextView tvValue = itemView.findViewById(R.id.tvValue);

                tvLabel.setText(item.getLabel());
                // Usar serie si está disponible, sino usar amount
                String value = !item.getSerie().isEmpty() ? item.getSerie() : item.getAmount();
                tvValue.setText(value);
            }

            itemsContainer.addView(itemView);
        }

        // Configurar totales secundarios si existen
        if (section.getSecondaryTotals() != null && !section.getSecondaryTotals().isEmpty()) {
            totalContainer.setVisibility(View.GONE); // Ocultar el total principal
            secondaryTotalsContainer.setVisibility(View.VISIBLE);
            secondaryTotalsContainer.removeAllViews();

            // Agregar cada total secundario al contenedor dedicado
            for (com.example.cajaac.TransactionTotal total : section.getSecondaryTotals()) {
                View totalView;

                if (section.getColumnType() == TransactionSection.ColumnType.TABLE) {
                    // Para tabla, usar layout de total simplificado (solo label y monto)
                    totalView = inflater.inflate(R.layout.item_transaction_table_total, secondaryTotalsContainer, false);

                    TextView tvTotalLabel = totalView.findViewById(R.id.tvTotalLabel);
                    TextView tvTotalAmount = totalView.findViewById(R.id.tvTotalAmount);

                    tvTotalLabel.setText(total.getLabel());
                    tvTotalAmount.setText(total.getAmount());

                    totalView.setBackgroundColor(ContextCompat.getColor(getContext(), total.getBackgroundColor()));
                } else if (section.getColumnType() == TransactionSection.ColumnType.THREE_COLUMNS) {
                    totalView = inflater.inflate(R.layout.item_transaction_three_columns, secondaryTotalsContainer, false);

                    TextView tvLabel = totalView.findViewById(R.id.tvLabel);
                    TextView tvQuantity = totalView.findViewById(R.id.tvQuantity);
                    TextView tvAmount = totalView.findViewById(R.id.tvAmount);

                    tvLabel.setText(total.getLabel());
                    tvLabel.setTypeface(null, android.graphics.Typeface.BOLD);
                    tvQuantity.setText(total.getQuantity());
                    tvQuantity.setTypeface(null, android.graphics.Typeface.BOLD);
                    tvAmount.setText(total.getAmount());
                    tvAmount.setTypeface(null, android.graphics.Typeface.BOLD);

                    totalView.setBackgroundColor(ContextCompat.getColor(getContext(), total.getBackgroundColor()));
                    totalView.setPadding(10, 8, 10, 8);
                } else {
                    totalView = inflater.inflate(R.layout.item_transaction, secondaryTotalsContainer, false);

                    TextView tvLabel = totalView.findViewById(R.id.tvLabel);
                    TextView tvValue = totalView.findViewById(R.id.tvValue);

                    tvLabel.setText(total.getLabel());
                    tvLabel.setTypeface(null, android.graphics.Typeface.BOLD);
                    tvValue.setText(total.getAmount());
                    tvValue.setTypeface(null, android.graphics.Typeface.BOLD);

                    totalView.setBackgroundColor(ContextCompat.getColor(getContext(), total.getBackgroundColor()));
                    totalView.setPadding(10, 8, 10, 8);
                }

                secondaryTotalsContainer.addView(totalView);
            }
        } else {
            secondaryTotalsContainer.setVisibility(View.GONE);
        }

        // Configurar o ocultar la sección de total principal
        if (section.hasTotal() && (section.getSecondaryTotals() == null || section.getSecondaryTotals().isEmpty())) {
            totalContainer.setVisibility(View.VISIBLE);
            tvTotalLabel.setText(section.getTotalLabel());
            totalContainer.setBackgroundColor(
                    ContextCompat.getColor(getContext(), section.getTotalBackgroundColor())
            );

            // Mostrar columna de cantidad si es THREE_COLUMNS
            if (section.getColumnType() == TransactionSection.ColumnType.THREE_COLUMNS) {
                tvTotalQuantity.setVisibility(View.VISIBLE);
                tvTotalQuantity.setText(section.getTotalQuantity());
                tvTotalAmount.setText(section.getTotalAmount());
            } else {
                tvTotalQuantity.setVisibility(View.GONE);
                tvTotalAmount.setText(section.getTotalAmount());
            }
        } else if (!section.hasTotal()) {
            totalContainer.setVisibility(View.GONE);
        }
    }
}

