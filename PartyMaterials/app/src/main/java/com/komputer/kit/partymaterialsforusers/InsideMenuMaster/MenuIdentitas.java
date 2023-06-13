package com.komputer.kit.partymaterialsforusers.InsideMenuMaster;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.Database;
import com.komputer.kit.partymaterialsforusers.R;

public class MenuIdentitas extends AppCompatActivity {

    Database db;

    private TextInputLayout textInputLayout;
    private TextInputEditText inpNama;
    private TextInputLayout textInputLayout2;
    private TextInputEditText inpAlamat;
    private TextInputLayout textInputLayout3;
    private TextInputEditText inpTelp;
    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_identitas);

        getSupportActionBar().setTitle("Identitiy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        load();
        selecData();
    }

    public void load(){
        db = new Database(this);
    }

    public void pesan(String isi){
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void selecData(){
        String sql = "SELECT * FROM tblidentitas";
        Cursor cursor = db.select(sql);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            inpNama.setText(cursor.getString(cursor.getColumnIndex("namatoko")));
            inpAlamat.setText(cursor.getString(cursor.getColumnIndex("alamat")));
            inpTelp.setText(cursor.getString(cursor.getColumnIndex("nohp")));
        } else {
            pesan("Data is Emty!");
        }
    }

    private void initView() {
        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        inpNama = (TextInputEditText) findViewById(R.id.inpNama);
        textInputLayout2 = (TextInputLayout) findViewById(R.id.textInputLayout2);
        inpAlamat = (TextInputEditText) findViewById(R.id.inpAlamat);
        textInputLayout3 = (TextInputLayout) findViewById(R.id.textInputLayout3);
        inpTelp = (TextInputEditText) findViewById(R.id.inpTelp);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
    }

    public void simpan(View view){
        String namaToko = inpNama.getText().toString();
        String alamat = inpAlamat.getText().toString();
        String nohp = inpTelp.getText().toString();
        String query = "UPDATE tblidentitas SET namatoko='"+namaToko+"' , alamat='"+alamat+"' , nohp="+nohp+" WHERE idtoko=1; ";


        if (db.runSql(query)){
            pesan("Update Succed!");
        }else{
            pesan("Update Failed!");
        }
    }

    public void hapus(View view) {
        inpNama.setText("");
        inpAlamat.setText("");
        inpTelp.setText("");
    }
}
