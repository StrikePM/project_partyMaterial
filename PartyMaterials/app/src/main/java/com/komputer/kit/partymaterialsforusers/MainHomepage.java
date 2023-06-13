package com.komputer.kit.partymaterialsforusers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import androidx.constraintlayout.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.MenuHomepage.MenuLaporan;
import com.komputer.kit.partymaterialsforusers.MenuHomepage.MenuMaster;
import com.komputer.kit.partymaterialsforusers.MenuHomepage.MenuTransaksi;
import com.komputer.kit.partymaterialsforusers.MenuHomepage.MenuUtilitas;

public class MainHomepage extends AppCompatActivity {

    private ConstraintLayout constraintLayout3;
    private TextView txtView;
    private ConstraintLayout line1;
    private ConstraintLayout line2;
    private TextView txt1;
    private TextView txt2;
    private ImageView calender;
    private TextView txt3;
    private TextView txt4;
    private TextView txt5;
    private TextView txt6;
    private TextView txt7;
    private TextView txt8;
    private ImageView img4;
    static final Integer WRITE_EXST = 0x3;

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_homepage);

        String[] PERMISSIONS ={
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainHomepage.this,PERMISSIONS, WRITE_EXST);
        }

        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);

        initView();
        dbload();
        selecData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selecData();
    }

    public void master(View view) {
        Intent intent = new Intent(this, MenuMaster.class);
        startActivity(intent);
    }

    public void transaksi(View view) {
        Intent intent = new Intent(this, MenuTransaksi.class);
        startActivity(intent);
    }

    public void laporan(View view) {
        Intent intent = new Intent(this, MenuLaporan.class);
        startActivity(intent);
    }

    public void utilitas(View view) {
        Intent intent = new Intent(this, MenuUtilitas.class);
        startActivity(intent);
    }

    public void dbload(){
        db = new Database(this);
    }

    public void selecData(){
        String sql = "SELECT * FROM tblidentitas";
        Cursor cursor = db.select(sql);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            txtView.setText(cursor.getString(cursor.getColumnIndex("namatoko")));
        } else {
            pesan("data kosong!");
        }
    }

    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        constraintLayout3 = (ConstraintLayout) findViewById(R.id.constraintLayout3);
        txtView = (TextView) findViewById(R.id.nmToko);
        line1 = (ConstraintLayout) findViewById(R.id.line1);
        line2 = (ConstraintLayout) findViewById(R.id.line2);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        calender = (ImageView) findViewById(R.id.srcPrinter);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txt5 = (TextView) findViewById(R.id.txt5);
        txt6 = (TextView) findViewById(R.id.txt6);
        txt7 = (TextView) findViewById(R.id.txt7);
        txt8 = (TextView) findViewById(R.id.txt8);
        img4 = (ImageView) findViewById(R.id.img4);
    }
}
