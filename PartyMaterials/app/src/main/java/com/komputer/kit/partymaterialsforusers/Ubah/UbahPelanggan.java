package com.komputer.kit.partymaterialsforusers.Ubah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.komputer.kit.partymaterialsforusers.R;

public class UbahPelanggan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_pelanggan);

        getSupportActionBar().setTitle("Ubah Pelanggan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
