package com.komputer.kit.partymaterialsforusers;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komputer.kit.partymaterialsforusers.MenuHomepage.MenuTransaksi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class StrukPembayaran extends AppCompatActivity {
    Database db;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    String faktur;

    Config config, temp;
    String hasil, device;
    View v;
    int flagready = 0;
    private ConstraintLayout constraintLayout8;
    private TextInputLayout txtInput1;
    private TextInputEditText inpPrinter;
    private ImageView srcPrinter;
    private ConstraintLayout container;
    private TextView nmApk;
    private TextView fakturp;
    private TextView tanggal;
    private TextView pelanggan;
    private TextView lineOne;
    private TextView barang;
    private TextView harga;
    private TextView total1;
    private TextView lineTwo;
    private TextView bayar;
    private TextView kembali;
    private TextView penutup;
    private LinearLayout linearLayout;
    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struk_pembayaran);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        config = new Config(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new Config(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new Database(this);
        v = this.findViewById(android.R.id.content);

        device = config.getCustom("Printer", "");
        faktur = getIntent().getStringExtra("faktur");
        if (TextUtils.isEmpty(faktur)){
            Intent i = new Intent(this, MenuTransaksi.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        try {
            findBT();
            openBT();
        } catch (Exception e) {
            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
        }
        initView();
    }

    private void initView() {
        constraintLayout8 = (ConstraintLayout) findViewById(R.id.constraintLayout8);
        txtInput1 = (TextInputLayout) findViewById(R.id.txtInput1);
        inpPrinter = (TextInputEditText) findViewById(R.id.inpPrinter);
        srcPrinter = (ImageView) findViewById(R.id.srcPrinter);
        container = (ConstraintLayout) findViewById(R.id.container);
        nmApk = (TextView) findViewById(R.id.nmApk);
        fakturp = (TextView) findViewById(R.id.fakturp);
        tanggal = (TextView) findViewById(R.id.tanggal);
        pelanggan = (TextView) findViewById(R.id.pelanggann);
        lineOne = (TextView) findViewById(R.id.lineOne);
        barang = (TextView) findViewById(R.id.barang);
        harga = (TextView) findViewById(R.id.harga);
        total1 = (TextView) findViewById(R.id.total1);
        lineTwo = (TextView) findViewById(R.id.lineTwo);
        bayar = (TextView) findViewById(R.id.bayar);
        kembali = (TextView) findViewById(R.id.kembali);
        penutup = (TextView) findViewById(R.id.penutup);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
    }

    private void resetConnection() {
        if (mmInputStream != null) {
            try {mmInputStream.close();} catch (Exception e) {}
            mmInputStream = null;
        }

        if (mmOutputStream != null) {
            try {mmOutputStream.close();} catch (Exception e) {}
            mmOutputStream = null;
        }

        if (mmSocket != null) {
            try {mmSocket.close();} catch (Exception e) {}
            mmSocket = null;
        }
    }

    public void preview(View view){
        try {
            ConstraintLayout w = (ConstraintLayout) findViewById(R.id.container) ;
            setPreview() ;
            w.setVisibility(View.VISIBLE);
        } catch (Exception e){
            Toast.makeText(this, "Preview gagal, Karena pengisian toko kurang lengkap", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPreview() {
        Cursor cIdent = db.select("SELECT * FROM tblidentitas WHERE idtoko = 1");
        cIdent.moveToNext();
        Cursor cOrder = db.select("SELECT * FROM vorderdetail WHERE faktur='" + getIntent().getStringExtra("faktur") + "'");
        cOrder.moveToNext();
        Cursor cOrderDetil = db.select("SELECT * FROM vorderdetail WHERE faktur='" + getIntent().getStringExtra("faktur") + "'");

        String toko = cIdent.getString(cIdent.getColumnIndex("namatoko"));
        String alamat = cIdent.getString(cIdent.getColumnIndex("alamat"));
        String telp = cIdent.getString(cIdent.getColumnIndex("nohp"));
        nmApk.setText(toko + "\n" + alamat + "\n" + telp);
        String tFakturs = "Invoice  : " + getIntent().getStringExtra("faktur");
        fakturp.setText(tFakturs);
        String tPelanggans = "Customer : " + cOrder.getString(cOrder.getColumnIndex("pelanggan"));
        pelanggan.setText(tPelanggans);
        String tgls = "Date     : " + Function.getDate("dd-MM-yyyy");
        tanggal.setText(tgls);

        String header = Function.setCenter(toko) + "\n" +
                Function.setCenter(alamat) + "\n" +
                Function.setCenter(telp) + "\n" + "\n" + "\n" +
                tFakturs + "\n" +
                tgls + "\n" +
                tPelanggans + "\n" +
                Function.getStrip();

        String body = "";
        String view = "";
        while (cOrderDetil.moveToNext()) {
            String menu = cOrderDetil.getString(cOrderDetil.getColumnIndex("jasa"));
            String jumlah = cOrderDetil.getString(cOrderDetil.getColumnIndex("jumlah"));
            String harga = Modul.removeE(cOrderDetil.getString(cOrderDetil.getColumnIndex("harga")));
            String hargaAll = Modul.removeE(cOrderDetil.getString(cOrderDetil.getColumnIndex("hargaorder")));

            body += menu + "\n" +
                    jumlah + " X " + harga + " = " + hargaAll + "\n";
            view += menu + "\n" +
                    jumlah + " X " + harga + " = " + hargaAll + "\n";
        }
        barang.setText(view);
        body += Function.getStrip();

        String totals = "Total : " + Modul.removeE(cOrder.getString(cOrder.getColumnIndex("total")));
        total1.setText(totals);
        String baayar = "Pay : " + Modul.removeE(cOrder.getString(cOrder.getColumnIndex("bayar")));
        bayar.setText(baayar);

        Double kmbl = cOrder.getDouble(cOrder.getColumnIndex("bayar"))-cOrder.getDouble(cOrder.getColumnIndex("total"));
        String tKembali = Modul.removeE(cOrder.getDouble(cOrder.getColumnIndex("kembali"))).equals("0") ? Modul.removeE(kmbl):Modul.removeE(cOrder.getDouble(cOrder.getColumnIndex("kembali")));
        String kembalian = "Changes : " + tKembali;
        kembali.setText(kembalian);

        String footer = Function.setRight(totals) + "\n" +
                Function.setRight(baayar) + "\n" +
                Function.setRight(kembalian) + "\n" + "\n" + "\n" +
                Function.setCenter("Thank you for using") + "\n" +
                Function.setCenter("Party Material") + "\n" + "\n";

        hasil = header + body + footer;
    }

    public static String setRight(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<32-leng;i++){
            if((31-leng) == i){
                hasil += item ;
            } else {
                hasil += "  " ;
            }
        }
        return hasil ;
    }

    public void cari(View view){
        Intent i = new Intent(this,ActivityCetakCari.class) ;
        i.putExtra("faktur",faktur) ;
        startActivity(i);
    }

    public void cetak(View view) throws IOException {
        try {
            if(Function.getText(v,R.id.inpPrinter).equals("Tidak Ada Perangkat")){
                Toast.makeText(this, "There is no printer avaiable!", Toast.LENGTH_SHORT).show();
            } else if (flagready == 1){
                try {
                    setPreview();
                }catch (Exception e){
                    Toast.makeText(this, "Preview Failed!", Toast.LENGTH_SHORT).show();
                }
                sendData(hasil);

                onBackPressed();

            } else {
                Toast.makeText(this, "Printer isn't ready!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Printing process failed, please check your bluetooth or printer!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "There is no bluetooth adapter", Toast.LENGTH_SHORT).show();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                Function.setText(v,R.id.inpPrinter,"Printer isn't choosen yet!") ;
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(this.device)) {
                        mmDevice = device;
                        Function.setText(v,R.id.inpPrinter,this.device) ;
                        break;
                    }
                }
            } else {
                Function.setText(v,R.id.inpPrinter,"There are no avaiable devices!") ;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void openBT() throws IOException {
        try {
            resetConnection();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            //00001101-0000-1000-8000-00805F9B34FB
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            ConstraintLayout c = (ConstraintLayout) findViewById(R.id.simbol) ;
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                c.setBackgroundDrawable( getResources().getDrawable(R.drawable.ovalgreen) );
            } else {
                c.setBackground( getResources().getDrawable(R.drawable.ovalgreen));
            }
            flagready = 1 ;
//            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to connect printer", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to connect printer", Toast.LENGTH_SHORT).show();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                Toast.makeText(StrukPembayaran.this, data, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData(String hasil) throws IOException {
        try {
            hasil += "\n\n\n";
            mmOutputStream.write(hasil.getBytes());mBluetoothAdapter.cancelDiscovery() ; mmSocket.close();

            resetConnection();
            Toast.makeText(this, "Print Succed!", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
