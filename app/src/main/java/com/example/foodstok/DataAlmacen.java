package com.example.foodstok;

public class DataAlmacen {
    private byte[] foto;
    private String nombre;
    private String categoria;
    private String fechaFabricacion;
    private String fechaCaducidad;
    private int cantidad;
    private String idarticulo;
    private String AlmacenM;

    public DataAlmacen( String AlmacenM) {

        this.AlmacenM = AlmacenM;
    }

    public String getAlmacenM() {
        return AlmacenM;
    }

}