package com.example.cajaac.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.adapters.ExpandableItemAdapter;

import java.util.List;

public class ExpandableCardView extends LinearLayout {

    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvHeaderCol1;
    private TextView tvHeaderCol2;
    private TextView tvHeaderCol3;
    private TextView tvHeaderCol4;
    private RecyclerView rvItems;
    private ExpandableItemAdapter adapter;
    private LinearLayout totalContainer;
    private TextView tvTotalLabel;
    private TextView tvTotalAmount;
    private LinearLayout expandButton;
    private TextView tvExpandText;
    private ImageView ivExpandArrow;

    private boolean isExpanded = false;
    private int collapsedItemCount = 8; // Mostrar 8 items por defecto cuando está contraído
    private List<ExpandableItem> allItems;
    private boolean hasFourColumns = false;

    public ExpandableCardView(Context context) {
        super(context);
        init(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_expandable_card, this, true);

        ivIcon = findViewById(R.id.ivIcon);
        tvTitle = findViewById(R.id.tvTitle);
        tvHeaderCol1 = findViewById(R.id.tvHeaderCol1);
        tvHeaderCol2 = findViewById(R.id.tvHeaderCol2);
        tvHeaderCol3 = findViewById(R.id.tvHeaderCol3);
        tvHeaderCol4 = findViewById(R.id.tvHeaderCol4);
        rvItems = findViewById(R.id.rvItems);
        totalContainer = findViewById(R.id.totalContainer);
        tvTotalLabel = findViewById(R.id.tvTotalLabel);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        expandButton = findViewById(R.id.expandButton);
        tvExpandText = findViewById(R.id.tvExpandText);
        ivExpandArrow = findViewById(R.id.ivExpandArrow);

        // Configurar RecyclerView
        rvItems.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ExpandableItemAdapter();
        rvItems.setAdapter(adapter);

        expandButton.setOnClickListener(v -> toggleExpand());
    }

    public void setData(ExpandableCardData data) {
        // Configurar icono y título
        ivIcon.setImageResource(data.iconRes);
        ivIcon.setColorFilter(ContextCompat.getColor(getContext(), data.titleColor));
        tvTitle.setText(data.title);
        tvTitle.setTextColor(ContextCompat.getColor(getContext(), data.titleColor));

        // Configurar headers
        tvHeaderCol1.setText(data.headerCol1);
        tvHeaderCol2.setText(data.headerCol2);
        tvHeaderCol3.setText(data.headerCol3);

        // Configurar 4ta columna si existe
        this.hasFourColumns = data.headerCol4 != null && !data.headerCol4.isEmpty();
        if (hasFourColumns) {
            tvHeaderCol4.setText(data.headerCol4);
            tvHeaderCol4.setVisibility(View.VISIBLE);
        } else {
            tvHeaderCol4.setVisibility(View.GONE);
        }

        // Configurar total
        tvTotalLabel.setText(data.totalLabel);
        tvTotalAmount.setText(data.totalAmount);

        // Guardar todos los items
        this.allItems = data.items;
        this.collapsedItemCount = data.collapsedItemCount;

        // Mostrar items según estado
        displayItems(false);

        // Mostrar u ocultar botón expandir según cantidad de items
        if (allItems.size() <= collapsedItemCount) {
            expandButton.setVisibility(View.GONE);
        } else {
            expandButton.setVisibility(View.VISIBLE);
        }
    }

    private void displayItems(boolean showAll) {
        int itemsToShow = showAll ? allItems.size() : Math.min(collapsedItemCount, allItems.size());
        List<ExpandableItem> itemsToDisplay = allItems.subList(0, itemsToShow);
        adapter.submitList(new java.util.ArrayList<>(itemsToDisplay));
    }

    private void toggleExpand() {
        isExpanded = !isExpanded;
        displayItems(isExpanded);

        if (isExpanded) {
            tvExpandText.setText("Mostrar menos");
            ivExpandArrow.setRotation(180);
            totalContainer.setVisibility(View.VISIBLE);
        } else {
            tvExpandText.setText("Mostrar más");
            ivExpandArrow.setRotation(0);
            totalContainer.setVisibility(View.GONE);
        }
    }

    // Clase interna para los datos del card
    public static class ExpandableCardData {
        public int iconRes;
        public int titleColor;
        public String title;
        public String headerCol1;
        public String headerCol2;
        public String headerCol3;
        public String headerCol4;
        public String totalLabel;
        public String totalAmount;
        public List<ExpandableItem> items;
        public int collapsedItemCount = 8;

        public ExpandableCardData(int iconRes, int titleColor, String title) {
            this.iconRes = iconRes;
            this.titleColor = titleColor;
            this.title = title;
        }

        public ExpandableCardData setHeaders(String col1, String col2, String col3) {
            this.headerCol1 = col1;
            this.headerCol2 = col2;
            this.headerCol3 = col3;
            return this;
        }

        public ExpandableCardData setHeaders(String col1, String col2, String col3, String col4) {
            this.headerCol1 = col1;
            this.headerCol2 = col2;
            this.headerCol3 = col3;
            this.headerCol4 = col4;
            return this;
        }

        public ExpandableCardData setTotal(String label, String amount) {
            this.totalLabel = label;
            this.totalAmount = amount;
            return this;
        }

        public ExpandableCardData setItems(List<ExpandableItem> items) {
            this.items = items;
            return this;
        }

        public ExpandableCardData setCollapsedItemCount(int count) {
            this.collapsedItemCount = count;
            return this;
        }
    }

    // Clase interna para cada item
    public static class ExpandableItem {
        public String label;
        public String retention;
        public String amount;

        public ExpandableItem(String label, String retention, String amount) {
            this.label = label;
            this.retention = retention;
            this.amount = amount;
        }
    }
}
