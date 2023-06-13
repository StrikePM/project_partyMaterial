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

import com.komputer.kit.partymaterialsforusers.ClassPelanggan;
import com.komputer.kit.partymaterialsforusers.ClassPelangganAdapter;
import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.R;
import com.komputer.kit.partymaterialsforusers.Tambah.TambahPelanggan;

import java.util.ArrayList;
import java.util.List;

public class MenuPelanggan extends AppCompatActivity {

    Database db;

    List<ClassPelanggan> pelangganList = new ArrayList<ClassPelanggan>();
    ClassPelangganAdapter adapter;
    RecyclerView rcvPelanggan;
    private ConstraintLayout constraintLayout2;
    private EditText namaPlgn;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pelanggan);

        getSupportActionBar().setTitle("Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        load();
        selectData("");
        initView();
    }

    public void Tambah(View view) {
        Intent intent = new Intent(this, TambahPelanggan.class);
        startActivity(intent);
    }

    public void load() {
        db = new Database(this);

        rcvPelanggan = findViewById(R.id.rcvPelanggan);

        rcvPelanggan.setLayoutManager(new LinearLayoutManager(this));
        rcvPelanggan.setHasFixedSize(true);
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void selectData(String a) {
        String sql = "SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+a+"%' OR alamat LIKE '%"+a+"%' OR nohp LIKE '%"+a+"%' ORDER BY pelanggan, alamat, nohp ASC";
        adapter = new ClassPelangganAdapter(this, pelangganList);
        rcvPelanggan.setAdapter(adapter);
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
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintLayout2);
        namaPlgn = (EditText) findViewById(R.id.ccari);
        btnTambah = (Button) findViewById(R.id.btnTambah);

        namaPlgn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectData(namaPlgn.getText().toString());
            }
        });
    }

    public void deleteItemPelanggan(String id){
        String idPelanggan = id;
        String sql = "DELETE FROM tblpelanggan WHERE idpelanggan= "+idPelanggan+"";

        if (db.runSql(sql)){
            Toast.makeText(this, "Delete Succed!", Toast.LENGTH_SHORT).show();
            load();
            selectData("");
        }else {
            Toast.makeText(this, "Delete Failed!", Toast.LENGTH_SHORT).show();
        }

    }
}
