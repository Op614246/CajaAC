package com.example.cajaac.models;

/**
 * Modelo de datos para la información principal de la caja
 */
public class DatosCaja {
    private String numeroCaja;
    private String totalCaja;
    private String nombreUsuario;
    private String montoApertura;
    private String turno;
    private String fechaApertura;
    private String fechaResumen;
    private String montoEfectivo;
    private String montoTarjetas;

    // Constructor
    public DatosCaja() {
    }

    public DatosCaja(String numeroCaja, String totalCaja, String nombreUsuario,
                     String montoApertura, String turno, String fechaApertura,
                     String fechaResumen, String montoEfectivo, String montoTarjetas) {
        this.numeroCaja = numeroCaja;
        this.totalCaja = totalCaja;
        this.nombreUsuario = nombreUsuario;
        this.montoApertura = montoApertura;
        this.turno = turno;
        this.fechaApertura = fechaApertura;
        this.fechaResumen = fechaResumen;
        this.montoEfectivo = montoEfectivo;
        this.montoTarjetas = montoTarjetas;
    }

    // Getters y Setters
    public String getNumeroCaja() {
        return numeroCaja;
    }

    public void setNumeroCaja(String numeroCaja) {
        this.numeroCaja = numeroCaja;
    }

    public String getTotalCaja() {
        return totalCaja;
    }

    public void setTotalCaja(String totalCaja) {
        this.totalCaja = totalCaja;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getMontoApertura() {
        return montoApertura;
    }

    public void setMontoApertura(String montoApertura) {
        this.montoApertura = montoApertura;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getFechaResumen() {
        return fechaResumen;
    }

    public void setFechaResumen(String fechaResumen) {
        this.fechaResumen = fechaResumen;
    }

    public String getMontoEfectivo() {
        return montoEfectivo;
    }

    public void setMontoEfectivo(String montoEfectivo) {
        this.montoEfectivo = montoEfectivo;
    }

    public String getMontoTarjetas() {
        return montoTarjetas;
    }

    public void setMontoTarjetas(String montoTarjetas) {
        this.montoTarjetas = montoTarjetas;
    }
}
