package com.komputer.kit.partymaterialsforusers;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.constraintlayout.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.MenuHomepage.MenuTransaksi;

import java.util.Calendar;

public class KonfirmasiPembayaran extends AppCompatActivity {

    int hari, bulan, tahun;
    Calendar cal;
    private CardView cardView;
    private TextView txtKonFak;
    private TextView txtKonPel;
    private TextView txtKonTot;
    private ConstraintLayout constraintLayout7;
    private ImageView konCal;
    private TextInputLayout txtInput3;
    private TextInputEditText konInp;
    private EditText edtKonBay;
    private EditText edtKonKem;
    private Button btnKonfirmasi;
    private Database db;
    String faktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);
        db = new Database(this);

        getSupportActionBar().setTitle("Confirm");

        initView();
        selectData();
        konfirmasi();
        this.faktur = faktur;
//        faktur = getIntent().getStringExtra("faktur");
        Bundle extra = getIntent().getExtras();
        if (extra == null){

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).
                setTitle("WARNIG!!! you will lost your order list").
                setPositiveButton("Cancel transaction", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.runSql("DELETE FROM tblorder WHERE faktur = '"+getIntent().getStringExtra("faktur")+"'");
                            db.runSql("DELETE FROM tblorderdetail WHERE faktur = '"+getIntent().getStringExtra("faktur")+"'");
                            finish();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }

    private void initView() {
        cardView = (CardView) findViewById(R.id.cardView);
        txtKonFak = (TextView) findViewById(R.id.txtKonFak);
        txtKonPel = (TextView) findViewById(R.id.txtkonPel);
        txtKonTot = (TextView) findViewById(R.id.txtKonTot);
        txtInput3 = (TextInputLayout) findViewById(R.id.txtInput3);
        edtKonBay = (EditText) findViewById(R.id.bayar);
        edtKonKem = (EditText) findViewById(R.id.kembali);
        btnKonfirmasi = (Button) findViewById(R.id.btnKonfirmasi);
    }

    public void struk(View view) {
//        final String faktur = getIntent().getStringExtra("faktur");
        String bayar = edtKonBay.getText().toString();
        String kembali = Modul.parseDF(edtKonKem.getText().toString());

        if (bayar.isEmpty()){
            pesan("Input money is needed!");
        } else {
//            Toast.makeText(this, "UPDATE tblorder SET bayar = '"+bayar+"', kembali = '"+kembali+"', status=1 WHERE faktur = "+getIntent().getStringExtra("faktur"), Toast.LENGTH_SHORT).show();
            if (db.runSql("UPDATE tblorder SET bayar = '"+bayar+"', kembali = '"+kembali+"', status=1 WHERE faktur = '"+getIntent().getStringExtra("faktur")+"'")){
                pesan("Succes");

                AlertDialog.Builder showAlert = new AlertDialog.Builder(this);
                showAlert.setTitle("Pay Confirm");
                showAlert.setMessage("Are you want to print the bill?");

                showAlert.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(KonfirmasiPembayaran.this, StrukPembayaran.class);
                        startActivity(intent.putExtra("faktur",getIntent().getStringExtra("faktur")));
                        finish();
                    }
                });

                showAlert.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pesan("Okay!");
                        Intent intent = new Intent(KonfirmasiPembayaran.this, MenuTransaksi.class);
                        startActivity(intent);
                    }
                });

                showAlert.show();
            }else {
                pesan("Failed");
            }
        }
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void selectData(){
        Cursor c = db.select("SELECT * FROM vorderdetail WHERE faktur = '"+getIntent().getStringExtra("faktur")+"'");
        c.moveToFirst();
        txtKonFak.setText(c.getString(c.getColumnIndex("faktur")));
        txtKonPel.setText(c.getString(c.getColumnIndex("pelanggan")));
        txtKonTot.setText(Modul.removeE(c.getDouble(c.getColumnIndex(("total")))));
    }

    public void konfirmasi(){
        final String total = "SELECT * FROM tblorder WHERE faktur = '"+getIntent().getStringExtra("faktur")+"'";
        Cursor c = db.select(total);
        c.moveToFirst();
        txtKonTot.setText(Modul.removeE(c.getDouble(c.getColumnIndex("total"))));
        edtKonBay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    String hasil = String.valueOf(Double.valueOf(s.toString())-Double.valueOf(txtKonTot.getText().toString()));
                    edtKonKem.setText(Function.removeE(hasil));
                }
            }
        });

    }
}
