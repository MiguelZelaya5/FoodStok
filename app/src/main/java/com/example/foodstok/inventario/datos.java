package com.example.foodstok.inventario;

import com.example.foodstok.R;

import java.util.ArrayList;
import java.util.List;

public class datos {
    public static List<Inve_categorias> getCatList() {
        List<Inve_categorias> catList = new ArrayList<>();

        Inve_categorias carnes = new Inve_categorias();
        carnes.setName("Carnes");
        carnes.setImage(R.drawable.carne);
        catList.add(carnes);

        Inve_categorias lacteos = new Inve_categorias();
        lacteos.setName("Lacteos");
        lacteos.setImage(R.drawable.lacteos);
        catList.add(lacteos);

        Inve_categorias vegetales = new Inve_categorias();
        vegetales.setName("Vegetales");
        vegetales.setImage(R.drawable.vegetales);
        catList.add(vegetales);

        Inve_categorias fruta = new Inve_categorias();
        fruta.setName("Fruta");
        fruta.setImage(R.drawable.frutas);
        catList.add(fruta);

        Inve_categorias aderezo = new Inve_categorias();
        aderezo.setName("Aderezo");
        aderezo.setImage(R.drawable.aderezos);
        catList.add(aderezo);

        return catList;
    }
}
