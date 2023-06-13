package com.komputer.kit.partymaterialsforusers;

import android.database.Cursor;
import android.os.Bundle;
import androidx.constraintlayout.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MenuCariBarang extends AppCompatActivity {

    Database db;
    List<ClassBarang> barangList = new ArrayList<ClassBarang>();
    private ConstraintLayout constraintLayout2;
    private EditText cari;
    private RecyclerView rcvWadah;
    TransaksiCariBarangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cari);

        getSupportActionBar().setTitle("Barang");
        getSupportActionBar().setElevation(0);

        load();
        selectData("");
        initView();
    }

    public void load(){
        db = new Database(this);

        rcvWadah = (RecyclerView) findViewById(R.id.rcvWadah);

        rcvWadah.setLayoutManager(new LinearLayoutManager(this));
        rcvWadah.setHasFixedSize(true);
    }

    public void selectData(String a) {
        String sql = "SELECT * FROM tbljasa WHERE jasa LIKE '%" + a + "%' ORDER BY jasa ASC";
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

                barangList.add(new ClassBarang(idjasa, idkelompok, jasa, harga, kelompok));
            }

            adapter = new TransaksiCariBarangAdapter(this, barangList);
            rcvWadah.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintLayout2);
        cari = (EditText) findViewById(R.id.ccari);

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectData(cari.getText().toString());
            }
        });
    }
}
