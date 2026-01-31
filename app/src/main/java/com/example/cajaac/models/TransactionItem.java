package com.example.cajaac.models;

public class TransactionItem {
    private String label;
    private String amount;
    private String serie;
    private String quantity;

    // Campos para tabla
    private String date;
    private String user;
    private String concept;
    private String receivedFrom; // Para tablas de 5 columnas

    // Campo para 4 columnas tipo delivery (promedio)
    private String average;

    // Campo para número de operaciones
    private String operations;

    // Constructor para 2 columnas (label, amount)
    public TransactionItem(String label, String amount) {
        this.label = label;
        this.amount = amount;
        this.serie = amount;  // También se puede usar como serie
        this.quantity = "";
        this.date = "";
        this.user = "";
        this.concept = "";
        this.receivedFrom = "";
        this.average = "";
        this.operations = "";
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
        this.receivedFrom = "";
        this.average = "";
        this.operations = "";
    }
    
    // Constructor para tabla de 4 columnas (date, user, concept, amount)
    public TransactionItem(String date, String user, String concept, String amount) {
        this.date = date;
        this.user = user;
        this.concept = concept;
        this.amount = amount;
        this.label = "";
        this.serie = "";
        this.quantity = "";
        this.receivedFrom = "";
        this.average = "";
        this.operations = "";
    }
    
    // Constructor para tabla de 5 columnas (date, user, receivedFrom, concept, amount)
    public TransactionItem(String date, String user, String receivedFrom, String concept, String amount) {
        this.date = date;
        this.user = user;
        this.receivedFrom = receivedFrom;
        this.concept = concept;
        this.amount = amount;
        this.label = "";
        this.serie = "";
        this.quantity = "";
        this.average = "";
        this.operations = "";
    }

    // Constructor para 4 columnas tipo delivery (label, operations, total, average)
    public static TransactionItem createFourColumns(String label, String operations, String total, String average) {
        TransactionItem item = new TransactionItem(label, total);
        item.operations = operations;
        item.average = average;
        return item;
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
    
    public String getReceivedFrom() {
        return receivedFrom;
    }

    public String getAverage() {
        return average;
    }

    public String getOperations() {
        return operations;
    }
}

