package com.example.cajaac;

import java.util.List;

public class TransactionSection {
    private String title;
    private int titleColor;
    private int iconRes;
    private List<TransactionItem> items;
    private String totalLabel;
    private String totalAmount;
    private int totalBackgroundColor;

    public TransactionSection(String title, int titleColor, int iconRes,
                              List<TransactionItem> items, String totalLabel,
                              String totalAmount, int totalBackgroundColor) {
        this.title = title;
        this.titleColor = titleColor;
        this.iconRes = iconRes;
        this.items = items;
        this.totalLabel = totalLabel;
        this.totalAmount = totalAmount;
        this.totalBackgroundColor = totalBackgroundColor;
    }

    // Getters
    public String getTitle() { return title; }
    public int getTitleColor() { return titleColor; }
    public int getIconRes() { return iconRes; }
    public List<TransactionItem> getItems() { return items; }
    public String getTotalLabel() { return totalLabel; }
    public String getTotalAmount() { return totalAmount; }
    public int getTotalBackgroundColor() { return totalBackgroundColor; }
}
