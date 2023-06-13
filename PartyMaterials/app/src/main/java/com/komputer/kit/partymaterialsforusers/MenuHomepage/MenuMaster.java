package com.komputer.kit.partymaterialsforusers.MenuHomepage;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuBarang;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuIdentitas;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuKategori;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuPelanggan;
import com.komputer.kit.partymaterialsforusers.R;

public class MenuMaster extends AppCompatActivity {
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_master);

        getSupportActionBar().setTitle("Master Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    public void keMenuIdentitas(View view) {
        Intent intent = new Intent(this, MenuIdentitas.class);
        startActivity(intent);
    }

    public void keMenuKategori(View view) {
        Intent intent = new Intent(this, MenuKategori.class);
        startActivity(intent);
    }

    public void keMenuBarang(View view) {
        db = new Database(this);
        String sql = "SELECT * FROM tblkelompok";
        Cursor cursor = db.select(sql);
        if (cursor.getCount() > 0){
            Intent intent = new Intent(this, MenuBarang.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Please insert category first!", Toast.LENGTH_SHORT).show();
        }

    }

    public void keMenuPelanggan(View view) {
        Intent intent = new Intent(this, MenuPelanggan.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
