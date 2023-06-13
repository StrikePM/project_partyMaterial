package com.komputer.kit.partymaterialsforusers.InsideMenuTransaksi;

import android.database.Cursor;
import android.os.Bundle;
import androidx.constraintlayout.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.ClassPengembalian;
import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.PengembalianAdapter;
import com.komputer.kit.partymaterialsforusers.R;

import java.util.ArrayList;
import java.util.List;

public class MenuPengembalian extends AppCompatActivity {

    private Database db;
    List<ClassPengembalian> pengembalianLists = new ArrayList<ClassPengembalian>();
    PengembalianAdapter adapter;
    RecyclerView rcvPengembalian;
    String keyword="";
    private ConstraintLayout constraintLayout9;
    private EditText cari;
    private LinearLayout linearLayout3;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pengembalian);

        getSupportActionBar().setTitle("Return Material");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        load();
        initView();
        selectData("");
    }

    public void load(){
        db = new Database(this);

        rcvPengembalian = findViewById(R.id.rcvPengembalian);

        rcvPengembalian.setLayoutManager(new LinearLayoutManager(this));
        rcvPengembalian.setHasFixedSize(true);
    }

    public void selectData(String a) {
        String sql = "";
        adapter = new PengembalianAdapter(this, pengembalianLists);
        rcvPengembalian.setAdapter(adapter);
        if (a.equals("")){
            sql = "SELECT * FROM vorderdetail WHERE status = 1";
        } else {
            sql = "SELECT * FROM vorderdetail WHERE status = 1 AND (pelanggan LIKE '%"+a+"%' OR faktur LIKE '%"+a+"%')";
        }
        Cursor cursor = db.select(sql);
        pengembalianLists.clear();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String faktur = cursor.getString(cursor.getColumnIndex("faktur"));
                String tanggal = cursor.getString(cursor.getColumnIndex("tglorder"));
                String nama = cursor.getString(cursor.getColumnIndex("pelanggan"));
                String tlp = cursor.getString(cursor.getColumnIndex("nohp"));
                String barang = cursor.getString(cursor.getColumnIndex("jasa"));
                String jumlah = cursor.getString(cursor.getColumnIndex("jumlah"));

                pengembalianLists.add(new ClassPengembalian(faktur, tanggal, nama, tlp, barang, jumlah));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        constraintLayout9 = (ConstraintLayout) findViewById(R.id.constraintLayout9);
        cari = (EditText) findViewById(R.id.ccari);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);

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

    public void deleteItemPengembalian(String id){
        String idOrder = id;
        String sql = "DELETE FROM tblorderdetail WHERE idorder= "+idOrder+"";

        if (db.runSql(sql)) {
            pesan("Delete Succed!");
            load();
            selectData("");
        }else {
            pesan("Delete Failed!");
        }
    }

    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}
