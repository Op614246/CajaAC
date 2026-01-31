package com.example.cajaac;

public class TransactionItem {
    private String label;
    private String amount;
    private String serie;
    private String quantity;

    // Campos para tabla
    private String date;
    private String user;
    private String concept;

    // Constructor para 2 columnas (label, amount)
    public TransactionItem(String label, String amount) {
        this.label = label;
        this.amount = amount;
        this.serie = amount;  // También se puede usar como serie
        this.quantity = "";
        this.date = "";
        this.user = "";
        this.concept = "";
    }

    // Constructor para 3 columnas (label, quantity, amount)
    public TransactionItem(String label, String quantity, String amount) {
        this.label = label;
        this.quantity = quantity;
        this.amount = amount;
        this.serie = "";
        this.date = "";
        this.user = "";
        this.concept = "";
    }

    // Constructor para tabla (date, user, concept, amount)
    public TransactionItem(String date, String user, String concept, String amount) {
        this.date = date;
        this.user = user;
        this.concept = concept;
        this.amount = amount;
        this.label = "";
        this.serie = "";
        this.quantity = "";
    }

    public String getLabel() {
        return label;
    }

    public String getAmount() {
        return amount;
    }

    public String getSerie() {
        return serie;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getConcept() {
        return concept;
    }
}

