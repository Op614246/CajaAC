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
    private TextView tvTotalLabel;
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
        tvTotalLabel = findViewById(R.id.tvTotalLabel);
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

        // Agregar items dinámicamente
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (TransactionItem item : section.getItems()) {
            View itemView = inflater.inflate(R.layout.item_transaction, itemsContainer, false);

            TextView tvLabel = itemView.findViewById(R.id.tvLabel);
            TextView tvAmount = itemView.findViewById(R.id.tvAmount);

            tvLabel.setText(item.getLabel());
            tvAmount.setText(item.getAmount());

            itemsContainer.addView(itemView);
        }

        // Configurar total
        tvTotalLabel.setText(section.getTotalLabel());
        tvTotalAmount.setText(section.getTotalAmount());
        totalContainer.setBackgroundColor(
                ContextCompat.getColor(getContext(), section.getTotalBackgroundColor())
        );
    }
}

