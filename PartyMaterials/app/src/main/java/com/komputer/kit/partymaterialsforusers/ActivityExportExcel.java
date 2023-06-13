package com.komputer.kit.partymaterialsforusers;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ActivityExportExcel extends AppCompatActivity {

    Config config,temp;
    Database db;
    SharedPreferences getPrefs;
    String nama;
    String path;

    int row = 0;

    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;
    String type;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_excel);

        Function.btnBack("Export Excel",getSupportActionBar());

        getWindow().setSoftInputMode(3);
        this.config = new Config(getSharedPreferences("config", 0));
        this.temp = new Config(getSharedPreferences("temp", 0));
        this.db = new Database(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("tipe");
        this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";


        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        try {
            new File(Environment.getExternalStorageDirectory() + "/PartyMaterial").mkdirs();
        } catch (Exception e) {
        }
        setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setText(){
        Function.setText(v,R.id.ePath,"Internal Storage/Download/");
    }

    public void export(View view) throws IOException, WriteException {
        if (type.equals("pelanggan")){
            nama="Laporan Pelanggan";
            exPelanggan();
        }else if(type.equals("barang")){
            nama="Laporan Barang";
            exBarang();
        }else if (type.equals("pendapatan")){
            nama="Laporan Pendapatan";
            exLaporanPendapatan();
        }else if(type.equals("pengembalian")){
            nama="Laporan Pengembalian";
            exLaporanPengembalian();
        } else if (type.equals("penjualan")){
            nama="Laporan Penjualan";
            exLaporanPenjualan();
        }
    }

    private void exLaporanPenjualan() throws IOException, WriteException {
        File file = new File(path+nama+" "+Function.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        setHeader(db,sheet,9);
        excelNextLine(sheet,2) ;

        String[] judul = {"No.", "Faktur", "Tanggal Order", "Tanggal Selesai", "Barang", "Jumlah Barang", "Harga Jual", "Total", "Pelanggan"};
        setJudul(sheet,judul);

        Cursor c = db.select("SELECT * FROM vorderdetail") ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String jasa = Function.getString(c, "jasa");
                String pelanggan = Function.getString(c, "pelanggan");
                String faktur = Function.getString(c, "faktur");
                String tglselesai = Function.getString(c, "tglselesai");
                String tglorder = Function.getString(c, "tglorder");
                String jumlah = Function.getString(c, "jumlah");
                String harga = Function.getString(c, "harga");
                double tot = Double.parseDouble(jumlah)*Double.parseDouble(harga);




                addLabel(sheet,col++, row, Function.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, Function.dateToNormal(tglorder));
                addLabel(sheet,col++, row, Function.dateToNormal(tglselesai));
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, jumlah);
                addLabel(sheet,col++, row, Function.removeE(harga));
                addLabel(sheet,col++, row, Function.removeE(tot));
                addLabel(sheet,col++, row, pelanggan);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Succed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();
        }
    }

    private void exLaporanPendapatan() throws IOException, WriteException {
        File file = new File(path+nama+" "+Function.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        setHeader(db,sheet,8);
        excelNextLine(sheet,2) ;

        String[] judul = {
                "No.",
                "Faktur",
                "Tanggal Order",
                "Tanggal Selesai",
                "Total",
                "Bayar",
                "Kembali",
                "Nama Pelanggan"};
        setJudul(sheet,judul);

        Cursor c = db.select("SELECT * FROM vorderdetail") ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String pelanggan = Function.getString(c, "pelanggan");
                String faktur = Function.getString(c, "faktur");
                String tnggalorder = Function.getString(c, "tglorder");
                String tnggalselesai = Function.getString(c, "tglselesai");
                String totalbayar = Function.getString(c, "total");
                String jumlahbayar = Function.getString(c, "bayar");
                String kembali = Function.getString(c, "kembali");

                addLabel(sheet,col++, row, Function.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, Function.dateToNormal(tnggalorder));
                addLabel(sheet,col++, row, Function.dateToNormal(tnggalselesai));
                addLabel(sheet,col++, row, Function.removeE(totalbayar));
                addLabel(sheet,col++, row, Function.removeE(jumlahbayar));
                addLabel(sheet,col++, row, Function.removeE(kembali));
                addLabel(sheet,col++, row, pelanggan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Succed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();
        }
    }

    private void exLaporanPengembalian() throws IOException, WriteException {
        File file = new File(path+nama+" "+Function.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        setHeader(db,sheet,7);
        excelNextLine(sheet,2) ;

        String[] judul = {
                "No.",
                "Faktur",
                "Tanggal Order",
                "Tanggal Selesai",
                "Nama Pelanggan",
                "Barang",
                "Jumlah"};
        setJudul(sheet,judul);

        Cursor c = db.select("SELECT * FROM vorderdetail");
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = Function.getString(c, "faktur");
                String tglorder = Function.getString(c, "tglorder");
                String tglselesai = Function.getString(c, "tglselesai");
                String jasa = Function.getString(c, "jasa");
                String jumlahjual = Function.getString(c, "jumlah");
                String pelanggan = Function.getString(c, "pelanggan");

                addLabel(sheet,col++, row, Function.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, Function.dateToNormal(tglorder));
                addLabel(sheet,col++, row, Function.dateToNormal(tglselesai));
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, jumlahjual);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Succed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exBarang() throws IOException, WriteException {
        File file = new File(path+nama+" "+Function.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        String[] judul = {"No.", "Kelompok", "Barang", "Harga"};
        setHeader(db,sheet,4);
        excelNextLine(sheet,2) ;
        setJudul(sheet,judul);

        Cursor c = db.select("SELECT * FROM tbljasa") ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String idkelompok = c.getString(c.getColumnIndex("idkelompok"));
                Cursor cur = db.select("SELECT * FROM tblkelompok WHERE idkelompok='"+idkelompok+"'");
                cur.moveToNext();

                String jasa = c.getString(c.getColumnIndex("jasa"));
                String harga = c.getString(c.getColumnIndex("harga"));
                String kelompok = cur.getString(cur.getColumnIndex("kelompok"));


                addLabel(sheet,col++, row, Function.intToStr(no));
                addLabel(sheet,col++, row, kelompok);
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, Function.removeE(harga));



                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Succed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exPelanggan() throws IOException, WriteException {
        File file = new File(path+nama+" "+Function.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        setHeader(db,sheet,4);
        excelNextLine(sheet,2) ;

        String[] judul = {"No.", "Nama Pelanggan","Alamat","Nomor Telp"} ;
        setJudul(sheet,judul);

        Cursor c = db.select("SELECT * FROM tblpelanggan");
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = Function.getString(c,"pelanggan") ;
                String alamat = Function.getString(c,"alamat") ;
                String telp = Function.getString(c,"nohp") ;

                addLabel(sheet,col++, row, Function.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Succed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();
        }
    }

    public void setHeader(Database db, WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.select("SELECT * FROM tblidentitas WHERE idtoko") ;
            c.moveToNext() ;

            addLabel(sheet, row++,Function.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++,Function.getString(c,"alamat"),JumlahKolom);
            addLabel(sheet, row++,Function.getString(c,"nohp"),JumlahKolom);
        }catch (Exception e){

        }
    }

    public void setCenter(CSVWriter csvWriter, int JumlahKolom, String value){
        try {
            int baru ;
            if(JumlahKolom%2 == 1){
                baru = JumlahKolom-1 ;
            } else {
                baru = JumlahKolom ;
            }
            int i ;
            String[] a = new String[baru];
            for(i = 0 ; i < baru/2 ; i++){
                a[i] = "" ;
            }
            a[i] = value ;
            csvWriter.writeNext(a);
        } catch (Exception e){

        }
    }

    private void createContent(WritableSheet sheet) throws WriteException,
            RowsExceededException {
        int startSum = row+1;
        // Write a few number
        for (int i = 1; i < 10; i++) {
            // First column
            addNumber(sheet, 0, row , i + 10);
            // Second column
            addNumber(sheet, 1, row++, i * i);
        }

        int endSum = row;
        // Lets calculate the sum of it
        StringBuffer buf = new StringBuffer();
        buf.append("SUM(A"+startSum+":A"+endSum+")");
        Formula f = new Formula(0, row, buf.toString());
        sheet.addCell(f);
        buf = new StringBuffer();
        buf.append("SUM(B"+startSum+":B"+endSum+")");
        f = new Formula(1, row, buf.toString());
        sheet.addCell(f);
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBold);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           Integer integer) throws WriteException, RowsExceededException {
        jxl.write.Number number;
        number = new jxl.write.Number(column, row, integer, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    private void addLabel(WritableSheet sheet, int row, String s,int JumlahKolom)
            throws WriteException, RowsExceededException {
        Label label;
        JumlahKolom-- ;
        WritableCellFormat newFormat = null;
        newFormat = new WritableCellFormat(timesBold) ;
        newFormat.setAlignment(Alignment.CENTRE) ;
        label = new Label(0, row, s, newFormat) ;
        sheet.addCell(label);
        sheet.mergeCells(0,row,JumlahKolom,row) ; // parameters -> col1,row1,col2,row2
    }

    private void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automaticall                y wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        WritableFont times10ptBold = new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD, false);
        timesBold = new WritableCellFormat(times10ptBold);
        // Lets automatically wrap the cells
        timesBold.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(timesBold);

//        cv.setAutosize(true);
    }

    public Boolean excelNextLine(WritableSheet sheet, int total){
        try {
            for (int i = 0 ; i < total ; i++){
                addLabel(sheet,0,row++,"");
            }
            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public void setJudul(WritableSheet sheet, String[] val) throws WriteException {
        int col = 0 ;
        for (int i = 0 ; i < val.length ; i++){
            addCaption(sheet,col++,row,val[i]);
        }
        row++ ;
    }
}
