package com.komputer.kit.partymaterialsforusers.MenuHomepage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.komputer.kit.partymaterialsforusers.LaporanVersiSatu;
import com.komputer.kit.partymaterialsforusers.LaporanVersiDua;
import com.komputer.kit.partymaterialsforusers.R;

public class MenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan);

        getSupportActionBar().setTitle("Report Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    public void laporanPelanggan(View view) {
        Intent intent = new Intent(this, LaporanVersiSatu.class);
        intent.putExtra("tipe", "pelanggan");
        startActivity(intent);
    }

    public void laporanBarang(View view) {
        Intent intent = new Intent(this, LaporanVersiSatu.class);
        intent.putExtra("tipe", "barang");
        startActivity(intent);
    }

    public void laporanPeminjaman(View view) {
        Intent intent = new Intent(this, LaporanVersiDua.class);
        intent.putExtra("tipe", "penjualan");
        startActivity(intent);
    }

    public void laporanPengembalian(View view) {
        Intent intent = new Intent(this, LaporanVersiDua.class);
        intent.putExtra("tipe", "pengembalian");
        startActivity(intent);
    }

    public void laporanPendapatan(View view) {
        Intent intent = new Intent(this, LaporanVersiDua.class);
        intent.putExtra("tipe", "pendapatan");
        startActivity(intent);
    }
}
