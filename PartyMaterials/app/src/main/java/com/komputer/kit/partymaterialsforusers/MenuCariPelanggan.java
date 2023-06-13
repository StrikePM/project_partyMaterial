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

public class MenuCariPelanggan extends AppCompatActivity {

    Database db;
    List<ClassPelanggan> pelangganList = new ArrayList<ClassPelanggan>();
    private ConstraintLayout constraintLayout2;
    private EditText cari;
    private RecyclerView rcvWadah;
    TransaksiCariPelangganAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cari);

        getSupportActionBar().setTitle("Pelanggan");
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
        String sql = "SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+a+"%' OR alamat LIKE '%"+a+"%' OR nohp LIKE '%"+a+"%' ORDER BY pelanggan, alamat, nohp ASC";
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

            adapter = new TransaksiCariPelangganAdapter(this, pelangganList);
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
