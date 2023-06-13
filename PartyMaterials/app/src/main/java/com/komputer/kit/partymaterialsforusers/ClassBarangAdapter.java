package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuBarang;
import com.komputer.kit.partymaterialsforusers.Tambah.TambahBarang;

import java.util.List;

public class ClassBarangAdapter extends RecyclerView.Adapter<ClassBarangAdapter.ViewHolder>{

    Context context;
    List<ClassBarang> barangList;

    public ClassBarangAdapter(Context context, List<ClassBarang> barangList) {
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.nmBarang.setText(barangList.get(i).getBarang());
        viewHolder.harga.setText(Function.removeE(barangList.get(i).getHarga()));
        viewHolder.kelompok.setText(barangList.get(i).getKelompok());

        viewHolder.optBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.optBarang);
                popupMenu.inflate(R.menu.item_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.idUba:
                                Intent intent = new Intent(context, TambahBarang.class);
                                intent.putExtra("idjasa", barangList.get(i).getIdjasa());
                                intent.putExtra("idkelompok", barangList.get(i).getIdkelompok());
                                intent.putExtra("jasa", barangList.get(i).getBarang());
                                intent.putExtra("harga", barangList.get(i).getHarga());
                                context.startActivity(intent);
                                break;
                            case R.id.idHapus:
                                AlertDialog.Builder showAlert = new AlertDialog.Builder(context);
                                Database db = new Database(context);
                                Cursor cursor = db.select("SELECT * FROM vorderdetail WHERE idjasa = '"+barangList.get(i).getIdjasa()+"'");
                                if (cursor.getCount() > 0){
                                    showAlert.setTitle("Delete Item (!!!WARNING!!!)");
                                    showAlert.setMessage("WARNING!!! Your income in report will be deleted too!");
                                } else {
                                    showAlert.setTitle("Delete Confirm");
                                    showAlert.setMessage("Are you sure want to delete this data?");
                                }

                                showAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((MenuBarang)context).deleteItemBarang(barangList.get(i).getIdjasa());
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
        return barangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nmBarang, harga, kelompok, optBarang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nmBarang = itemView.findViewById(R.id.brgNama);
            harga = itemView.findViewById(R.id.brgHarga);
            kelompok = itemView.findViewById(R.id.brgKat);
            optBarang = itemView.findViewById(R.id.optBarang);
        }
    }

    public void pesan(String isi){
        Toast.makeText(context, isi, Toast.LENGTH_SHORT).show();
    }
}
