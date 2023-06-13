package com.komputer.kit.partymaterialsforusers.InsideMenuTransaksi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.komputer.kit.partymaterialsforusers.ClassTransGetSet;
import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.Function;
import com.komputer.kit.partymaterialsforusers.KonfirmasiPembayaran;
import com.komputer.kit.partymaterialsforusers.MenuCariBarang;
import com.komputer.kit.partymaterialsforusers.MenuCariPelanggan;
import com.komputer.kit.partymaterialsforusers.Modul;
import com.komputer.kit.partymaterialsforusers.R;
import com.komputer.kit.partymaterialsforusers.ViewRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MenuOrder extends AppCompatActivity {

    private ScrollView scrollView2;
    private TextView tvShow;
    private TextInputLayout txtInput1;
    private TextInputEditText faktur;
    private ImageView calender;
    private TextInputLayout txtInput2;
    private TextInputEditText tglMulai;
    private TextInputLayout txtInput3;
    private TextInputEditText tglKembali;
    private ImageView img3;
    private EditText namaPlgn;
    private ImageView img5;
    private EditText barang;
    private ImageView img6;
    private EditText harga;
    private EditText jumlah;
    private Button btn1;
    private Button btn2;
    private String idPelanggan="";
    private String idbarang="";
    private String fkr = "000000000";
    private ViewRecyclerAdapter adapter;
    private RecyclerView rcvOrder;

    Double fixHarga=0.0,hasil;

    List<ClassTransGetSet> detailList = new ArrayList<ClassTransGetSet>();

    int hari, bulan, tahun;
    Calendar cal;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);

        getSupportActionBar().setTitle("Loaning");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cal = Calendar.getInstance();
        hari = cal.get(Calendar.DAY_OF_MONTH);
        bulan = cal.get(Calendar.MONTH);
        tahun = cal.get(Calendar.YEAR);

        load();
        initView();
        getFaktur();
        prizeTrans();
        setOrderDetail();
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).
                setTitle("Are you sure want to cancel the transaction?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Cursor cursor = db.select("SELECT * FROM tblorder WHERE total = 0");
                            if (cursor.getCount() > 0)
                                while (cursor.moveToNext()){
                                    db.runSql("DELETE FROM tblorder WHERE idorder = "+cursor.getString(0));
                                    db.runSql("DELETE FROM tblorderdetail WHERE idorder = "+cursor.getString(0));
                                }
                            finish();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void load(){
        db=new Database(this);

        rcvOrder = (RecyclerView) findViewById(R.id.rcvOrder);

        rcvOrder.setLayoutManager(new LinearLayoutManager(this));
        rcvOrder.setHasFixedSize(true);
    }

    private void prizeTrans() {
        jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    hasil=fixHarga*Double.valueOf(s.toString());
                    harga.setText(Modul.removeE(hasil));
                }
            }
        });

    }

    public void konfirmasi(View view) {
        String fakturz = faktur.getText().toString();
        String totalz = tvShow.getText().toString();

        if (totalz.equals("")){
            pesan("Please click save button!");
        }else {
            if (db.runSql("UPDATE tblorder SET total='"+totalz+"' WHERE faktur = '"+fakturz+"'")){
                Intent intent = new Intent(this, KonfirmasiPembayaran.class);
                startActivity(intent.putExtra("faktur",fkr));
                finish();
                pesan("Succed!");
            }else {
                pesan("Failed!");
            }
        }
    }

    public void getFaktur(){
        Cursor c = db.select("SELECT * FROM tblorder ORDER BY idorder ASC");
        Integer nextFaktur = c.getCount() + 1;
        fkr = (fkr.substring(String.valueOf(nextFaktur).length())) + nextFaktur;
        faktur.setText(fkr);
    }

    public void resetEdt(){
        barang.setText("");
        jumlah.setText("");
        harga.setText("");
    }

    private void initView() {
        scrollView2 = (ScrollView) findViewById(R.id.scrollView2);
        tvShow = (TextView) findViewById(R.id.tvShow);
        txtInput1 = (TextInputLayout) findViewById(R.id.txtInput1);
        faktur = (TextInputEditText) findViewById(R.id.faktur);
        calender = (ImageView) findViewById(R.id.srcPrinter);
        txtInput2 = (TextInputLayout) findViewById(R.id.txtInput2);
        tglMulai = (TextInputEditText) findViewById(R.id.tglMulai);
        txtInput3 = (TextInputLayout) findViewById(R.id.txtInput3);
        tglKembali = (TextInputEditText) findViewById(R.id.tglKembali);
        img3 = (ImageView) findViewById(R.id.img3);
        namaPlgn = (EditText) findViewById(R.id.ccari);
        img5 = (ImageView) findViewById(R.id.img5);
        barang = (EditText) findViewById(R.id.barang);
        img6 = (ImageView) findViewById(R.id.img6);
        harga = (EditText) findViewById(R.id.harga);
        jumlah = (EditText) findViewById(R.id.jumlah);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        tglMulai.setText(getDate());
        tglKembali.setText(getDate());

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
            }
        });
    }

    public void setDate(int i) {
        showDialog(i);
    }

    public String getDate() {
        String bln, tgl;

        if (hari < 10) {
            tgl = "0" + String.valueOf(hari);
        } else {
            tgl = String.valueOf(hari);
        }

        if (bulan < 9) {
            bln = "0" + String.valueOf(bulan + 1);
        } else {
            bln = String.valueOf(bulan + 1);
        }
        return tgl + "/" + bln + "/" + String.valueOf(tahun);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, tahun, bulan, hari);
        } else if (id == 2) {
            return new DatePickerDialog(this, date2, tahun, bulan, hari);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            String tgl = Modul.getDate("dd/MM/YYYY");

            if (thn < tahun){
                Function.setText(tglMulai, R.id.tglMulai, tgl);
            }else if(bln < bulan){
                Function.setText(tglMulai, R.id.tglMulai, tgl);
            }else if(day < hari){
                Function.setText(tglMulai, R.id.tglMulai, tgl);
            }else {
                Function.setText(tglMulai, R.id.tglMulai, Function.setDatePickerNormal(thn, bln+1, day));
            }
//            String bulan;
//
//            if (bln < 9) {
//                bulan = "0" + String.valueOf(bln + 1);
//            } else {
//                bulan = String.valueOf(bln + 1);
//            }
//            tglMulai.setText(String.valueOf(day) + "/" + bulan + "/" + String.valueOf(thn));
        }
    };

    private DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            String tgl = Modul.getDate("dd/MM/YYYY");

            if (thn < tahun){
                Function.setText(tglKembali, R.id.tglKembali, tgl);
            }else if(bln < bulan){
                Function.setText(tglKembali, R.id.tglKembali, tgl);
            }else if(day < hari){
                Function.setText(tglKembali, R.id.tglKembali, tgl);
            }else {
                Function.setText(tglKembali, R.id.tglKembali, Function.setDatePickerNormal(thn, bln+1, day));
            }

