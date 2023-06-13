package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CariBarangAdapter extends RecyclerView.Adapter<CariBarangAdapter.ViewHolder>{

    Context context;
    List<ClassBarang> barangList;

    public CariBarangAdapter(Context context, List<ClassBarang> barangList) {
        this.context = context;
        this.barangList = barangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_barang, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nmBarang.setText(barangList.get(i).getBarang());
        viewHolder.harga.setText(barangList.get(i).getHarga());
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nmBarang, harga, optBarang;
        ConstraintLayout wadahBarang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wadahBarang = itemView.findViewById(R.id.wadahBarang);
            nmBarang = itemView.findViewById(R.id.brgNama);
            harga = itemView.findViewById(R.id.brgHarga);
            optBarang = itemView.findViewById(R.id.optBarang);
        }
    }
}

