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

public class AlmcacenData extends RecyclerView.Adapter<AlmcacenData.ViewHolder> {
    private List<DataAlmacen> dataalmacen;

    public AlmcacenData(List<DataAlmacen> dataalmacen) {
        this.dataalmacen = dataalmacen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostralnalmacen, parent, false);
        return new AlmcacenData.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AlmcacenData.ViewHolder holder, int position) {
        DataAlmacen dataalmace = dataalmacen.get(position);
        holder.nombreTextView.setText(dataalmace.getAlmacenM());

    }

    @Override
    public int getItemCount() {
        return dataalmacen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTextView = itemView.findViewById(R.id.nombreTextView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int position = getBindingAdapterPosition(); // O getAbsoluteAdapterPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, FiltroAlmacen.class);
                        intent.putExtra("nombreAlmacen", dataalmacen.get(position).getAlmacenM());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

