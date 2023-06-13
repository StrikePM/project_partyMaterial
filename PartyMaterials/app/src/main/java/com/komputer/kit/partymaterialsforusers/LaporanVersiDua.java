package com.komputer.kit.partymaterialsforusers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LaporanVersiDua extends AppCompatActivity {

    EditText eCari;
    TextView eKe, eDari;
    ArrayList arrayList = new ArrayList();
    String tipe;
    Database db;
    String dari="", ke="";
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_versi_dua);

        getSupportActionBar().setTitle("Loaning Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        db = new Database(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tipe = getIntent().getStringExtra("tipe");

        eKe = (TextView)findViewById(R.id.eKe);
        eDari = (TextView)findViewById(R.id.eDari);
        eCari = (EditText) findViewById(R.id.eCarii);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tipe.equals("penjualan")){
                    arrayList.clear();
                    loadList(eCari.getText().toString());
                } else if (tipe.equals("pengembalian")){
                    arrayList.clear();
                    loadList2(eCari.getText().toString());
                } else if (tipe.equals("pendapatan")){
                    arrayList.clear();
                    loadList3(eCari.getText().toString());
                }
            }
        });

        TextView tvTotal = findViewById(R.id.tvIncome);
        if (tipe.equals("penjualan")){
            getSupportActionBar().setTitle("Loaning Report");
            loadList("");
            tvTotal.setVisibility(View.INVISIBLE);
        } else if (tipe.equals("pengembalian")){
            getSupportActionBar().setTitle("Return Report");
            loadList2("");
            tvTotal.setVisibility(View.INVISIBLE);
        } else if (tipe.equals("pendapatan")){
            Cursor c=db.select("SELECT SUM(total) FROM vorderdetail");
            if (c.getCount()>0){
                c.moveToNext();
                tvTotal.setText("Rp."+Modul.removeE(c.getDouble(0)));
            } else {
                tvTotal.setText("Rp.0");
            }
            getSupportActionBar().setTitle("Income Report");
            loadList3("");
        }
        setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        dari = setDatePicker(year,month+1,day) ;
        ke = setDatePicker(year,month+1,day) ;
        String now = setDatePickerNormal(year,month+1,day) ;
        eKe.setText(now);
        eDari.setText(now);
    }

    public void loadList(String cari) {
        String q = "";
        if (cari.equals("")) {
            q = "SELECT * FROM vorderdetail WHERE total>0 AND tglorder BETWEEN '"+dari+"' AND '"+ke+"'";
        } else {
            q = "SELECT * FROM vorderdetail WHERE total>0 AND (pelanggan LIKE '%"+cari+"%' OR faktur LIKE '%"+cari+"%') AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder ASC";
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanTransaksi(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.select(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String faktur = c.getString(c.getColumnIndex("faktur"));
                String tglorder = c.getString(c.getColumnIndex("tglorder"));
                String tglselesai = c.getString(c.getColumnIndex("tglselesai"));
                String pelanggan = c.getString(c.getColumnIndex("pelanggan"));
                String jasa = c.getString(c.getColumnIndex("jasa"));
                String jumlah = c.getString(c.getColumnIndex("jumlah"));
                String harga = c.getString(c.getColumnIndex("harga"));
                double tot = Double.parseDouble(jumlah)*Double.parseDouble(harga);
//                String tot = c.getString(c.getColumnIndex("total"));

                String campur = faktur + "\n" + tglorder +" - "+ tglselesai + "__" +"Customer : "+ pelanggan + "__" +"Item : "+ jasa + "__" + jumlah + " x " + removeE(harga) + " = " + removeE(tot);
                arrayList.add(campur);
            }
        } else {
        }
        adapter.notifyDataSetChanged();
    }

    public void loadList2(String cari) {
        String sql = "";
        if (cari.equals("")){
            sql = "SELECT * FROM vorderdetail WHERE status = 2";
        } else {
            sql = "SELECT * FROM vorderdetail WHERE status = 2 AND (pelanggan LIKE '%"+cari+"%' OR faktur LIKE '%"+cari+"%') AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder ASC";
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanTransaksi(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor cursor = db.select(sql);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String faktur = cursor.getString(cursor.getColumnIndex("faktur"));
                String tglorder = cursor.getString(cursor.getColumnIndex("tglorder"));
                String tglselesai = cursor.getString(cursor.getColumnIndex("tglselesai"));
                String nama = cursor.getString(cursor.getColumnIndex("pelanggan"));
                String barang = cursor.getString(cursor.getColumnIndex("jasa"));
                String jumlah = cursor.getString(cursor.getColumnIndex("jumlah"));

                String campur = faktur+"__"+tglorder+" - "+tglselesai+"__"+"Customer : "+nama+"__"+"Item : "+barang+"\nValues :"+jumlah+" Item";
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void loadList3(String cari) {
        String sql = "";
        if (cari.equals("")){
            sql = "SELECT * FROM vorderdetail WHERE status > 1";
        } else {
            sql = "SELECT * FROM vorderdetail WHERE status > 1 AND (pelanggan LIKE '%"+cari+"%' OR faktur LIKE '%"+cari+"%') AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder ASC";
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanTransaksi(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor cursor = db.select(sql);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String faktur = cursor.getString(cursor.getColumnIndex("faktur"));
                String tglorder = cursor.getString(cursor.getColumnIndex("tglorder"));
                String tglselesai = cursor.getString(cursor.getColumnIndex("tglselesai"));
                String nama = cursor.getString(cursor.getColumnIndex("pelanggan"));
                String notlp = cursor.getString(cursor.getColumnIndex("nohp"));
                String total = cursor.getString(cursor.getColumnIndex("total"));
                String bayar = cursor.getString(cursor.getColumnIndex("bayar"));
//                String kembali = cursor.getString(cursor.getColumnIndex("kembali"));

                String kembali = String.valueOf(Double.valueOf(bayar)-Double.valueOf(total));
//                if (Double.parseDouble(total) > Double.parseDouble(bayar)){
//                    kembali="-"+kembali;
//                }

                String campur = faktur + "\n" + tglorder +" - "+ tglselesai + "\nPhone : "+notlp+"__"+"Customer : "+nama+"__"+"Total : "+Function.removeE(total)+"__"+"Pay : "+Function.removeE(bayar)+"__"+"Change : "+Function.removeE(kembali);
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view){
        Intent i = new Intent(this, ActivityExportExcel.class);
        i.putExtra("tipe",tipe) ;
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tipe.equals("penjualan")){
            loadList(eCari.getText().toString());
        } else if (tipe.equals("pengembalian")){
            loadList2(eCari.getText().toString());
        } else if (tipe.equals("pendapatan")){
            loadList3(eCari.getText().toString());
        }
    }

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    public void filter(){
        loadList(eCari.getText().toString());
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if(id == 2){
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            eDari.setText(setDatePickerNormal(thn,bln+1,day));
            dari = setDatePicker(thn,bln+1,day) ;
            filter();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            eKe.setText(setDatePickerNormal(thn,bln+1,day));
            ke = setDatePicker(thn,bln+1,day) ;
            filter();
        }
    };
    //end date time picker

    public static String setDatePicker(int year , int month, int day) {
        String bln,thn,hri ;
        if(month < 10){
            bln = "0"+ String.valueOf(month) ;
        } else {
            bln = String.valueOf(month) ;
        }

        if(day < 10){
            hri = "0"+ String.valueOf(day) ;
        } else {
            hri = String.valueOf(day) ;
        }

        return hri+"/"+bln+"/"+String.valueOf(year);
    }

    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,thn,hri ;
        if(month < 10){
            bln = "0"+ String.valueOf(month) ;
        } else {
            bln = String.valueOf(month) ;
        }

        if(day < 10){
            hri = "0"+ String.valueOf(day) ;
        } else {
            hri = String.valueOf(day) ;
        }

        return hri+"/"+bln+"/"+String.valueOf(year);
    }

    public static String dateToNormal(String date){
        try {
            String b1 = date.substring(4) ;
            String b2 = b1.substring(2) ;

            String m = b1.substring(0,2) ;
            String d = b2.substring(0,2) ;
            String y = date.substring(0,4) ;
            return d+"/"+m+"/"+y ;
        }catch (Exception e){
            return "ini tanggal" ;
        }
    }

    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return numberFormat(df.format(value)) ;
    }
    public static String removeE(String value){
        double hasil = Double.parseDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return numberFormat(df.format(hasil)) ;
    }

    public static String numberFormat(String number){ // Rp. 1,000,000.00
        try{
            String hasil = "";
            String[] b = number.split("\\.") ;

            if(b.length == 1){
                String[] a = number.split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
            } else {
                String[] a = b[0].split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
                hasil+=","+b[1] ;
            }
            return  hasil ;
        }catch (Exception e){
            return  "" ;
        }
    }
}

class AdapterLaporanTransaksi extends RecyclerView.Adapter<AdapterLaporanTransaksi.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLaporanTransaksi(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pengembalian, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView faktur, tanggal, nama, item, notlp, print;

        public ViewHolder(View view) {
            super(view);

            faktur = (TextView) view.findViewById(R.id.txtPenFak);
            tanggal = (TextView) view.findViewById(R.id.txtPenTgl);
            nama = (TextView) view.findViewById(R.id.txtPenNam);
            item = (TextView) view.findViewById(R.id.txtPenBar);
            print = (TextView) view.findViewById(R.id.optPengembalian);
            print.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final String[] row = data.get(i).split("__");

        viewHolder.faktur.setText(row[0]);
        viewHolder.tanggal.setText(row[1]);
        viewHolder.nama.setText(row[2]);
        viewHolder.item.setText(row[3]);
    }
}
