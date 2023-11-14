package com.example.foodstok;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class mostrarCategorias extends RecyclerView.Adapter<mostrarCategorias.ViewHolder> {

    private List<DataItem> dataItems;

    public mostrarCategorias(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public mostrarCategorias.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new mostrarCategorias.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mostrarCategorias.ViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);

        // Establece los datos en los elementos de la vista
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(dataItem.getFoto(), 0, dataItem.getFoto().length));
        holder.nombreTextView.setText("Nombre: "+dataItem.getNombre());
        holder.fabricacionTextView.setText("Fecha de fabricacion: "+dataItem.getFechaFabricacion());
        holder.caducidadTextView.setText("Fecha de caducidad: "+dataItem.getFechaCaducidad());
        holder.cantidadTextView.setText("Cantidad: "+String.valueOf(dataItem.getCantidad()));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nombreTextView;
        TextView fabricacionTextView;
        TextView caducidadTextView;
        TextView cantidadTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Obtén las referencias a los elementos de la vista
            imageView = itemView.findViewById(R.id.imageView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            fabricacionTextView = itemView.findViewById(R.id.fabricacionTextView);
            caducidadTextView = itemView.findViewById(R.id.caducidadTextView);
            cantidadTextView = itemView.findViewById(R.id.cantidadTextView);

        }
    }

}
