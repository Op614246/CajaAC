package com.example.cajaac.models;

public class ProductoEstrellaItem {
    private String ranking;
    private String letra;
    private String nombre;
    private String categoria;
    private String ventas;
    private String porcentaje;
    private int colorPorcentaje;

    public ProductoEstrellaItem(String ranking, String letra, String nombre, String categoria,
                                String ventas, String porcentaje, int colorPorcentaje) {
        this.ranking = ranking;
        this.letra = letra;
        this.nombre = nombre;
        this.categoria = categoria;
        this.ventas = ventas;
        this.porcentaje = porcentaje;
        this.colorPorcentaje = colorPorcentaje;
    }

    public String getRanking() {
        return ranking;
    }

    public String getLetra() {
        return letra;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getVentas() {
        return ventas;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public int getColorPorcentaje() {
        return colorPorcentaje;
    }
}

