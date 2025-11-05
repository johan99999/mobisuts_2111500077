package com.hanlie.mobisuts_2111500077;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hanlie.mobisuts_2111500077.ClassGlobal;
import com.hanlie.mobisuts_2111500077.MainActivity;
import com.hanlie.mobisuts_2111500077.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DaftarAkun extends AppCompatActivity {

    EditText txtNama, txtHp, txtSekolah, txtAlamat, txtTtgll;
    Button btnUpfoto, btnDaftar;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_akun);

        txtNama = findViewById(R.id.txtNama);
        txtHp = findViewById(R.id.txtHp);
        txtSekolah = findViewById(R.id.txtSekolah);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtTtgll = findViewById(R.id.txtTtgll);
        btnUpfoto = findViewById(R.id.btnUpfoto);
        btnDaftar = findViewById(R.id.btnDaftar);

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
                    txtAlamat.setError("Alamat ga boleh kosong yah!");
                    txtAlamat.requestFocus();
                }
                else {
                    connectDB("daftar");
                }
            }
        });
    }

    private void connectDB(String aksi){
        stringRequest = new StringRequest(Request.Method.POST,
                ((ClassGlobal) getApplicationContext()).getUrl() + "daftar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String pesan = jObj.getString("pesan");
                            boolean hasil = jObj.getBoolean("hasil");
                            Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();
                            if (hasil && aksi.equalsIgnoreCase("daftar")) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Gagal hubungi Server incikboss! : " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                if(aksi.equalsIgnoreCase("daftar"))
                    param.put("aksi", "daftar");
                param.put("nama", txtNama.getText().toString().trim());
                param.put("nowa", txtHp.getText().toString().trim());
                param.put("sekolah", txtSekolah.getText().toString().trim());
                param.put("ttgll", txtTtgll.getText().toString().trim());
                param.put("alamat", txtAlamat.getText().toString().trim());
                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
