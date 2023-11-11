package com.example.foodstok.inventario;
import java.io.Serializable;

public class Inve_categorias {
    private String name;
    private int image;

    public Inve_categorias() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
