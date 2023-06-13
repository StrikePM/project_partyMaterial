package com.komputer.kit.partymaterialsforusers.MenuHomepage;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuBarang;
import com.komputer.kit.partymaterialsforusers.InsideMenuTransaksi.MenuOrder;
import com.komputer.kit.partymaterialsforusers.InsideMenuTransaksi.MenuPengembalian;
import com.komputer.kit.partymaterialsforusers.MainHomepage;
import com.komputer.kit.partymaterialsforusers.R;

public class MenuTransaksi extends AppCompatActivity {
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi);

        getSupportActionBar().setTitle("Transaction Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    public void keMenuOrder(View view) {
        db = new Database(this);
        String sql = "SELECT * FROM tblpelanggan";
        String sql2 = "SELECT * FROM tbljasa";
        Cursor cursor = db.select(sql);
        Cursor cursor2 = db.select(sql2);
        if (cursor.getCount() > 0 && cursor2.getCount() > 0){
            Intent intent = new Intent(this, MenuOrder.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Please insert customer or item in master menu!", Toast.LENGTH_LONG).show();
        }
    }

    public void keMenuPengembalian(View view) {
        Intent intent = new Intent(this, MenuPengembalian.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainHomepage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
