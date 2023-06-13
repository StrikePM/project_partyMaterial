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

import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuPelanggan;
import com.komputer.kit.partymaterialsforusers.Tambah.TambahPelanggan;

import java.util.List;

public class ClassPelangganAdapter extends RecyclerView.Adapter<ClassPelangganAdapter.ViewHolder> {

    Context context;
    List<ClassPelanggan> pelangganList;

    public ClassPelangganAdapter(Context context, List<ClassPelanggan> pelangganList) {
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.nmPelanggan.setText(pelangganList.get(i).getPelanggan());
        viewHolder.alamat.setText(pelangganList.get(i).getAlamat());
        viewHolder.nohp.setText(pelangganList.get(i).getNohp());

        viewHolder.optPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.optPelanggan);
                popupMenu.inflate(R.menu.item_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.idUba:
                                Intent intent = new Intent(context, TambahPelanggan.class);
                                intent.putExtra("idpelanggan", pelangganList.get(i).getIdpelanggan());
                                intent.putExtra("pelanggan", pelangganList.get(i).getPelanggan());
                                intent.putExtra("alamat", pelangganList.get(i).getAlamat());
                                intent.putExtra("nohp", pelangganList.get(i).getNohp());
                                context.startActivity(intent);
                                break;

                            case R.id.idHapus:
                                AlertDialog.Builder showAlert = new AlertDialog.Builder(context);
                                Database db = new Database(context);
                                Cursor cursor = db.select("SELECT * FROM vorderdetail WHERE idpelanggan = '"+pelangganList.get(i).getIdpelanggan()+"'");
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
                                        ((MenuPelanggan)context).deleteItemPelanggan(pelangganList.get(i).getIdpelanggan());
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

    public void pesan(String isi){
        Toast.makeText(context, isi, Toast.LENGTH_SHORT).show();
    }
}
