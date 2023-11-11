package com.example.foodstok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodstok.inventario.Inve_categorias;

import java.util.List;

public class categorias_adapter extends BaseAdapter {

    private Context context;
    private List<Inve_categorias> catList;

    public categorias_adapter(Context context, List<Inve_categorias> catList) {
        this.context = context;
        this.catList = catList;
    }

    @Override
    public int getCount() {
        return catList != null ? catList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_cat, viewGroup, false);

        TextView txtName = rootView.findViewById(R.id.name);
        ImageView image = rootView.findViewById(R.id.image);

        txtName.setText(catList.get(i).getName());
        image.setImageResource(catList.get(i).getImage());

        return rootView;
    }
}