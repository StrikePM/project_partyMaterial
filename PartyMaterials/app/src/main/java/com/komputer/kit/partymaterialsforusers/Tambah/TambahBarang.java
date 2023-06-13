package com.komputer.kit.partymaterialsforusers.Tambah;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuBarang;
import com.komputer.kit.partymaterialsforusers.R;

import java.util.ArrayList;

public class TambahBarang extends AppCompatActivity {

    TextInputEditText nmBarang, nmHarga;
    Database db;
    Spinner sprBarang;
    ArrayList arrayKategori = new ArrayList();
    ArrayList arrayId = new ArrayList();
    String idBarang,idKelompok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        db = new Database(this);

        getSupportActionBar().setTitle("Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nmBarang = findViewById(R.id.inputNamaBarang);
        sprBarang = findViewById(R.id.sprBarang);
        nmHarga = findViewById(R.id.inputHargaBarang);

        setBarang();

        Bundle extra = getIntent().getExtras();
        if (extra==null){
            idBarang = null;
        }else {
            idBarang = extra.getString("idjasa");
            nmBarang.setText(extra.getString("jasa"));
            nmHarga.setText(extra.getString("harga"));
            idKelompok = extra.getString("idkelompok");

            sprBarang.setSelection(db.select("SELECT * FROM tblkelompok WHERE idkelompok<"+idKelompok+" ORDER BY kelompok ASC").getCount());
        }
    }

    public void simpanBarang(View view) {
        String barang = nmBarang.getText().toString();
        String idKelompok = arrayId.get(sprBarang.getSelectedItemPosition()).toString();
        String harga = nmHarga.getText().toString();

        if (barang.isEmpty() || harga.isEmpty()){
            pesan("please fill the emty text!");
        }else{

            if (idBarang==null){
                if (db.runSql("INSERT INTO tbljasa (idkelompok, jasa, harga) VALUES ("+idKelompok+", '"+barang+"', "+harga+")")){
                    Intent intent = new Intent(TambahBarang.this, MenuBarang.class);
                    startActivity(intent);
                    pesan("Save Succed");
                }else {
                    pesan("Save Failed");
                }
            }else {
                if (db.runSql("UPDATE tbljasa SET idkelompok="+idKelompok+",jasa='"+barang+"',harga="+harga+" WHERE idjasa="+idBarang+"")){
                    Intent intent = new Intent(TambahBarang.this, MenuBarang.class);
                    startActivity(intent);
                    pesan("Updaate Succed");
                }else {
                    pesan("Update Failed");
                }
            }
        }

        nmBarang.setText("");
        nmHarga.setText("");
    }

    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void setBarang(){
        String sql = "SELECT * FROM tblkelompok ORDER BY kelompok ASC";
        Cursor cursor = db.select(sql);
        arrayKategori.clear();
        arrayId.clear();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String idkelompok = cursor.getString(cursor.getColumnIndex("idkelompok"));
                String kelompok = cursor.getString(cursor.getColumnIndex("kelompok"));

                arrayKategori.add(kelompok);
                arrayId.add(idkelompok);

            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayKategori);
            sprBarang.setAdapter(adapter);
        }
    }
}
