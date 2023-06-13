package com.komputer.kit.partymaterialsforusers.Tambah;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuPelanggan;
import com.komputer.kit.partymaterialsforusers.R;

public class TambahPelanggan extends AppCompatActivity {

    TextInputEditText nmPelanggan, nmAlamat, noHp;
    Database db;
    String idPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pelanggan);

        db = new Database(this);

        getSupportActionBar().setTitle("Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nmPelanggan = findViewById(R.id.inputNamaPelanggan);
        nmAlamat = findViewById(R.id.inputAlamatPelanggan);
        noHp = findViewById(R.id.inputNoHpPelanggan);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            idPelanggan = null;
        } else {
            idPelanggan = extra.getString("idpelanggan");
            nmPelanggan.setText(extra.getString("pelanggan"));
            nmAlamat.setText(extra.getString("alamat"));
            noHp.setText(extra.getString("nohp"));
        }

    }

    public void simpan(View view) {
        String pelanggan = nmPelanggan.getText().toString();
        String alamat = nmAlamat.getText().toString();
        String nohp = noHp.getText().toString();

        if (pelanggan.isEmpty() || alamat.isEmpty() || nohp.isEmpty()) {
            pesan("please fill the emty text!");
        } else {

            if (idPelanggan == null) {
                if (db.runSql("INSERT INTO tblpelanggan (pelanggan, alamat, nohp) VALUES ('"+pelanggan+"', '"+alamat+"', "+nohp+")")) {
                    Intent intent = new Intent(TambahPelanggan.this, MenuPelanggan.class);
                    startActivity(intent);
                    pesan("Save Succed");
                } else {
                    pesan("Save Failed");
                }
            } else {
                if (db.runSql("UPDATE tblpelanggan SET  pelanggan = '"+pelanggan+"', alamat = '"+alamat+"', nohp = "+nohp+" WHERE idpelanggan = '"+idPelanggan+"'")) {
                    Intent intent = new Intent(TambahPelanggan.this, MenuPelanggan.class);
                    startActivity(intent);
                    pesan("Update Succed");
                } else {
                    pesan("Update Failed");
                }
            }
        }

        nmPelanggan.setText("");
        nmAlamat.setText("");
        noHp.setText("");

//        if (pelanggan.isEmpty()){
//            pesan("please fill the emty text!");
//        }else{
//            String sql = "INSERT INTO tblpelanggan (pelanggan, alamat, nohp) VALUES ( '"+pelanggan+"', '"+alamat+"', "+nohp+")";
//            Intent intent = new Intent(TambahPelanggan.this, MenuPelanggan.class);
//            startActivity(intent);
//            if (db.runSql(sql)){
//                pesan("Save Succed!");
//            }else {
//                pesan("Save Failed!");
//            }
//        }
//
//        nmPelanggan.setText("");
//        nmAlamat.setText("");
//        noHp.setText("");
    }

    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}
