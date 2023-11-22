package com.example.foodstok;
public class DataItem {

    private byte[] foto;
    private String nombre;
    private String categoria;
    private String fechaFabricacion;
    private String fechaCaducidad;
    private int cantidad;
    private String idarticulo;

    public DataItem(byte[] foto, String nombre, String categoria, String fechaFabricacion, String fechaCaducidad, int cantidad,String idarticulo ) {
        this.foto = foto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaCaducidad = fechaCaducidad;
        this.cantidad = cantidad;
        this.idarticulo=idarticulo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getFechaFabricacion() {
        return fechaFabricacion;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public int getCantidad() {
        return cantidad;
    }
    public String getidarticulo() {
        return idarticulo;
    }


}