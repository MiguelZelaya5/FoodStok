package com.example.foodstok;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<DataItem> dataItems;

    public RecyclerViewAdapter(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);

        // Establece los datos en los elementos de la vista
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(dataItem.getFoto(), 0, dataItem.getFoto().length));
        holder.nombreTextView.setText(dataItem.getNombre());
        holder.categoriaTextView.setText(dataItem.getCategoria());
        holder.fabricacionTextView.setText(dataItem.getFechaFabricacion());
        holder.caducidadTextView.setText(dataItem.getFechaCaducidad());
        holder.cantidadTextView.setText(String.valueOf(dataItem.getCantidad()));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nombreTextView;
        TextView categoriaTextView;
        TextView fabricacionTextView;
        TextView caducidadTextView;
        TextView cantidadTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Obtén las referencias a los elementos de la vista
            imageView = itemView.findViewById(R.id.imageView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            categoriaTextView = itemView.findViewById(R.id.categoriaTextView);
            fabricacionTextView = itemView.findViewById(R.id.fabricacionTextView);
            caducidadTextView = itemView.findViewById(R.id.caducidadTextView);
            cantidadTextView = itemView.findViewById(R.id.cantidadTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, LlenarProducto.class);
                    intent.putExtra("Id", dataItems.get(getAdapterPosition()).getidarticulo());
                    context.startActivity(intent);
                }
            });
        }
    }
}