package com.komputer.kit.partymaterialsforusers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    Context context;
    public static final String DATABASE_NAME = "dbhq";
    private static final int VERSION = 1;

    SQLiteDatabase db;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

        this.context = context;

        db = this.getWritableDatabase();
        buatTabel();
    }

    public Cursor select(String sql){
        try{
            return db.rawQuery(sql, null);
        }catch(Exception e){
            return null;
        }
    }

    public boolean runSql(String sql){
        try {
            db.execSQL(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void buatTabel(){
        String tblidentitas = "CREATE TABLE \"tblidentitas\" (\n" +
                "\t\"idtoko\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"namatoko\"\tTEXT,\n" +
                "\t\"alamat\"\tTEXT,\n" +
                "\t\"nohp\"\tTEXT\n" +
                ");";
        runSql(tblidentitas);

        String tbljasa = "CREATE TABLE \"tbljasa\" (\n" +
                "\t\"idjasa\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"idkelompok\"\tINTEGER,\n" +
                "\t\"jasa\"\tTEXT,\n" +
                "\t\"harga\"\tREAL,\n" +
                "\tFOREIGN KEY(\"idkelompok\") REFERENCES \"tblkelompok\"(\"idkelompok\") on update cascade on delete restrict\n" +
                ");";
        runSql(tbljasa);

        String tblkelompok = "CREATE TABLE \"tblkelompok\" (\n" +
                "\t\"idkelompok\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"kelompok\"\tTEXT\n" +
                "\t\"usable\"\tTEXT\n" +
                ");";
        runSql(tblkelompok);

        String tblorder = "CREATE TABLE \"tblorder\" (\n" +
                "\t\"idorder\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"tglorder\"\tTEXT,\n" +
                "\t\"tglselesai\"\tTEXT,\n" +
                "\t\"idpelanggan\"\tINTEGER,\n" +
                "\t\"status\"\tINTEGER,\n" +
                "\t\"faktur\"\tTEXT,\n" +
                "\t\"total\"\tREAL,\n" +
                "\t\"bayar\"\tREAL,\n" +
                "\t\"kembali\"\tREAL,\n" +
                "\tFOREIGN KEY(\"idpelanggan\") REFERENCES \"tblpelanggan\"(\"idpelanggan\") on update cascade on delete restrict\n" +
                ");";
        runSql(tblorder);

        String tblorderdetail = "CREATE TABLE \"tblorderdetail\" (\n" +
                "\t\"idorderdetail\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"idjasa\"\tINTEGER,\n" +
                "\t\"idorder\"\tINTEGER,\n" +
                "\t\"jumlah\"\tINTEGER,\n" +
                "\t\"hargaorder\"\tREAL,\n" +
                "\tFOREIGN KEY(\"idorder\") REFERENCES \"tblorder\"(\"idorder\") on update cascade on delete restrict,\n" +
                "\tFOREIGN KEY(\"idjasa\") REFERENCES \"tbljasa\"(\"idjasa\") on update cascade on delete restrict\n" +
                ");";
        runSql(tblorderdetail);

        String tblpelanggan = "CREATE TABLE \"tblpelanggan\" (\n" +
                "\t\"idpelanggan\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"pelanggan\"\tTEXT,\n" +
                "\t\"alamat\"\tTEXT,\n" +
                "\t\"nohp\"\tTEXT\n" +
                ");";
        runSql(tblpelanggan);

//        String vorderdetail = "CREATE VIEW vorderdetail AS SELECT " +
//                "tblorderdetail.idorderdetail, " +
//                "tblorderdetail.idjasa, " +
//                "tblorderdetail.idorder, " +
//                "tblorderdetail.jumlah, " +
//                "tblorderdetail.hargaorder, " +
//                "tblorderdetail.keterangan, " +
//                "tbljasa.idkelompok, " +
//                "tbljasa.jasa, " +
//                "tbljasa.harga, " +
//                "tblkelompok.kelompok, " +
//                "tblorder.tglorder, " +
//                "tblorder.tglselesai, " +
//                "tblorder.status, " +
//                "tblorder.faktur, " +
//                "tblorder.idpelanggan, " +
//                "tblorder.total, " +
//                "tblorder.bayar, " +
//                "tblorder.kembali, " +
//                "tblpelanggan.pelanggan, " +
//                "tblpelanggan.alamat, " +
//                "tblpelanggan.nohp, " +
//                "tblorder.uangmuka " +
//                "FROM tblpelanggan " +
//                "INNER JOIN (tblorder INNER JOIN (tblkelompok INNER JOIN (tbljasa INNER JOIN tblorderdetail " +
//                "ON tbljasa.idjasa = tblorderdetail.idjasa) ON tblkelompok.idkelompok = tbljasa.idkelompok) " +
//                "ON tblorder.idorder = tblorderdetail.idorder) ON tblpelanggan.idpelanggan = tblorder.idpelanggan;\n";
        String vorderdetail = "CREATE VIEW IF NOT EXISTS vorderdetail AS SELECT " +
                "tblorderdetail.idorderdetail, " +
                "tblorderdetail.idjasa, " +
                "tblorderdetail.idorder, " +
                "tblorderdetail.jumlah, " +
                "tblorderdetail.hargaorder, " +
                "tbljasa.idkelompok, " +
                "tbljasa.jasa, " +
                "tbljasa.harga, " +
                "tblkelompok.kelompok, " +
                "tblorder.tglorder, " +
                "tblorder.tglselesai, " +
                "tblorder.status, " +
                "tblorder.faktur, " +
                "tblorder.idpelanggan, " +
                "tblorder.total, " +
                "tblorder.bayar, " +
                "tblorder.kembali, " +
                "tblpelanggan.pelanggan, " +
                "tblpelanggan.alamat, " +
                "tblpelanggan.nohp " +
                "FROM tblorderdetail " +
                "INNER JOIN tbljasa ON tblorderdetail.idjasa = tbljasa.idjasa " +
                "INNER JOIN tblkelompok ON tbljasa.idkelompok = tblkelompok.idkelompok " +
                "INNER JOIN tblpelanggan ON tblorder.idpelanggan = tblpelanggan.idpelanggan " +
                "INNER JOIN tblorder ON tblorderdetail.idorder = tblorder.idorder";
        runSql(vorderdetail);
        String vjasa = "CREATE VIEW IF NOT EXISTS vjasa AS SELECT " +
                "tbljasa.idjasa, " +
                "tbljasa.idkelompok, " +
                "tbljasa.jasa, " +
                "tbljasa.harga, " +
                "tblkelompok.kelompok " +
                "FROM tbljasa " +
                "INNER JOIN tblkelompok ON tbljasa.idkelompok = tblkelompok.idkelompok";
        runSql(vjasa);

        String sql = "SELECT * FROM tblidentitas";
        Cursor cursor = select(sql);
        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO tblidentitas ( namatoko, alamat, nohp) VALUES (\"PartyMaterial\",\"Default\",\"123123123\");");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
