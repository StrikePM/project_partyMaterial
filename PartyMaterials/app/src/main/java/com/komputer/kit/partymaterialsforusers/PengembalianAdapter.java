package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.InsideMenuTransaksi.MenuPengembalian;

import java.util.List;

public class PengembalianAdapter extends RecyclerView.Adapter<PengembalianAdapter.ViewHolder> {

    Context context;
    List<ClassPengembalian> pengembalianList;

    public PengembalianAdapter(Context context, List<ClassPengembalian> pengembalianList){
        this.context = context;
        this.pengembalianList = pengembalianList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pengembalian, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.fktr.setText(pengembalianList.get(i).getFaktur());
        viewHolder.tgl.setText(pengembalianList.get(i).getTanggal());
        viewHolder.plg.setText(pengembalianList.get(i).getPelanggan());
        viewHolder.tlp.setText(pengembalianList.get(i).getNophone());
        viewHolder.brg.setText(pengembalianList.get(i).getBarang()+"\n"+pengembalianList.get(i).getJumlah()+" Item");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder showAlert = new AlertDialog.Builder(context);
                showAlert.setTitle("Return Confirm");
                showAlert.setMessage("Are you want to return item?");

                showAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database db = new Database(context);

                        if (db.runSql("UPDATE tblorder SET status=2 WHERE faktur='"+pengembalianList.get(i).getFaktur()+"'")) {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            ((MenuPengembalian)context).load();
                            ((MenuPengembalian)context).selectData("");
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                showAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pesan("Okay!");
                    }
                });

                showAlert.show();
            }
        });

        viewHolder.optPeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder showAlert = new AlertDialog.Builder(context);
                showAlert.setTitle("Delete Confirm");
                showAlert.setMessage("Are you want to delete item?");

                showAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MenuPengembalian)context).deleteItemPengembalian(pengembalianList.get(i).getFaktur());
                    }
                });

                showAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pesan("Okay!");
                    }
                });

                showAlert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pengembalianList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView fktr, tgl, plg, brg, tlp, optPeng;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fktr = itemView.findViewById(R.id.txtPenFak);
            tgl = itemView.findViewById(R.id.txtPenTgl);
            plg = itemView.findViewById(R.id.txtPenNam);
            brg = itemView.findViewById(R.id.txtPenBar);
            tlp = itemView.findViewById(R.id.txtPenTlp);
            optPeng = itemView.findViewById(R.id.optPengembalian);
        }
    }

    public void pesan(String isi){
        Toast.makeText(context, isi, Toast.LENGTH_SHORT).show();
    }
}
