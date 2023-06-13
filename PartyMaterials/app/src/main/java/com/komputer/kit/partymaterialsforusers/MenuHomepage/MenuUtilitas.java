package com.komputer.kit.partymaterialsforusers.MenuHomepage;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.InsideMenuUtilitas.BackupData;
import com.komputer.kit.partymaterialsforusers.InsideMenuUtilitas.RestoreData;
import com.komputer.kit.partymaterialsforusers.MainSplash;
import com.komputer.kit.partymaterialsforusers.Modul;
import com.komputer.kit.partymaterialsforusers.R;

public class MenuUtilitas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utilitas);

        getSupportActionBar().setTitle("Utility Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cvBackup(View view) {
        Intent intent = new Intent(this, BackupData.class);
        startActivity(intent);
    }

    public void cvRestore(View view) {
        Intent intent = new Intent(this, RestoreData.class);
        startActivity(intent);
    }

    public void cvResetData(View view){
        final String  dirIn="/data/data/com.komputerkit.partymaterialsforusers/databases/";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Reset data and restart");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertDialog.Builder ale = new AlertDialog.Builder(MenuUtilitas.this);
                        ale.setMessage("Are you sure want to reset data?");
                        ale.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (Modul.deleteFile(dirIn + Database.DATABASE_NAME)) {
                                            Toast.makeText(MenuUtilitas.this, "Reset Succed!", Toast.LENGTH_SHORT).show();
                                            finishAffinity();
                                            startActivity(new Intent(MenuUtilitas.this,MainSplash.class));

                                        } else {
                                            Toast.makeText(MenuUtilitas.this, "Reset Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        ale.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = ale.create();
                        alertDialog.show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Reset Data");
        alertDialog.show();
    }

    public void cvCaraPenggunaan(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Move to Youtube tutorial? ");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")) ;
                startActivity(i);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MenuUtilitas.this, "Okay", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
