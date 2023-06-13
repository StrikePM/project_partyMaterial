package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import androidx.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LaporanBarangAdapter extends RecyclerView.Adapter<LaporanBarangAdapter.ViewHolder> {

    Context context;
    List<ClassBarang> barangList;

    public LaporanBarangAdapter(Context context, List<ClassBarang> barangList){
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
        viewHolder.harga.setText(Function.removeE(barangList.get(i).getHarga()));
        viewHolder.brgKat.setText(barangList.get(i).getKelompok());
        viewHolder.optBarang.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nmBarang, harga, brgKat, optBarang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nmBarang = itemView.findViewById(R.id.brgNama);
            harga = itemView.findViewById(R.id.brgHarga);
            brgKat = itemView.findViewById(R.id.brgKat);
            optBarang = itemView.findViewById(R.id.optBarang);
        }
    }
}
