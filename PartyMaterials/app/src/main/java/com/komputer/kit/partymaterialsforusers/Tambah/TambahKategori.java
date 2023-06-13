package com.komputer.kit.partymaterialsforusers.Tambah;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuKategori;
import com.komputer.kit.partymaterialsforusers.R;

public class TambahKategori extends AppCompatActivity {

    TextInputEditText nmKategori;
    Database db;
    String idKelompok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kategori);

        db = new Database(this);

        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nmKategori = findViewById(R.id.inputKategori);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            idKelompok = null;
        } else {
            idKelompok = extra.getString("idkelompok");
            nmKategori.setText(extra.getString("kelompok"));
        }

    }

    public void simpan(View view) {
        String kelompok = nmKategori.getText().toString();

        if (kelompok.isEmpty()) {
            pesan("please fill the emty text!");
        } else {

            if (idKelompok == null) {
                if (db.runSql("INSERT INTO tblkelompok (kelompok) VALUES ('"+kelompok+"')")) {
                    Intent intent = new Intent(TambahKategori.this, MenuKategori.class);
                    startActivity(intent);
                    pesan("Save Succed");
                } else {
                    pesan("Save Failed");
                }
            } else {
                if (db.runSql("UPDATE tblkelompok SET kelompok = '"+kelompok+"' WHERE idkelompok = '"+idKelompok+"'")) {
                    Intent intent = new Intent(TambahKategori.this, MenuKategori.class);
                    startActivity(intent);
                    pesan("Updaate Succed");
                } else {
                    pesan("Update Failed");
                }
            }
        }

        nmKategori.setText("");


//        String kategori = nmKategori.getText().toString();
//
//        if (kategori.isEmpty()){
//            pesan("please fill the emty text!");
//        }else{
//            String sql = "INSERT INTO tblkelompok (kelompok) VALUES ('"+kategori+"')";
//            Intent intent = new Intent(TambahKategori.this, MenuKategori.class);
//            startActivity(intent);
//            if (db.runSql(sql)){
//                pesan("Save Succed!");
//            }else {
//                pesan("Save Failed!");
//            }
//        }
//
//        nmKategori.setText("");

    }
    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}
