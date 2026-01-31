package com.example.cajaac;

import java.util.List;

public class TransactionSection {

    public enum ColumnType {
        DEFAULT,
        THREE_COLUMNS
    }

    private String title;
    private int titleColor;
    private int iconRes;
    private List<TransactionItem> items;
    private String totalLabel;
    private String totalQuantity;
    private String totalAmount;
    private int totalBackgroundColor;
    private boolean hasTotal;
    private ColumnType columnType;

    // Constructor completo
    public TransactionSection(String title, int titleColor, int iconRes,
                              List<TransactionItem> items, String totalLabel,
                              String totalQuantity, String totalAmount,
                              int totalBackgroundColor,
                              boolean hasTotal, ColumnType columnType) {
        this.title = title;
        this.titleColor = titleColor;
        this.iconRes = iconRes;
        this.items = items;
        this.totalLabel = totalLabel;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.totalBackgroundColor = totalBackgroundColor;
        this.hasTotal = hasTotal;
        this.columnType = columnType;
    }

    // Constructor sin total (para compatibilidad)
    public TransactionSection(String title, int titleColor, int iconRes,
                              List<TransactionItem> items, boolean hasTotal,
                              ColumnType columnType) {
        this.title = title;
        this.titleColor = titleColor;
        this.iconRes = iconRes;
        this.items = items;
        this.hasTotal = hasTotal;
        this.columnType = columnType;
        this.totalLabel = "";
        this.totalQuantity = "";
        this.totalAmount = "";
        this.totalBackgroundColor = android.R.color.transparent;
    }

    // Constructor original (para compatibilidad)
    public TransactionSection(String title, int titleColor, int iconRes,
                              List<TransactionItem> items, String totalLabel,
                              String totalAmount, int totalBackgroundColor) {
        this(title, titleColor, iconRes, items, totalLabel, "", totalAmount,
             totalBackgroundColor, true, ColumnType.DEFAULT);
    }

    // Getters
    public String getTitle() { return title; }
    public int getTitleColor() { return titleColor; }
    public int getIconRes() { return iconRes; }
    public List<TransactionItem> getItems() { return items; }
    public String getTotalLabel() { return totalLabel; }
    public String getTotalQuantity() { return totalQuantity; }
    public String getTotalAmount() { return totalAmount; }
    public int getTotalBackgroundColor() { return totalBackgroundColor; }
    public boolean hasTotal() { return hasTotal; }
    public ColumnType getColumnType() { return columnType; }
}
