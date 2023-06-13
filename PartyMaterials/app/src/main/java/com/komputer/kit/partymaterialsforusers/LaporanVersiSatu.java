package com.komputer.kit.partymaterialsforusers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.constraintlayout.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LaporanVersiSatu extends AppCompatActivity {

    String tipe;
    Database db;
    List<ClassBarang> barangList = new ArrayList<>();
    LaporanBarangAdapter adapterB;
    List<ClassPelanggan> pelangganList = new ArrayList<>();
    LaporanPelangganAdapter adapterP;
    private ConstraintLayout constraintLayout2;
    private EditText editLapSrc;
    private Button exportLapBar;
    private ConstraintLayout conLapBar;
    RecyclerView rcVerSat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_versi_satu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        load();
        initView();
        tipe = getIntent().getStringExtra("tipe");
        switch (tipe) {
            case "pelanggan":
                getSupportActionBar().setTitle("Customer Report");
                selectPelanggan("");

                editLapSrc.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        selectPelanggan(editLapSrc.getText().toString());
                    }
                });
                break;
            case "barang":
                getSupportActionBar().setTitle("Material Report");
                selectBarang("");

                editLapSrc.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        selectBarang(editLapSrc.getText().toString());
                    }
                });
                break;
        }
    }

    public void load() {
        db = new Database(this);

        rcVerSat = findViewById(R.id.rcVerSat);

        rcVerSat.setLayoutManager(new LinearLayoutManager(this));
        rcVerSat.setHasFixedSize(true);
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void selectBarang(String a) {
        String sql = "SELECT * FROM tbljasa WHERE jasa LIKE '%"+a+"%'";
        Cursor cursor = db.select(sql);
        barangList.clear();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String idkelompok = cursor.getString(cursor.getColumnIndex("idkelompok"));
                Cursor cur = db.select("SELECT * FROM tblkelompok WHERE idkelompok='"+idkelompok+"'");
                cur.moveToNext();

                String idjasa = cursor.getString(cursor.getColumnIndex("idjasa"));
                String jasa = cursor.getString(cursor.getColumnIndex("jasa"));
                String harga = cursor.getString(cursor.getColumnIndex("harga"));
                String kelompok = cur.getString(cur.getColumnIndex("kelompok"));

                barangList.add(new ClassBarang(idjasa,idkelompok,jasa,harga,kelompok));
            }

            adapterB = new LaporanBarangAdapter(this, barangList);
            rcVerSat.setAdapter(adapterB);
            adapterB.notifyDataSetChanged();
        }
    }

    public void selectPelanggan(String a){
        String sql = "SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+a+"%' OR alamat LIKE '%"+a+"%' OR nohp LIKE '%"+a+"%'";
        Cursor cursor = db.select(sql);
        pelangganList.clear();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String idpelanggan = cursor.getString(cursor.getColumnIndex("idpelanggan"));
                String pelanggan = cursor.getString(cursor.getColumnIndex("pelanggan"));
                String alamat = cursor.getString(cursor.getColumnIndex("alamat"));
                String nohp = cursor.getString(cursor.getColumnIndex("nohp"));

                pelangganList.add(new ClassPelanggan(idpelanggan, pelanggan, alamat, nohp));
            }

            adapterP = new LaporanPelangganAdapter(this, pelangganList);
            rcVerSat.setAdapter(adapterP);
            adapterP.notifyDataSetChanged();
        }
    }

    private void initView() {
        constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintLayout2);
        editLapSrc = (EditText) findViewById(R.id.editLapBar);
        exportLapBar = (Button) findViewById(R.id.exportLapBar);
        rcVerSat = (RecyclerView) findViewById(R.id.rcVerSat);


    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcel.class);
        i.putExtra("tipe",tipe) ;
        startActivity(i);
    }
}
