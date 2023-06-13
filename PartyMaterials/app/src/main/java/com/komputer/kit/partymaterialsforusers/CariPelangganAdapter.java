package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import androidx.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CariPelangganAdapter extends RecyclerView.Adapter<CariPelangganAdapter.ViewHolder> {

    Context context;
    List<ClassPelanggan> pelangganList;

    public CariPelangganAdapter(Context context, List<ClassPelanggan> pelangganList) {
        this.context = context;
        this.pelangganList = pelangganList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pelanggan, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.nmPelanggan.setText(pelangganList.get(i).getPelanggan());
        viewHolder.alamat.setText(pelangganList.get(i).getAlamat());
        viewHolder.nohp.setText(pelangganList.get(i).getNohp());
    }

    @Override
    public int getItemCount() {
        return pelangganList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nmPelanggan, alamat, nohp, optPelanggan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nmPelanggan = itemView.findViewById(R.id.txtNama);
            alamat = itemView.findViewById(R.id.txtAlamat);
            nohp = itemView.findViewById(R.id.txtTlp);
            optPelanggan = itemView.findViewById(R.id.optPelanggan);

        }
    }
}
