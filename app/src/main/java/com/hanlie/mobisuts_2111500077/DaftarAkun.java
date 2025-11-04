package com.hanlie.mobisuts_2111500077;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DaftarAkun extends AppCompatActivity {

    EditText txtNama, txtHp, txtSekolah, txtAlamat, txtTtgll;
    Button btnUpfoto, btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_akun);

        txtNama = (EditText) findViewById(R.id.txtNama);
        txtHp = (EditText) findViewById(R.id.txtHp);
        txtSekolah = (EditText) findViewById(R.id.txtSekolah);
        txtAlamat = (EditText) findViewById(R.id.txtAlamat);
        txtTtgll = (EditText) findViewById(R.id.txtTtgll);
        btnUpfoto = (Button) findViewById(R.id.btnUpfoto);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtHp.length() < 10){
                    txtHp.setError("Nomor Wa minimal 10 angka!");
                    txtHp.requestFocus();
                }
                else if(txtNama.getText().toString().trim().isEmpty()){
                    txtNama.setError("Nama ga boleh kosong yah!");
                    txtNama.requestFocus();
                }
                else if(txtSekolah.getText().toString().trim().isEmpty()){
                    txtSekolah.setError("Asal sekolah ga boleh kosong yah!");
                    txtSekolah.requestFocus();
                }
                else if(txtAlamat.getText().toString().trim().isEmpty()){
                    txtAlamat.setError("Asal sekolah ga boleh kosong yah!");
                    txtAlamat.requestFocus();
                }
                else {
                    String pesan = "";
                    pesan += "Nama: " + txtNama.getText().toString();
                    pesan += "\nTempat Tanggal Lahir: " + txtTtgll.getText().toString();
                    pesan += "\nNomor WA: " + txtHp.getText().toString();
                    pesan += "\nAsal Sekolah: " + txtSekolah.getText().toString();
                    pesan += "\nAlamat: " + txtAlamat.getText().toString();
                    Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}