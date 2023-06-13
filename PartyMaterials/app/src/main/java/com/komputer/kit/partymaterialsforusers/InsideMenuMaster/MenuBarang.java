package com.komputer.kit.partymaterialsforusers.InsideMenuMaster;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.ClassBarang;
import com.komputer.kit.partymaterialsforusers.ClassBarangAdapter;
import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.R;
import com.komputer.kit.partymaterialsforusers.Tambah.TambahBarang;

import java.util.ArrayList;
import java.util.List;

public class MenuBarang extends AppCompatActivity {

    Database db;

    List<ClassBarang> barangList = new ArrayList<ClassBarang>();
    ClassBarangAdapter adapter;
    RecyclerView rcvBarang;
    private ConstraintLayout constraintLayout2;
    private EditText namaBrng;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_barang);

        getSupportActionBar().setTitle("Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        load();
        selectData("");
        initView();
    }

    public void Tambah(View view) {
        Intent intent = new Intent(this, TambahBarang.class);
        startActivity(intent);
    }

    public void load() {
        db = new Database(this);

        rcvBarang = findViewById(R.id.rcvBarang);

        rcvBarang.setLayoutManager(new LinearLayoutManager(this));
        rcvBarang.setHasFixedSize(true);
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void selectData(String a) {
        String sql = "SELECT * FROM vjasa WHERE (jasa LIKE '%"+a+"%' OR harga LIKE '%"+a+"%' OR kelompok LIKE '%"+a+"%') ORDER BY idjasa ASC";
        adapter = new ClassBarangAdapter(this, barangList);
        rcvBarang.setAdapter(adapter);
        Cursor cursor = db.select(sql);
        barangList.clear();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String idjasa = cursor.getString(cursor.getColumnIndex("idjasa"));
                String idkelompok = cursor.getString(cursor.getColumnIndex("idkelompok"));
                String jasa = cursor.getString(cursor.getColumnIndex("jasa"));
                String harga = cursor.getString(cursor.getColumnIndex("harga"));
                String kelompok = cursor.getString(cursor.getColumnIndex("kelompok"));

                barangList.add(new ClassBarang(idjasa, idkelompok, jasa, harga, kelompok));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintLayout2);
        namaBrng = (EditText) findViewById(R.id.ccari);
        btnTambah = (Button) findViewById(R.id.btnTambah);

        namaBrng.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectData(s.toString());
            }
        });
    }

    public void deleteItemBarang(String id){
        String idBarang = id;
        String sql = "DELETE FROM tbljasa WHERE idjasa= "+idBarang+"";

        if (db.runSql(sql)){
            Toast.makeText(this, "Berhasil Hapus", Toast.LENGTH_SHORT).show();
            load();
            selectData("");
        }else {
            Toast.makeText(this, "Gagal hapus", Toast.LENGTH_SHORT).show();
        }

    }
}
