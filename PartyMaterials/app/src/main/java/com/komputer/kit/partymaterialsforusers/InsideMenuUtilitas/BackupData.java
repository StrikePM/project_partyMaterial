package com.komputer.kit.partymaterialsforusers.InsideMenuUtilitas;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.Modul;
import com.komputer.kit.partymaterialsforusers.R;

import java.io.File;

public class BackupData extends AppCompatActivity {

    Modul config,temp;
    Database db;
    String deviceid;
    SharedPreferences getPrefs;
    String[] header;
    String idAkun;
    private String inputFile;
    String nama;
    Boolean needDate = Boolean.valueOf(true);
    String path,dirIn;
    String rincian;
    int row = 0;

    String type;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_data);

        getSupportActionBar().setTitle("Backup Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.db = new Database(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
        this.dirIn="/data/data/com.komputerkit.partymaterialsforusers/databases/";

        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        File file = new File(path) ;
        if(!file.exists()){
            file.mkdirs() ;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backup(View view) {
        String dbName = db.DATABASE_NAME;
        String dbOut = db.DATABASE_NAME+Modul.getDate("HH:mm dd-MM-yyyy");

        if(Modul.copyFile(dirIn,path,dbName)){
            if(Modul.renameFile(path,dbName,dbOut)){
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}