//            if (bln < 9) {
//                bulan = "0" + String.valueOf(bln + 1);
//            } else {
//                bulan = String.valueOf(bln + 1);
//            }
//            tglKembali.setText(String.valueOf(day) + "/" + bulan + "/" + String.valueOf(thn));
        }
    };

    public void cariPelanggan(View view) {
        Intent intent = new Intent(this, MenuCariPelanggan.class);
        startActivityForResult(intent, 1);
    }

    public void cariBarang(View view) {
        Intent intent = new Intent(this, MenuCariBarang.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            namaPlgn.setText(data.getStringExtra("pelanggan"));
            idPelanggan = data.getStringExtra("idpelanggan");
        } else if (resultCode == 2) {
            barang.setText(data.getStringExtra("barang"));
            idbarang = data.getStringExtra("idbarang");
            harga.setText(data.getStringExtra("harga"));
            fixHarga=Double.valueOf(data.getStringExtra("harga"));
        }
    }

    public void simpan(View view) {
        String fk = faktur.getText().toString();
        String mulai = tglMulai.getText().toString();
        String kembali = tglKembali.getText().toString();
        String nama = idPelanggan;
        String brng= idbarang;
        String jm = jumlah.getText().toString();
        String hrg = harga.getText().toString().replace(".","");

        if (fk.isEmpty() || mulai.isEmpty() || kembali.isEmpty() || nama.isEmpty() || brng.isEmpty() || jm.isEmpty() || hrg.isEmpty()){
            pesan("Please fill the emty field!");
        }else {
            Cursor c = db.select("SELECT * FROM tblorder WHERE faktur = '"+fk+"'");
            if (c.getCount() > 0){
                if (db.runSql("UPDATE tblorder SET tglorder = '"+mulai+"', tglselesai = '"+kembali+"'," +
                        " idpelanggan = "+nama+" WHERE faktur = '"+fk+"'")){
                    c.moveToFirst();
                    if (db.runSql("INSERT INTO tblorderdetail (idjasa, idorder, jumlah," +
                            " hargaorder) VALUES ("+brng+", "+c.getString(c.getColumnIndex("idorder"))+", '"+jm+"', '"+hrg+"')")){
                        pesan("Saved!");
                        resetEdt();
                        setOrderDetail();
                        setTotal();
                    }else {
                        pesan("Failed!");
                    }
                }else {
                    pesan("Failed2!");
                }
            }else {
                if (db.runSql("INSERT INTO tblorder (tglorder, tglselesai, idpelanggan, faktur, total) " +
                        "VALUES ('"+mulai+"','"+kembali+"',"+nama+",'"+fk+"',0)")){
                    Cursor idOrder = db.select("SELECT * FROM tblorder WHERE faktur = '"+fk+"'");
                    idOrder.moveToNext();
                    if (db.runSql("INSERT INTO tblorderdetail (idjasa, idorder, jumlah, " +
                            "hargaorder) VALUES ('"+String.valueOf(brng)+"','"+idOrder.getString(idOrder.getColumnIndex("idorder"))+"','"+jm+"','"+hrg+"')")){
                        pesan("Saved!");
                        resetEdt();
                        setOrderDetail();
                        setTotal();
                    }else {
                        pesan("Failed3!");
                    }
                }else {
                    pesan("Failed4!");
                }
            }
        }
    }

    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void setOrderDetail(){
        String fktr = faktur.getText().toString();
        String sql = "SELECT * FROM vorderdetail WHERE faktur = '"+fktr+"'";

        adapter = new ViewRecyclerAdapter(this, detailList);
        rcvOrder.setAdapter(adapter);
        Cursor cursor = db.select(sql);
        detailList.clear();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                String idOrderDetail = cursor.getString(cursor.getColumnIndex("idorderdetail"));
                String idOrder = cursor.getString(cursor.getColumnIndex("jasa"));
                String idJasa = cursor.getString(cursor.getColumnIndex("idjasa"));
                String jumlah = cursor.getString(cursor.getColumnIndex("jumlah"));
                String harga = cursor.getString(cursor.getColumnIndex("hargaorder"));

                detailList.add(new ClassTransGetSet(idJasa, idOrderDetail, idOrder, jumlah, harga));
            }

        }else {
            Toast.makeText(this, "Fill the emty field starting from the top!!!", Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
    }

    public void setTotal(){
       String total = faktur.getText().toString();
       Cursor c = db.select("SELECT SUM (hargaorder) FROM vorderdetail WHERE faktur = '"+total+"'");
       c.moveToFirst();
       tvShow.setText(Modul.removeE(c.getDouble(0)));
    }

    public void deleteItemOrder(String id){
        String idorderdetail = id;
        String sql = "DELETE FROM tblorderdetail WHERE idorderdetail= "+idorderdetail+"";

        if (db.runSql(sql)){
            Toast.makeText(this, "Delete Succed!", Toast.LENGTH_SHORT).show();
            load();
            setOrderDetail();
        }else {
            Toast.makeText(this, "Delete Failed!", Toast.LENGTH_SHORT).show();
        }
    }

}
