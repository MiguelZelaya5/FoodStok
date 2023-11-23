package com.example.foodstok.datos_lista;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodstok.R;
import com.example.foodstok.RecyclerViewAdapter;

import java.util.ArrayList;

public class list_adapter extends RecyclerView.Adapter<list_adapter.ContactoViewHolder> {

    ArrayList<datos> listdatos;

    public list_adapter(ArrayList<datos> listdatos){
        this.listdatos = listdatos;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mostrar_lista, null, false);

        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        holder.viewNombre.setText(listdatos.get(position).getNombre());
        holder.viewcantidad.setText(listdatos.get(position).getCantidad());
    }

    @Override
    public int getItemCount() {
        return listdatos.size();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre, viewcantidad;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);

            //viewid = itemView.findViewById(R.id.viewid);
            viewNombre = itemView.findViewById(R.id.viewnombre);
            viewcantidad = itemView.findViewById(R.id.viewcantidad);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, edit_delete_datos.class);
                    intent.putExtra("ID", listdatos.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });*/
        }
    }
}
