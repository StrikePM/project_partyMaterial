package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.constraintlayout.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.InsideMenuTransaksi.MenuOrder;

import java.util.List;

public class ViewRecyclerAdapter extends RecyclerView.Adapter<ViewRecyclerAdapter.ViewHolder>{

    Context context;
    List<ClassTransGetSet> detailList;

    public ViewRecyclerAdapter(Context context, List<ClassTransGetSet> detailList){
        this.context = context;
        this.detailList = detailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.brng.setText("Item : "+detailList.get(i).getIdOrder());
        viewHolder.hrg.setText("Price : "+Function.removeE(detailList.get(i).getHrg()));
        viewHolder.jmlh.setText("Amount : "+Function.removeE(detailList.get(i).getJmlh()));

        viewHolder.optOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder showAlert = new AlertDialog.Builder(context);
                showAlert.setTitle("Delete Confirm");
                showAlert.setMessage("Are you sure want to delete this data?");

                showAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MenuOrder)context).deleteItemOrder(detailList.get(i).getIdOrderDetail());
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
        return detailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView brng, jmlh, hrg, optOrder;
        ConstraintLayout wadahViewTransaksi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brng = itemView.findViewById(R.id.odrBrng);
            jmlh = itemView.findViewById(R.id.odrJumlah);
            hrg = itemView.findViewById(R.id.odrHarga);
            optOrder = itemView.findViewById(R.id.odrDelete);
        }
    }

    public void pesan(String isi){
        Toast.makeText(context, isi, Toast.LENGTH_SHORT).show();
    }
}
