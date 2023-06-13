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

public class TransaksiCariPelangganAdapter extends RecyclerView.Adapter<TransaksiCariPelangganAdapter.ViewHolder> {
    
    Context context;
    List<ClassPelanggan> pelangganList;

    public  TransaksiCariPelangganAdapter(Context context, List<ClassPelanggan> pelangganList){
        this.context = context;
        this.pelangganList = pelangganList;
    }
    
    @NonNull
    @Override
    public TransaksiCariPelangganAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pelanggan, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiCariPelangganAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.nmPelanggan.setText(pelangganList.get(i).getPelanggan());
        viewHolder.alamat.setText(pelangganList.get(i).getAlamat());
        viewHolder.nohp.setText(pelangganList.get(i).getNohp());
        viewHolder.optPelaggan.setVisibility(View.GONE);

        viewHolder.wadahPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuOrder.class);

                intent.putExtra("pelanggan", pelangganList.get(i).getPelanggan());
                intent.putExtra("idpelanggan", pelangganList.get(i).getIdpelanggan());
                ((MenuCariPelanggan)context).setResult(1, intent);
                ((MenuCariPelanggan) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pelangganList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nmPelanggan, alamat, nohp, optPelaggan;
        ConstraintLayout wadahPelanggan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wadahPelanggan = itemView.findViewById(R.id.wadahPelanggan);
            nmPelanggan = itemView.findViewById(R.id.txtNama);
            alamat = itemView.findViewById(R.id.txtAlamat);
            nohp = itemView.findViewById(R.id.txtTlp);
            optPelaggan = itemView.findViewById(R.id.optPelanggan);
        }
    }
}
