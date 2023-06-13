package com.komputer.kit.partymaterialsforusers.Ubah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.komputer.kit.partymaterialsforusers.R;

public class UbahKategori extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kategori);

        getSupportActionBar().setTitle("Ubah Kategori");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
