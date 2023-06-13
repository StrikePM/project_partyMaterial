package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuKategori;
import com.komputer.kit.partymaterialsforusers.Tambah.TambahKategori;

import java.util.List;

public class ClassKategoriAdapter extends RecyclerView.Adapter<ClassKategoriAdapter.ViewHolder>{

    Context context;
    List<ClassKategori> kategoriList;

    public ClassKategoriAdapter(Context context, List<ClassKategori> kategoriList) {
        this.context = context;
        this.kategoriList = kategoriList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kategori, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.nmKategori.setText(kategoriList.get(i).getKelompok());

        viewHolder.optKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.optKategori);
                popupMenu.inflate(R.menu.item_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.idUba:
                                Intent intent = new Intent(context, TambahKategori.class);
                                intent.putExtra("idkelompok", kategoriList.get(i).getIdkelompok());
                                intent.putExtra("kelompok", kategoriList.get(i).getKelompok());
                                context.startActivity(intent);
                                break;

                            case R.id.idHapus:
                                AlertDialog.Builder showAlert = new AlertDialog.Builder(context);
                                showAlert.setTitle("Delete Confirm");
                                showAlert.setMessage("Are you sure want to delete this data?");

                                showAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((MenuKategori)context).getKategori(kategoriList.get(i).getIdkelompok());
                                    }
                                });

                                showAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pesan("Okay!");
                                    }
                                });

                                showAlert.show();
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nmKategori, optKategori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nmKategori = itemView.findViewById(R.id.txtKategori);
            optKategori = itemView.findViewById(R.id.optKategori);
        }
    }


    public void pesan(String isi){
        Toast.makeText(context, isi, Toast.LENGTH_SHORT).show();
    }
}
