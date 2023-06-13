package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komputer.kit.partymaterialsforusers.InsideMenuTransaksi.MenuOrder;

import java.util.List;

public class TransaksiCariBarangAdapter extends RecyclerView.Adapter<TransaksiCariBarangAdapter.ViewHolder> {

    Context context;
    List<ClassBarang> barangList;

    public TransaksiCariBarangAdapter(Context context, List<ClassBarang> barangList){
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.nmBarang.setText(barangList.get(i).getBarang());
        viewHolder.harga.setText(Modul.removeE(barangList.get(i).getHarga()));
        viewHolder.brgKat.setText(barangList.get(i).getKelompok());

        viewHolder.wadahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuOrder.class);

                intent.putExtra("barang", barangList.get(i).getBarang());
                intent.putExtra("idbarang", barangList.get(i).getIdjasa());
                intent.putExtra("harga", barangList.get(i).getHarga());
                ((MenuCariBarang)context).setResult(2, intent);
                ((MenuCariBarang)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nmBarang, harga, brgKat, optBarang;
        ConstraintLayout wadahBarang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wadahBarang = itemView.findViewById(R.id.wadahBarang);
            nmBarang = itemView.findViewById(R.id.brgNama);
            harga = itemView.findViewById(R.id.brgHarga);
            brgKat = itemView.findViewById(R.id.brgKat);
            optBarang = itemView.findViewById(R.id.optBarang);
            optBarang.setVisibility(View.INVISIBLE);
        }
    }
}
