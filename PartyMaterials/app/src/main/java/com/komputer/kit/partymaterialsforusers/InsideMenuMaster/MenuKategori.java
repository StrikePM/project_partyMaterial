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

import com.komputer.kit.partymaterialsforusers.ClassKategori;
import com.komputer.kit.partymaterialsforusers.ClassKategoriAdapter;
import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.R;
import com.komputer.kit.partymaterialsforusers.Tambah.TambahKategori;

import java.util.ArrayList;
import java.util.List;

public class MenuKategori extends AppCompatActivity {

    Database db;

    List<ClassKategori> kategoriList = new ArrayList<ClassKategori>();
    ClassKategoriAdapter adapter;
    RecyclerView rcvKategori;
    private ConstraintLayout constraintLayout2;
    private EditText namaKtgr;
    private Button btnTambah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kategori);

        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        load();
        selectData("");
        initView();
    }

    public void Tambah(View view) {
        Intent intent = new Intent(this, TambahKategori.class);
        startActivity(intent);
    }

    public void load() {
        db = new Database(this);

        rcvKategori = findViewById(R.id.rcvKategori);

        rcvKategori.setLayoutManager(new LinearLayoutManager(this));
        rcvKategori.setHasFixedSize(true);
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void selectData(String a) {
        String sql = "SELECT * FROM tblkelompok WHERE kelompok LIKE '%"+a+"%' ORDER BY kelompok ASC";
        Cursor cursor = db.select(sql);
        kategoriList.clear();
        adapter = new ClassKategoriAdapter(this, kategoriList);
        rcvKategori.setAdapter(adapter);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String idkelompok = cursor.getString(cursor.getColumnIndex("idkelompok"));
                String kelompok = cursor.getString(cursor.getColumnIndex("kelompok"));

                kategoriList.add(new ClassKategori(idkelompok, kelompok));
            }

        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintLayout2);
        namaKtgr = (EditText) findViewById(R.id.ccari);
        btnTambah = (Button) findViewById(R.id.btnTambah);

        namaKtgr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectData(namaKtgr.getText().toString());
            }
        });
    }

    public void getKategori(String id) {
        db= new Database(this);
        String sql = "SELECT * FROM tbljasa WHERE idkelompok = "+id+"";
        Cursor cursor = db.select(sql);
        if (cursor.getCount() > 0) {
            Toast.makeText(this, "This category is used in another Menu, delete it first then comeback here!", Toast.LENGTH_LONG).show();
        } else {
            db = new Database(this);
            String sql2 = "DELETE FROM tblkelompok WHERE idkelompok = "+id+"";
            if (db.runSql(sql2)) {
                Toast.makeText(this, "Delete Succed!", Toast.LENGTH_SHORT).show();
                load();
                selectData("");
            }
        }
    }
}