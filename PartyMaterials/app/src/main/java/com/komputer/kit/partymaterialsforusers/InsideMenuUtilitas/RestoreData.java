package com.komputer.kit.partymaterialsforusers.InsideMenuUtilitas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.InsideMenuMaster.MenuBarang;
import com.komputer.kit.partymaterialsforusers.MainSplash;
import com.komputer.kit.partymaterialsforusers.Modul;
import com.komputer.kit.partymaterialsforusers.R;

import java.io.File;
import java.util.ArrayList;

public class RestoreData extends AppCompatActivity {

    ListView listView ;
    String dirOut, dirIn;
    View v ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_data);

        getSupportActionBar().setTitle("Restore Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        v = this.findViewById(android.R.id.content);
        dirOut= Environment.getExternalStorageDirectory().toString() + "/Download/";
        dirIn="/data/data/com.komputerkit.partymaterialsforusers/databases/";
        listView = (ListView) findViewById(R.id.listView) ;
        try {
            File file = new File(dirOut) ;
            if(!file.exists()){
                file.mkdirs() ;
            }
            readFile();
        } catch (Exception e){
            Toast.makeText(this, "Fetch Data Failed!", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void readFile() {
        ArrayList arrayList = new ArrayList() ;
        ArrayAdapter arrayAdapter = new AdapterBackup(this,R.layout.item_restore,R.id.wadah,arrayList) ;
        listView.setAdapter(arrayAdapter);


        File dir = new File(dirOut) ;
        File[] isi = dir.listFiles() ; // ini penting
        if(isi.length > 0){
            for(int i = 0 ; i < isi.length ; i++){
                String nama = isi[i].getName() ;
                try {
                    String hasil = nama.substring(Database.DATABASE_NAME.length()) ;
                    if(nama.substring(0,(nama.length()-16)).equals(Database.DATABASE_NAME)){
                        arrayList.add("Date : " + hasil+"__"+nama);
                    }
                }catch (Exception e){

                }
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void u(final String db){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to restore data " +db+" ?");
        alertDialogBuilder.setPositiveButton("Restore",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(Modul.deleteFile(dirIn+Database.DATABASE_NAME)){
                            if(Modul.copyFile(dirOut,dirIn,db)){
                                File a = new File(dirIn+db) ;
                                File b = new File(dirIn+Database.DATABASE_NAME) ;
                                a.renameTo(b) ;
                                Toast.makeText(RestoreData.this, "Restore Succed, Restarting...", Toast.LENGTH_SHORT).show();
                                finishAffinity();

                                Intent intent = new Intent(RestoreData.this, MainSplash.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(RestoreData.this, "Restore Failed!", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(RestoreData.this, "Restore Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Restore Data");
        alertDialog.show();
    }

    public void restore(View v){
        String key = v.getTag().toString() ;
        try {
            u(key) ;
        }catch (Exception e){
            Toast.makeText(this, "Restore Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hapus(final View v){
        AlertDialog.Builder showAlert = new AlertDialog.Builder(this);
        showAlert.setTitle("Delete Confirm");
        showAlert.setMessage("Are you sure want to delete this data?");

        showAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String db = v.getTag().toString() ;
                String path = dirOut ;
                if(Modul.deleteFile(path+db)){
                    pesan("Delete Succed!");
                    readFile();
                } else {
                    pesan("Delete Failed!");
                }
            }
        });

        showAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pesan("Okay!");
            }
        });

        showAlert.show();
    }

    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}
